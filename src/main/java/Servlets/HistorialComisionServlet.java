/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ConfiguracionComisionDAO;
import Modelos.HistorialComision;
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
@WebServlet("/HistorialComisionServlet")
public class HistorialComisionServlet extends HttpServlet{
    
    private Gson gson =
        new Gson();

    @Override
    protected void doGet(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws IOException {

        // CORS
        resp.setHeader(
            "Access-Control-Allow-Origin",
            "http://localhost:4200"
        );

        resp.setHeader(
            "Access-Control-Allow-Methods",
            "GET, POST, OPTIONS"
        );

        resp.setHeader(
            "Access-Control-Allow-Headers",
            "Origin, Content-Type, Accept, Authorization"
        );

        resp.setContentType(
            "application/json"
        );

        ConfiguracionComisionDAO dao =
            new ConfiguracionComisionDAO();

        List<HistorialComision> lista =
            dao.listarHistorial();

        String json =
            gson.toJson(lista);

        resp.getWriter().write(json);
    }
}
