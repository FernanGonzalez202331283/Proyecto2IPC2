/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.PropuestaDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
@WebServlet("/propuesta/confirmar")
public class ConfirmarPropuestaServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        try {
            BufferedReader reader = req.getReader();
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(reader, JsonObject.class);

            int id = json.get("id").getAsInt();

            PropuestaDAO dao = new PropuestaDAO();
            dao.confirmarPropuesta(id);

            JsonObject res = new JsonObject();
            res.addProperty("msg", "Contrato iniciado");

            resp.getWriter().write(res.toString());

        } catch (Exception e) {

            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
