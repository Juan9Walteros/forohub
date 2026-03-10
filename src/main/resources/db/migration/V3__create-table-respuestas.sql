CREATE TABLE respuestas (
    id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    mensaje VARCHAR(2000) NOT NULL,
    topico_id BIGINT NOT NULL,
    autor_id BIGINT NOT NULL,
    fecha TIMESTAMP NOT NULL,
    solucion BOOLEAN NOT NULL DEFAULT FALSE,
    PRIMARY KEY (id),
    CONSTRAINT fk_respuestas_topico FOREIGN KEY (topico_id) REFERENCES topicos(id),
    CONSTRAINT fk_respuestas_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id)
);
