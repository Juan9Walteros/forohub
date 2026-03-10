package com.aluracursos.forohub.controller;

import com.aluracursos.forohub.domain.usuario.DatosAutenticacion;
import com.aluracursos.forohub.domain.usuario.Usuario;
import com.aluracursos.forohub.infra.security.TokenService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticación", description = "Endpoint de autenticación para obtener el token JWT")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DatosJWTToken> autenticar(@RequestBody @Valid DatosAutenticacion datos) {
        System.out.println("=== CONTROLLER LOGIN LLAMADO ===");
        System.out.println("Login: " + datos.login());
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                datos.login(), datos.clave());
        var usuarioAutenticado = authenticationManager.authenticate(authToken);
        var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
    }

    private record DatosJWTToken(String token) {
    }
}
