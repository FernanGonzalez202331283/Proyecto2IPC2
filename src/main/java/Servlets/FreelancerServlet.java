/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.FreelancerDAO;
import Modelos.Freelancer;
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
@WebServlet("/freelancer/dashboard")
public class FreelancerServlet extends HttpServlet{
   
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String rol = (String) req.getAttribute("rol");

            if (!"FREELANCER".equals(rol)) {
                resp.setStatus(403);
                resp.getWriter().write("{\"error\":\"No autorizado\"}");
                return;
            }

            Gson gson = new Gson();
            Freelancer f = gson.fromJson(req.getReader(), Freelancer.class);

            int userId = (int) req.getAttribute("userId");

            FreelancerDAO dao = new FreelancerDAO();

            boolean ok = dao.completarPerfil(userId, f);

            if (ok) {
                resp.getWriter().write("{\"msg\":\"Perfil completado\"}");
            } else {
                resp.setStatus(500);
                resp.getWriter().write("{\"error\":\"Error al completar perfil\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
