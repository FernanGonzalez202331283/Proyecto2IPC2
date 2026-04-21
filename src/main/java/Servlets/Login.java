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
   protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException{
          
        try {
            BufferedReader reader = req.getReader();
            Gson gson = new Gson();
            Usuario user = gson.fromJson(reader, Usuario.class);

            if (user == null || user.getUsername() == null) {
                resp.setStatus(400);
                resp.getWriter().write("JSON inválido");
                return;
            }

            UsuarioDAO dao = new UsuarioDAO();
            Usuario u = dao.login(user.getUsername(), user.getPassword());

            if (u != null) {

                String token = JWTUtil.generarToken(u.getUsername());

                resp.setContentType("application/json");
                resp.getWriter().write("{\"token\":\"" + token + "\"}");

            } else {
                resp.setStatus(401);
                resp.getWriter().write("Credenciales incorrectas");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("ERROR: " + e.getMessage());
        }
    }
}

