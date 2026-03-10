package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.respuesta.*;
import com.aluracursos.forohub.domain.topico.TopicoRepository;
import com.aluracursos.forohub.domain.usuario.UsuarioRepository;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
@Tag(name = "Respuestas", description = "CRUD de respuestas a tópicos")
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetalleRespuesta> crear(
            @RequestBody @Valid DatosRegistroRespuesta datos,
            UriComponentsBuilder uriBuilder) {

        var topico = topicoRepository.findById(datos.topicoId())
                .orElseThrow(() -> new EntityNotFoundException("Tópico no encontrado"));

        var autor = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        var respuesta = new Respuesta(datos, topico, autor);
        respuestaRepository.save(respuesta);

        URI url = uriBuilder.path("/respuestas/{id}").buildAndExpand(respuesta.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosDetalleRespuesta(respuesta));
    }

    @GetMapping
    public ResponseEntity<Page<DatosDetalleRespuesta>> listar(
            @RequestParam(required = false) Long topicoId,
            @PageableDefault(size = 10, sort = "fecha") Pageable paginacion) {

        Page<Respuesta> page;
        if (topicoId != null) {
            page = respuestaRepository.findByTopicoId(topicoId, paginacion);
        } else {
            page = respuestaRepository.findAll(paginacion);
        }
        return ResponseEntity.ok(page.map(DatosDetalleRespuesta::new));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleRespuesta> detalle(@PathVariable Long id) {
        var respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada"));
        return ResponseEntity.ok(new DatosDetalleRespuesta(respuesta));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosDetalleRespuesta> actualizar(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarRespuesta datos) {

        var respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada"));

        respuesta.actualizar(datos);
        return ResponseEntity.ok(new DatosDetalleRespuesta(respuesta));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        var respuesta = respuestaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Respuesta no encontrada"));

        respuestaRepository.delete(respuesta);
        return ResponseEntity.noContent().build();
    }
}
