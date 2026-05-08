/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;
import DAO.UsuarioDAO;
import Modelos.Usuario;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
/**
 *
 * @author fernan
 */

@WebServlet("/login")
public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        Gson gson = new Gson();

        try {
            Usuario userCreds = gson.fromJson(req.getReader(), Usuario.class);

            if (userCreds == null || userCreds.getUsername() == null || userCreds.getPassword() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Datos inválidos\"}");
                return;
            }

            UsuarioDAO dao = new UsuarioDAO();
            // Recuerda que dentro de dao.login() debes usar BCrypt.checkpw
            Usuario u = dao.login(userCreds.getUsername(), userCreds.getPassword());

            if (u != null && u.getEstado() == 1) {
                // GENERAR TOKEN
                String token = JWTUtil.generarToken(u.getId(), u.getUsername(), u.getRol());
                java.util.Map<String, Object> responseData = new java.util.HashMap<>();
                responseData.put("token", token);
                responseData.put("id", u.getId());
                responseData.put("username", u.getUsername());
                responseData.put("rol", u.getRol());
                responseData.put("perfilCompleto", u.getPerfilCompleto()); // <--- CRÍTICO PARA ANGULAR

                resp.getWriter().write(gson.toJson(responseData));

            } else {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.getWriter().write("{\"error\":\"Credenciales incorrectas o usuario inactivo\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error en el servidor\"}");
        }
    }
}

