package com.es.crmInmobiliaria.service;

import com.es.crmInmobiliaria.dtos.PropiedadCreateDTO;
import com.es.crmInmobiliaria.dtos.PropiedadDTO;
import com.es.crmInmobiliaria.dtos.PropiedadUpdateDTO;
import com.es.crmInmobiliaria.error.exception.DataBaseException;
import com.es.crmInmobiliaria.error.exception.NotFoundException;
import com.es.crmInmobiliaria.model.Propiedad;
import com.es.crmInmobiliaria.model.Propietario;
import com.es.crmInmobiliaria.model.Usuario;
import com.es.crmInmobiliaria.repository.PropiedadRepository;
import com.es.crmInmobiliaria.repository.PropietarioRepository;
import com.es.crmInmobiliaria.repository.UsuarioRepository;
import com.es.crmInmobiliaria.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class PropiedadService {
    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PropietarioRepository propietarioRepository;

    @Autowired
    private Mapper mapper;

    public List<PropiedadDTO> getAll() {
        List<Propiedad> propiedades;

        try {
            propiedades = propiedadRepository.findAll();
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        List<PropiedadDTO> propiedadesDTOS = new ArrayList<>();

        propiedades.forEach(propiedad -> {
            PropiedadDTO propiedadDTO = mapper.entityToDTO(propiedad);

            propiedadesDTOS.add(propiedadDTO);
        });

        return propiedadesDTOS;
    }

    public PropiedadDTO findById(String id) {
        Long idL = 0L;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id debe de ser un número correcto");
        }

        Propiedad propiedad = null;

        try {
            propiedad = propiedadRepository.findById(idL).orElseThrow(() -> new NotFoundException("No se ha encontrado ninguna propiedad con esa id"));
        } catch (NotFoundException e) {
            throw new NotFoundException("No se ha encontrado ninguna propiedad con esa id");
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        return mapper.entityToDTO(propiedad);
    }

    public PropiedadCreateDTO create(PropiedadCreateDTO propiedadDTO, Principal principal) {
        if (propiedadDTO.getDireccion() == null || propiedadDTO.getDireccion().isBlank()) {
            throw new IllegalArgumentException("El campo direccion no puede ser null");
        }

        if (propiedadDTO.getPrecio() == null) {
            throw new IllegalArgumentException("El campo precio no puede ser null");
        }

        if (propiedadDTO.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        String username = principal.getName();

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("El usuario no puede ser null");
        }

        if (propiedadRepository.findByDireccion(propiedadDTO.getDireccion().toUpperCase()).isPresent()) {
            throw new IllegalArgumentException("Ya hay una propiedad con esa direccion");
        }

        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("No se ha encontrado ningun usuario con ese nombre"));

        Propiedad propiedad = new Propiedad();

        propiedad.setDireccion(propiedadDTO.getDireccion().toUpperCase());
        propiedad.setPrecio(propiedadDTO.getPrecio());
        propiedad.setVendedor(usuario);
        propiedad.setOculta(propiedadDTO.getOculta());
        propiedad.setVendida(propiedadDTO.getVendida());

        if (propiedadDTO.getId_propietario() != null && !propiedadDTO.getId_propietario().isBlank()) {
            Long idL = 0L;

            try {
                idL = Long.parseLong(propiedadDTO.getId_propietario());
            } catch (NumberFormatException e) {
                throw new NumberFormatException("La id del propietario debe de ser null o ser un número");
            }

            Propietario propietario = null;

            try {
                propietario = propietarioRepository.findById(idL).orElseThrow(() -> new NotFoundException("No se ha encontrado ningun propietario con esa id"));
            } catch (NotFoundException e) {
                throw new NotFoundException("No se ha encontrado ningun propietario con esa id");
            } catch (Exception e) {
                throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
            }

            propiedad.setPropietario(propietario);
        }

        try {
            propiedadRepository.save(propiedad);
        } catch (Exception e) {
            throw new DataBaseException("Error al crear la propiedad: " + e.getMessage());
        }

        return propiedadDTO;
    }

    public PropiedadUpdateDTO update(String id, PropiedadUpdateDTO propiedadDTO) {
        if (propiedadDTO.getPrecio() == null) {
            throw new IllegalArgumentException("El campo precio no puede ser null");
        }

        if (propiedadDTO.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }

        Usuario usuario = null;

        Long idUsuario = 0L;

        try {
            idUsuario = Long.parseLong(propiedadDTO.getId_usuario());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id del usuario debe de ser null o ser un número");
        }

        try {
            usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new NotFoundException("No se ha encontrado ningun usuario con esa id"));
        } catch (NotFoundException e) {
            throw new NotFoundException("No se ha encontrado ningun usuario con esa id");
        }catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        Propiedad propiedad = null;

        Long idP = 0L;

        try {
            idP = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id debe de ser un número correcto");
        }

        try {
            propiedad = propiedadRepository.findById(idP).orElseThrow(() -> new NotFoundException("No se ha encontrado ninguna propiedad con esa id"));
        } catch (NotFoundException e) {
            throw new NotFoundException("No se ha encontrado ninguna propiedad con esa id");
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        propiedad.setPrecio(propiedadDTO.getPrecio());
        propiedad.setVendida(propiedadDTO.getVendida());
        propiedad.setOculta(propiedadDTO.getOculta());
        propiedad.setVendedor(usuario);

        if (propiedadDTO.getId_propietario() != null && !propiedadDTO.getId_propietario().isBlank()) {
            Long idL = 0L;

            try {
                idL = Long.parseLong(propiedadDTO.getId_propietario());
            } catch (NumberFormatException e) {
                throw new NumberFormatException("La id del propietario debe de ser null o ser un número");
            }

            Propietario propietario = null;

            try {
                propietario = propietarioRepository.findById(idL).orElseThrow(() -> new NotFoundException("No se ha encontrado ningun propietario con esa id"));
            } catch (NotFoundException e) {
                throw new NotFoundException("No se ha encontrado ningun propietario con esa id");
            } catch (Exception e) {
                throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
            }

            propiedad.setPropietario(propietario);
        }

        try {
            propiedadRepository.save(propiedad);
        } catch (Exception e) {
            throw new DataBaseException("Error al actualizar la propiedad: " + e.getMessage());
        }

        return propiedadDTO;
    }

    public void delete(String id) {
        Long idP = 0L;

        try {
            idP = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id debe de ser un número correcto");
        }

        Propiedad propiedad = null;

        try {
            propiedad = propiedadRepository.findById(idP).orElseThrow(() -> new NotFoundException("No se ha encontrado ninguna propiedad con esa id"));
        } catch (NotFoundException e) {
            throw new NotFoundException("No se ha encontrado ninguna propiedad con esa id");
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        propiedadRepository.delete(propiedad);
    }
}
