/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ClienteDAO;
import DAO.ContratoDAO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 *
 * @author fernan
 */
@WebServlet("/cliente/dashboard")
public class ClienteServlet extends HttpServlet{
   
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        try {
            int userId = (int) req.getAttribute("userId");

            ClienteDAO dao = new ClienteDAO();

            Map<String, Object> data = dao.obtenerDashboard(userId);
            
            int propuestas = dao.contarPropuestas(userId);
            data.put("propuestas", propuestas);
            int clienteId = dao.obtenerIdPorUsuario(userId);

            ContratoDAO contratoDAO = new ContratoDAO();
            int activos = contratoDAO.contarActivosPorCliente(clienteId);

            data.put("activos", activos);
            Gson gson = new Gson();
            resp.getWriter().write(gson.toJson(data));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
