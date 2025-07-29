package br.edu.ifg.trilhadeaprendizadoapims.auth.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayAccessFilter extends OncePerRequestFilter {

    @Value("${gateway.secret}")
    private String gatewaySecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String gatewayKey = request.getHeader("X-Gateway-Key");

        if (!gatewaySecret.equals(gatewayKey)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Acesso permitido apenas pelo gateway.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
