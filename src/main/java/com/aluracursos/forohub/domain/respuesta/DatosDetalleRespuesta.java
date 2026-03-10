package com.aluracursos.forohub.domain.respuesta;

import java.time.LocalDateTime;

public record DatosDetalleRespuesta(
        Long id,
        String mensaje,
        Long topicoId,
        String autorLogin,
        LocalDateTime fecha,
        Boolean solucion) {

    public DatosDetalleRespuesta(Respuesta respuesta) {
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getTopico().getId(),
                respuesta.getAutor().getLogin(),
                respuesta.getFecha(),
                respuesta.getSolucion());
    }
}
