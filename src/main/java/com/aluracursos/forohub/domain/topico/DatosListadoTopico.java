package com.aluracursos.forohub.domain.topico;

import java.time.LocalDateTime;

public record DatosListadoTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fecha,
        String status,
        String autor,
        String curso
) {
    public DatosListadoTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFecha(),
                topico.getStatus().toString(),
                topico.getAutor(),
                topico.getCurso()
        );
    }
}
