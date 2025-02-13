package com.es.crmInmobiliaria.controller;

import com.es.crmInmobiliaria.dtos.PropiedadCreateDTO;
import com.es.crmInmobiliaria.dtos.PropiedadDTO;
import com.es.crmInmobiliaria.dtos.PropiedadUpdateDTO;
import com.es.crmInmobiliaria.error.exception.BadRequestException;
import com.es.crmInmobiliaria.error.exception.NotFoundException;
import com.es.crmInmobiliaria.service.PropiedadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/propiedades")
public class PropiedadController {
    @Autowired
    private PropiedadService propiedadService;

    @GetMapping("/")
    public ResponseEntity<List<PropiedadDTO>> getAll() {
        return new ResponseEntity<>(propiedadService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PropiedadDTO> findById(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("id no válida");
        }

        PropiedadDTO propiedadDTO = propiedadService.findById(id);

        if (propiedadDTO == null) {
            throw new NotFoundException("usuario no encontrado");
        }

        return new ResponseEntity<>(propiedadDTO, HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<PropiedadCreateDTO> create(@RequestBody PropiedadCreateDTO propiedadDTO, Principal principal) {
        if (propiedadDTO == null) {
            throw new BadRequestException("El body no puede ser null");
        }

        PropiedadCreateDTO propiedadDTOCreated = propiedadService.create(propiedadDTO, principal);

        return new ResponseEntity<>(propiedadDTOCreated, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PropiedadUpdateDTO> update(@PathVariable String id, @RequestBody PropiedadUpdateDTO propiedadDTO) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("id no válida");
        }

        if (propiedadDTO == null) {
            throw new BadRequestException("El body no puede ser null");
        }

        PropiedadUpdateDTO propiedadDTOUpdated = propiedadService.update(id, propiedadDTO);

        return new ResponseEntity<>(propiedadDTOUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (id == null || id.isBlank()) {
            throw new BadRequestException("id no válida");
        }

        propiedadService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
