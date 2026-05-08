/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ReporteDAO;
import Modelos.ReporteRecarga;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
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
@WebServlet("/ReporteRecargasClienteServlet")
public class ReporteRecargasClienteServlet extends HttpServlet{
     @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {

            int usuarioId =
                (int) request.getAttribute("userId");

            String fechaInicio =
                request.getParameter("fechaInicio");

            String fechaFin =
                request.getParameter("fechaFin");

            ReporteDAO dao =
                new ReporteDAO();

            List<ReporteRecarga> lista =
                dao.obtenerHistorialRecargas(
                    usuarioId,
                    fechaInicio,
                    fechaFin
                );

            Gson gson = new Gson();

            response.getWriter().write(
                gson.toJson(lista)
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.setStatus(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );

            response.getWriter().write("""
                {
                    "error":"Error al generar reporte"
                }
            """);
        }
    }
}
