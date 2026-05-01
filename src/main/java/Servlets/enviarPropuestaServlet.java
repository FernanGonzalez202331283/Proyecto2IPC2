/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.PropuestaDAO;
import Modelos.Propuesta;
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
@WebServlet("/freelancer/propuesta")
public class enviarPropuestaServlet extends HttpServlet{
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        try {
            int userId = (int) req.getAttribute("userId");

            Gson gson = new Gson();
            Propuesta p = gson.fromJson(req.getReader(), Propuesta.class);

            PropuestaDAO dao = new PropuestaDAO();

            int freelancerId = dao.obtenerFreelancerId(userId);

            boolean ok = dao.enviarPropuesta(freelancerId, p);

            if (ok) {
                resp.getWriter().write("{\"msg\":\"Propuesta enviada\"}");
            } else {
                resp.setStatus(500);
            }

        } catch (Exception e) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
