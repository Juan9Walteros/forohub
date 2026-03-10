package com.aluracursos.forohub.domain.respuesta;

import com.aluracursos.forohub.domain.topico.Topico;
import com.aluracursos.forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "respuestas")
@Entity(name = "Respuesta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensaje;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    private LocalDateTime fecha;
    private Boolean solucion;

    public Respuesta(DatosRegistroRespuesta datos, Topico topico, Usuario autor) {
        this.mensaje = datos.mensaje();
        this.topico = topico;
        this.autor = autor;
        this.fecha = LocalDateTime.now();
        this.solucion = false;
    }

    public void actualizar(DatosActualizarRespuesta datos) {
        if (datos.mensaje() != null) {
            this.mensaje = datos.mensaje();
        }
        if (datos.solucion() != null) {
            this.solucion = datos.solucion();
        }
    }
}
