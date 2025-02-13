package com.es.crmInmobiliaria.controller;

import com.es.crmInmobiliaria.dtos.UsuarioDTO;
import com.es.crmInmobiliaria.dtos.UsuarioLoginDTO;
import com.es.crmInmobiliaria.dtos.UsuarioUpdateDTO;
import com.es.crmInmobiliaria.error.exception.BadRequestException;
import com.es.crmInmobiliaria.service.UsuarioService;
import com.es.crmInmobiliaria.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TokenService tokenService;

    @GetMapping("/")
    public ResponseEntity<List<UsuarioDTO>> getAll() {
        return new ResponseEntity<>(usuarioService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> findById(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("id no válida");
        }

        return new ResponseEntity<>(usuarioService.findById(id), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UsuarioLoginDTO usuarioLoginDTO) {
        if (usuarioLoginDTO == null) {
            throw new BadRequestException("El body no puede ser null");
        }

        if (usuarioLoginDTO.getUsername() == null || usuarioLoginDTO.getPassword() == null) {
            throw new BadRequestException("El body no puede ser nulo");
        }

        if (usuarioLoginDTO.getUsername().isBlank() || usuarioLoginDTO.getPassword().isBlank()) {
            throw new BadRequestException("El usuario o la contraseña no puede ser vacío");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuarioLoginDTO.getUsername(), usuarioLoginDTO.getPassword())
        );

        return new ResponseEntity<>(tokenService.generateToken(authentication), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<UsuarioLoginDTO> register(@RequestBody UsuarioLoginDTO usuarioRegisterDTO) {
        return new ResponseEntity<>(usuarioService.registerUser(usuarioRegisterDTO), HttpStatus.OK);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UsuarioLoginDTO> updateUser(@PathVariable String username, @RequestBody UsuarioLoginDTO usuarioDTO) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username no válido");
        }

        if (usuarioDTO == null) {
            throw new BadRequestException("El body no puede ser null");
        }

        return new ResponseEntity<>(usuarioService.updateUser(username, usuarioDTO), HttpStatus.OK);
    }

    @PutMapping("/internal/{username}")
    public ResponseEntity<UsuarioUpdateDTO> updateInternal(@PathVariable String username, @RequestBody UsuarioUpdateDTO usuarioDTO) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username no válido");
        }

        if (usuarioDTO == null) {
            throw new BadRequestException("El body no puede ser null");
        }

        return new ResponseEntity<>(usuarioService.updateInternalUser(username, usuarioDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        if (username == null || username.isBlank()) {
            throw new BadRequestException("Username no válido");
        }

        usuarioService.deleteUser(username);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}