/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ClienteDAO;
import Modelos.Cliente;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author fernan
 */
@WebServlet("/completarPerfil")
public class CompletarClienteServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        try {
            Gson gson = new Gson();
            Cliente c = gson.fromJson(req.getReader(), Cliente.class);

            int userId = (int) req.getAttribute("userId"); // viene del JWT

            ClienteDAO dao = new ClienteDAO();

            boolean ok = dao.crearCliente(userId, c);

            if (ok) {
                resp.getWriter().write("{\"msg\":\"Perfil completado\"}");
            } else {
                resp.setStatus(500);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
