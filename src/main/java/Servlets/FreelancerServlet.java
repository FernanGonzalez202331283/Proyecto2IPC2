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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fernan
 */
@WebServlet("/freelancer/dashboard")
public class FreelancerServlet extends HttpServlet{
   
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        try {
            int userId = (int) req.getAttribute("userId");

            FreelancerDAO dao = new FreelancerDAO();

            double saldo = dao.obtenerSaldo(userId);

            // luego conectamos real
            int propuestas = 0;
            int contratos = 0;

            Map<String, Object> data = new HashMap<>();
            data.put("saldo", saldo);
            data.put("propuestas", propuestas);
            data.put("contratos", contratos);

            Gson gson = new Gson();
            resp.getWriter().write(gson.toJson(data));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
