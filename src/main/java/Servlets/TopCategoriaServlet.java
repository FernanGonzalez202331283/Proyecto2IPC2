/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ReporteDAO;
import Modelos.TopCategoriaReporte;
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
@WebServlet("/ReporteTopCategoriasServlet")
public class TopCategoriaServlet extends HttpServlet{
    
    private Gson gson =
        new Gson();

    @Override
    protected void doGet(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws IOException {

        resp.setHeader(
            "Access-Control-Allow-Origin",
            "http://localhost:4200"
        );

        resp.setContentType(
            "application/json"
        );

        String fechaInicio =
            req.getParameter(
                "fechaInicio"
            );

        String fechaFin =
            req.getParameter(
                "fechaFin"
            );

        ReporteDAO dao =
            new ReporteDAO();

        List<TopCategoriaReporte> lista =
            dao.topCategorias(
                fechaInicio,
                fechaFin
            );

        String json =
            gson.toJson(lista);

        resp.getWriter().write(json);
    }
}
