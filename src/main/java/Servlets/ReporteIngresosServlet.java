/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ReporteDAO;
import Modelos.ReporteIngresos;
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
@WebServlet("/ReporteIngresosServlet")
public class ReporteIngresosServlet extends HttpServlet{
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");

        String inicio = req.getParameter("fechaInicio");
        String fin = req.getParameter("fechaFin");

        ReporteDAO dao = new ReporteDAO();

        ReporteIngresos data = dao.reporteIngresos(inicio, fin);

        resp.getWriter().write(gson.toJson(data));
    }
}
