/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ClienteDAO;
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
@WebServlet("/cliente/recargar")
public class RecargaSaldoServlet extends HttpServlet {
     @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        try {
            Gson gson = new Gson();

            //recibimos monto
            java.util.Map<String, Double> data = gson.fromJson(req.getReader(), java.util.Map.class);
            double monto = data.get("monto");

            int userId = (int) req.getAttribute("userId");

            ClienteDAO dao = new ClienteDAO();
            boolean ok = dao.recargarSaldo(userId, monto);

            if (ok) {
                resp.getWriter().write("{\"msg\":\"Recarga exitosa\"}");
            } else {
                resp.setStatus(500);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
