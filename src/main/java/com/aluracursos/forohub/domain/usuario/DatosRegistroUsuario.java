package com.aluracursos.forohub.domain.usuario;

import jakarta.validation.constraints.NotBlank;

public record DatosRegistroUsuario(
        @NotBlank String login,
        @NotBlank String clave) {
}
