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
import java.util.List;

/**
 *
 * @author fernan
 */
@WebServlet("/cliente/propuestas")
public class PropuestasClienteServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        try {
           int userId = Integer.parseInt(req.getParameter("userId"));

            PropuestaDAO dao = new PropuestaDAO();

            List<Propuesta> lista = dao.listarPorCliente(userId);

            Gson gson = new Gson();
            resp.getWriter().write(gson.toJson(lista));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
