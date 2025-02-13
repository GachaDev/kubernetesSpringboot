package com.es.crmInmobiliaria.util;

import com.es.crmInmobiliaria.dtos.PropiedadDTO;
import com.es.crmInmobiliaria.dtos.PropietarioCreateDTO;
import com.es.crmInmobiliaria.dtos.PropietarioDTO;
import com.es.crmInmobiliaria.dtos.UsuarioDTO;
import com.es.crmInmobiliaria.model.Propiedad;
import com.es.crmInmobiliaria.model.Propietario;
import com.es.crmInmobiliaria.model.Usuario;
import org.springframework.stereotype.Service;

@Service
public class Mapper {
    public UsuarioDTO entityToDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getUsername(), usuario.getRol(), usuario.getFecha_registro());
    }

    public PropiedadDTO entityToDTO(Propiedad propiedad) {
        return new PropiedadDTO(propiedad.getId(), propiedad.getDireccion(), propiedad.getPrecio(), propiedad.getVendida(), propiedad.getOculta(), propiedad.getVendedor().getId().toString());
    }

    public PropietarioDTO entityToDTO(Propietario propietario) {
        return new PropietarioDTO(propietario.getId(), propietario.getNombre(), propietario.getApellidos(), propietario.getTelefono(), propietario.getGenero(), propietario.getCasado(), propietario.getN_hijos(), propietario.getUsuario() != null ? propietario.getUsuario().getId() : null);
    }

    public Propietario DTOToEntity(PropietarioCreateDTO propietarioCreateDTO) {
        return new Propietario(propietarioCreateDTO.getNombre(), propietarioCreateDTO.getApellidos(), propietarioCreateDTO.getTelefono(), propietarioCreateDTO.getGenero(), propietarioCreateDTO.getCasado(), propietarioCreateDTO.getN_hijos());
    }
}
