/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.PropuestaDAO;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author fernan
 */
@WebServlet("/freelancer/propuesta/retirar")
public class RetirarPropuestaServlet extends HttpServlet {
     @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        try {
            int userId = (int) req.getAttribute("userId");
            int propuestaId = Integer.parseInt(req.getParameter("id"));

            PropuestaDAO dao = new PropuestaDAO();
            int freelancerId = dao.obtenerFreelancerId(userId);

            dao.retirarPropuesta(propuestaId, freelancerId);

            resp.getWriter().write("{\"msg\":\"Propuesta retirada\"}");

        } catch (RuntimeException e) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");

        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Error del servidor\"}");
        }
    }
}
