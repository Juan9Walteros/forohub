package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.usuario.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Usuarios", description = "Gestión de usuarios del foro")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetalleUsuario> registrar(
            @RequestBody @Valid DatosRegistroUsuario datos,
            UriComponentsBuilder uriBuilder) {

        if (usuarioRepository.existsByLogin(datos.login())) {
            throw new IllegalArgumentException("Ya existe un usuario con ese login");
        }

        var claveCifrada = passwordEncoder.encode(datos.clave());
        var usuario = new Usuario(null, datos.login(), claveCifrada);
        usuarioRepository.save(usuario);

        URI url = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuario.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosDetalleUsuario(usuario));
    }

    @GetMapping
    public ResponseEntity<Page<DatosDetalleUsuario>> listar(
            @PageableDefault(size = 10, sort = "login") Pageable paginacion) {
        var page = usuarioRepository.findAll(paginacion).map(DatosDetalleUsuario::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleUsuario> detalle(@PathVariable Long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        return ResponseEntity.ok(new DatosDetalleUsuario(usuario));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosDetalleUsuario> actualizarClave(
            @PathVariable Long id,
            @RequestBody @Valid DatosRegistroUsuario datos) {

        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuario.actualizarClave(passwordEncoder.encode(datos.clave()));
        return ResponseEntity.ok(new DatosDetalleUsuario(usuario));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        var usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        usuarioRepository.delete(usuario);
        return ResponseEntity.noContent().build();
    }
}
