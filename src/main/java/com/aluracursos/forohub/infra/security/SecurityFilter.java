package com.aluracursos.forohub.infra.security;

import com.aluracursos.forohub.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Obtener el token del header
        var authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                var token = authHeader.replace("Bearer ", "");
                var subject = tokenService.getSubject(token); // Extrae el username
                
                if (subject != null) {
                    // Token válido
                    var usuario = usuarioRepository.findByLogin(subject);
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null,
                            usuario.getAuthorities()); // Forzamos un inicio de sesión
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Token inválido, continuar sin autenticar
            }
        }

        filterChain.doFilter(request, response);
    }
}
