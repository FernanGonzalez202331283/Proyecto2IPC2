/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.SolicitudCategoriaDAO;
import Modelos.SolicitarCategoria;
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
@WebServlet("/solicitud-categoria")
public class SolicitarCategoriaServlet extends  HttpServlet{
    
    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp
    ) throws IOException {

        resp.setContentType("application/json");

        try {

            Gson gson = new Gson();

            SolicitarCategoria s =
                gson.fromJson(
                    req.getReader(),
                    SolicitarCategoria.class
                );

            SolicitudCategoriaDAO dao =
                new SolicitudCategoriaDAO();

            boolean ok =
                dao.crearSolicitud(s);

            if (ok) {

                resp.getWriter().write(
                    "{\"msg\":\"Solicitud enviada\"}"
                );

            } else {

                resp.setStatus(400);

                resp.getWriter().write(
                    "{\"error\":\"No se pudo guardar\"}"
                );

            }

        } catch (Exception e) {

            e.printStackTrace();

            resp.setStatus(500);

            resp.getWriter().write(
                "{\"error\":\"Error servidor\"}"
            );

        }

    }
}
