/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.CalificacionDAO;
import Modelos.Calificacion;
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
@WebServlet("/cliente/calificacion")
public class CalificacionServlet extends HttpServlet{
    
    @Override
    protected void doPost(HttpServletRequest req,
            HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");

        try {

            Gson gson = new Gson();

            Calificacion c = gson.fromJson(
                    req.getReader(),
                    Calificacion.class
            );

            //VALIDACIONES

            if (c.getEstrellas() < 1 || c.getEstrellas() > 5) {

                resp.setStatus(400);

                resp.getWriter().write(
                        "{\"error\":\"Las estrellas deben ser de 1 a 5\"}"
                );

                return;
            }

            CalificacionDAO dao = new CalificacionDAO();
                int freelancerId =
                    dao.obtenerFreelancerPorContrato(
                        c.getContratoId()
                    );

                c.setFreelancerId(freelancerId);
            boolean ok = dao.guardarCalificacion(c);

            if (ok) {

                resp.getWriter().write(
                        "{\"msg\":\"Calificación guardada\"}"
                );

            } else {

                resp.setStatus(500);

                resp.getWriter().write(
                        "{\"error\":\"No se pudo guardar la calificación\"}"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            resp.setStatus(500);

            resp.getWriter().write(
                    "{\"error\":\"Error del servidor\"}"
            );
        }
    }
}
