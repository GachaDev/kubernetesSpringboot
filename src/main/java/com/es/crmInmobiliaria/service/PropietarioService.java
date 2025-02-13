package com.es.crmInmobiliaria.service;

import com.es.crmInmobiliaria.dtos.PropietarioCreateDTO;
import com.es.crmInmobiliaria.dtos.PropietarioDTO;
import com.es.crmInmobiliaria.error.exception.BadRequestException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class PropietarioService {
    @Autowired
    private PropietarioRepository propietarioRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PropiedadRepository propiedadRepository;
    @Autowired
    private Mapper mapper;

    public void validateDTO(PropietarioCreateDTO propietarioCreateDTO) {
        //regex validar numero español
        String regex = "^(\\+34|0034)?[6-9]\\d{8}$";
        Pattern pattern = Pattern.compile(regex);

        if (propietarioCreateDTO.getTelefono() == null || propietarioCreateDTO.getTelefono().isBlank()) {
            throw new BadRequestException("El campo telefono no puede estar vacío.");
        }

        if (!pattern.matcher(propietarioCreateDTO.getTelefono()).matches()) {
            throw new BadRequestException("El campo telefono no tiene un formato válido");
        }

        if (propietarioCreateDTO.getGenero() == null || (!propietarioCreateDTO.getGenero().equalsIgnoreCase("Hombre") && !propietarioCreateDTO.getGenero().equalsIgnoreCase("Mujer") && !propietarioCreateDTO.getGenero().equalsIgnoreCase("No identificado"))) {
            throw new IllegalArgumentException("El campo género debe ser 'Hombre', 'Mujer' o 'No identificado'.");
        }

        if (propietarioCreateDTO.getN_hijos() < 0) {
            throw new BadRequestException("El campo n_hijos debe ser mayor o igual que 0.");
        }
    }

    public List<PropietarioDTO> getAll() {
        List<Propietario> propietarios;

        try {
            propietarios = propietarioRepository.findAll();
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        List<PropietarioDTO> propietariosDTOS = new ArrayList<>();

        propietarios.forEach(propietario -> {
            PropietarioDTO propietariosDTO = mapper.entityToDTO(propietario);

            propietariosDTOS.add(propietariosDTO);
        });

        return propietariosDTOS;
    }

    public PropietarioDTO getById(String id) {
        Long idL = 0L;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id debe de ser un número correcto");
        }

        Propietario propietario = null;

        try {
            propietario = propietarioRepository.findById(idL).orElseThrow(() -> new NotFoundException("No se ha encontrado ningun propietario con esa id"));
        } catch (NotFoundException e) {
            throw new NotFoundException("No se ha encontrado ningun propietario con esa id");
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        return mapper.entityToDTO(propietario);
    }

    public PropietarioCreateDTO create(PropietarioCreateDTO propietarioCreateDTO) {
        validateDTO(propietarioCreateDTO);

        Propietario propietario = mapper.DTOToEntity(propietarioCreateDTO);

        try {
            propietarioRepository.save(propietario);
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        return propietarioCreateDTO;
    }

    public PropietarioCreateDTO update(String id, PropietarioCreateDTO propietarioUpdateDTO) {
        Long idL = 0L;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id debe de ser un número correcto");
        }

        validateDTO(propietarioUpdateDTO);

        Propietario propietario = null;

        try {
            propietario = propietarioRepository.findById(idL).orElseThrow(() -> new NotFoundException("No se ha encontrado ningun propietario con esa id"));
        } catch (NotFoundException e) {
            throw new NotFoundException("No se ha encontrado ningun propietario con esa id");
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        propietario.setNombre(propietarioUpdateDTO.getNombre());
        propietario.setApellidos(propietarioUpdateDTO.getApellidos());
        propietario.setTelefono(propietarioUpdateDTO.getTelefono());
        propietario.setGenero(propietarioUpdateDTO.getGenero());
        propietario.setCasado(propietarioUpdateDTO.getCasado());
        propietario.setN_hijos(propietarioUpdateDTO.getN_hijos());

        try {
            propietarioRepository.save(propietario);
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        return propietarioUpdateDTO;
    }

    public void delete(String id) {
        Long idL = 0L;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id debe de ser un número correcto");
        }

        Propietario propietario = null;

        try {
            propietario = propietarioRepository.findById(idL).orElseThrow(() -> new NotFoundException("No se ha encontrado ningun propietario con esa id"));
        } catch (NotFoundException e) {
            throw new NotFoundException("No se ha encontrado ningun propietario con esa id");
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        if (propietario.getUsuario() != null) {
            Usuario usuario = propietario.getUsuario();
            usuario.setPropietario(null);
            usuarioRepository.save(usuario);
        }

        if (propietario.getPropiedades() != null && !propietario.getPropiedades().isEmpty()) {
            throw new DataBaseException("No se puede eliminar un propietario con propiedades asociadas sin asignarlas a otro propietario");
        }

        try {
            propietarioRepository.delete(propietario);
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }
    }
}
