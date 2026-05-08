/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ConfiguracionComisionDAO;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author fernan
 */
@WebServlet("/ConfiguracionComisionServlet")
public class ConfiguracionComisionServlet extends HttpServlet {

    private Gson gson = new Gson();

    private void configurarCors(
            HttpServletResponse response
    ) {

        response.setHeader(
                "Access-Control-Allow-Origin",
                "http://localhost:4200"
        );

        response.setHeader(
                "Access-Control-Allow-Methods",
                "GET, POST, OPTIONS"
        );

        response.setHeader(
                "Access-Control-Allow-Headers",
                "Origin, Content-Type, Accept, Authorization"
        );
    }

    @Override
    protected void doOptions(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        configurarCors(response);

        response.setStatus(
                HttpServletResponse.SC_OK
        );
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        configurarCors(response);

        response.setContentType(
                "application/json"
        );

        ConfiguracionComisionDAO dao
                = new ConfiguracionComisionDAO();

        double porcentaje
                = dao.obtenerPorcentajeActual();

        JsonObject json
                = new JsonObject();

        json.addProperty(
                "porcentajeActual",
                porcentaje
        );

        response.getWriter().write(
                gson.toJson(json)
        );
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        configurarCors(response);

        response.setContentType(
                "application/json"
        );

        JsonObject body
                = gson.fromJson(
                        request.getReader(),
                        JsonObject.class
                );

        double nuevoPorcentaje
                = body.get("nuevoPorcentaje")
                        .getAsDouble();

        ConfiguracionComisionDAO dao
                = new ConfiguracionComisionDAO();

        dao.actualizarPorcentaje(
                nuevoPorcentaje
        );

        JsonObject json
                = new JsonObject();

        json.addProperty(
                "mensaje",
                "Comisión actualizada correctamente"
        );

        response.getWriter().write(
                gson.toJson(json)
        );
    }
}
