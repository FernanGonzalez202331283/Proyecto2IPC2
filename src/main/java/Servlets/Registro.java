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
import java.io.IOException;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author fernan
 */
@WebServlet("/registro")
public class Registro extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Gson gson = new Gson();
            Usuario user = gson.fromJson(req.getReader(), Usuario.class);

            //VALIDACIÓN
            if (isInvalid(user)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"error\":\"Todos los campos son obligatorios\"}");
                return;
            }

            //ESTADO INICIAL
            user.setPerfilCompleto(0);
            user.setEstado(1);
            UsuarioDAO dao = new UsuarioDAO();

            if (dao.existeUsuario(user.getUsername())) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.getWriter().write("{\"error\":\"El nombre de usuario ya está en uso\"}");
                return;
            }

            boolean ok = dao.registrar(user);

            if (ok) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("{\"msg\":\"Registro exitoso\"}");
            } else {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"error\":\"Error al guardar\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"Error en datos\"}");
        }
    }

    private boolean isInvalid(Usuario u) {
        return u == null || u.getNombre() == null || u.getUsername() == null ||
               u.getPassword() == null || u.getCorreo() == null ||
               u.getCui() == null || u.getRol() == null ||
               u.getFechaNacimiento() == null;
    }
}
