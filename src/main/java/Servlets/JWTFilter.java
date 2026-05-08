/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import Servlets.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author fernan
 */
@WebFilter("/*")
public class JWTFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, jakarta.servlet.ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        resp.setHeader(
                "Access-Control-Allow-Origin",
                "http://localhost:4200"
        );
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        resp.setHeader(
                "Access-Control-Allow-Headers",
                "Origin, Content-Type, Accept, Authorization"
        );
        resp.setHeader(
                "Access-Control-Allow-Credentials",
                "true"
        );
        //Preflight
        if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String path = req.getRequestURI();

        //RUTAS LIBRES
        if (path.endsWith("/login")
                || path.endsWith("/registro")) {

            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token requerido\"}");
            return;
        }

        String token = authHeader.substring(7);
        Claims claims = JWTUtil.validarToken(token);

        if (claims == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"error\":\"Token inválido o expirado\"}");
            return;
        }

        //atributos del usuario
        req.setAttribute("userId", ((Number) claims.get("id")).intValue());
        req.setAttribute("username", claims.getSubject());
        req.setAttribute("rol", claims.get("rol"));

        chain.doFilter(request, response);
    }
}
