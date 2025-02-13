package com.es.crmInmobiliaria.service;

import com.es.crmInmobiliaria.dtos.UsuarioDTO;
import com.es.crmInmobiliaria.dtos.UsuarioLoginDTO;
import com.es.crmInmobiliaria.dtos.UsuarioUpdateDTO;
import com.es.crmInmobiliaria.error.exception.BadRequestException;
import com.es.crmInmobiliaria.error.exception.DataBaseException;
import com.es.crmInmobiliaria.error.exception.NotFoundException;
import com.es.crmInmobiliaria.model.Propietario;
import com.es.crmInmobiliaria.model.Usuario;
import com.es.crmInmobiliaria.repository.PropietarioRepository;
import com.es.crmInmobiliaria.repository.PropiedadRepository;
import com.es.crmInmobiliaria.repository.UsuarioRepository;
import com.es.crmInmobiliaria.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PropietarioRepository propietarioRepository;
    @Autowired
    private PropiedadRepository propiedadRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Mapper mapper;

    public void validateLoginDTO(UsuarioLoginDTO usuarioLoginDTO) {
        if (usuarioLoginDTO == null) {
            throw new BadRequestException("El body no puede ser null");
        }

        if (usuarioLoginDTO.getUsername() == null || usuarioLoginDTO.getPassword() == null) {
            throw new BadRequestException("El body no puede ser nulo");
        }

        if (usuarioLoginDTO.getUsername().isEmpty() || usuarioLoginDTO.getPassword().isEmpty()) {
            throw new BadRequestException("El usuario o la contraseña no puede ser vacío");
        }
    }

    public void validateUsuarioUpdateDTO(UsuarioUpdateDTO usuarioDTO) {
        if (usuarioDTO.getUsername() == null || usuarioDTO.getUsername().isBlank()) {
            throw new BadRequestException("El campo username no puede ser null");
        }

        if (usuarioDTO.getPassword() == null || usuarioDTO.getPassword().isBlank()) {
            throw new BadRequestException("El campo password no puede ser null");
        }

        if (usuarioDTO.getRol() == null || usuarioDTO.getRol().isBlank()) {
            throw new BadRequestException("El campo rol no puede ser null");
        }

        if (!usuarioDTO.getRol().equalsIgnoreCase("USER") && !usuarioDTO.getRol().equalsIgnoreCase("ADMIN")) {
            throw new BadRequestException("El rol solo puede ser USER o ADMIN");
        }
    }

    public List<UsuarioDTO> getAll() {
        List<Usuario> usuarios;
        List<UsuarioDTO> usuarioDTOS = new ArrayList<>();

        try {
            usuarios = usuarioRepository.findAll();
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        usuarios.forEach(usuario -> {
            UsuarioDTO usuarioDTO = mapper.entityToDTO(usuario);
            usuarioDTOS.add(usuarioDTO);
        });

        return usuarioDTOS;
    }

    public UsuarioDTO findById(String id) {
        Long idL = 0L;

        try {
            idL = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("La id debe de ser un número correcto");
        }

        Usuario usuario = null;

        try {
            usuario = usuarioRepository.findById(idL).orElseThrow(() -> new NotFoundException("No existe ningún usuario con esa id"));
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
        }

        return mapper.entityToDTO(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository
                .findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario No encontrado"));


        UserDetails userDetails = User
                .builder()
                .username(usuario.getUsername())
                .password(usuario.getPassword())
                .roles(usuario.getRol().split(","))
                .build();

        return userDetails;
    }


    public UsuarioLoginDTO registerUser(UsuarioLoginDTO usuarioRegisterDTO) {
        validateLoginDTO(usuarioRegisterDTO);

        if (usuarioRepository.findByUsername(usuarioRegisterDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }

        Usuario newUsuario = new Usuario();

        //Rol inicial por defecto es USER, el rol solo lo podra cambiar un admin mediante el PUT
        newUsuario.setPassword(passwordEncoder.encode(usuarioRegisterDTO.getPassword()));
        newUsuario.setUsername(usuarioRegisterDTO.getUsername());
        newUsuario.setFecha_registro(LocalDate.now());
        newUsuario.setRol("USER");

        usuarioRepository.save(newUsuario);

        return usuarioRegisterDTO;
    }

    public UsuarioLoginDTO updateUser(String username, UsuarioLoginDTO usuarioDTO) {
        validateLoginDTO(usuarioDTO);

        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuario no encontrado para actualizar"));

        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));

        try {
            usuarioRepository.save(usuario);
        } catch (Exception e) {
            throw new DataBaseException("Error al actualizar el usuario: " + e.getMessage());
        }

        return usuarioDTO;
    }

    public UsuarioUpdateDTO updateInternalUser(String username, UsuarioUpdateDTO usuarioDTO) {
        validateUsuarioUpdateDTO(usuarioDTO);

        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuario no encontrado para actualizar"));

        usuario.setUsername(usuarioDTO.getUsername());
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setRol(usuarioDTO.getRol());

        if (usuarioDTO.getId_propietario() != null && !usuarioDTO.getId_propietario().isBlank()) {
            Long idL = 0L;

            try {
                idL = Long.parseLong(usuarioDTO.getId_propietario());
            } catch (NumberFormatException e) {
                throw new NumberFormatException("La id del propietario debe de ser null o ser un número");
            }

            Propietario propietario = null;

            try {
                propietario = propietarioRepository.findById(idL).orElseThrow(() -> new NotFoundException("No existe ningún propietario con esa id"));
            } catch (NotFoundException e) {
                throw new NotFoundException(e.getMessage());
            } catch (Exception e) {
                throw new DataBaseException("error inesperado en la base de datos. " + e.getMessage());
            }

            usuario.setPropietario(propietario);
        }

        try {
            usuarioRepository.save(usuario);
        } catch (Exception e) {
            throw new DataBaseException("Error al actualizar el usuario: " + e.getMessage());
        }

        return usuarioDTO;
    }

    public void deleteUser(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Usuario no encontrado para eliminar"));

        if (usuario.getPropiedades() != null && !usuario.getPropiedades().isEmpty()) {
            throw new DataBaseException("No se puede eliminar un vendedor con propiedades asociadas sin asignarlas a otro vendedor");
        }

        if (usuario.getPropietario() != null) {
            Propietario propietario = usuario.getPropietario();
            propietario.setUsuario(null);
            propietarioRepository.save(propietario);
        }

        try {
            usuarioRepository.delete(usuario);
        } catch (Exception e) {
            throw new DataBaseException("Error al eliminar el usuario: " + e.getMessage());
        }
    }
}