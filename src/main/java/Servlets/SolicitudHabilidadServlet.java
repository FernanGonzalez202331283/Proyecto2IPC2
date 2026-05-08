/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.SolicitudHabilidadDAO;
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
@WebServlet("/freelancer/solicitud-habilidad")
public class SolicitudHabilidadServlet extends HttpServlet {
   
    @Override
    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");

        try {

            Gson gson = new Gson();

            Map<String, Object> data =
                gson.fromJson(req.getReader(), Map.class);

            String nombre =
                data.get("nombre").toString();

            String descripcion =
                data.get("descripcion").toString();

            int categoriaId =
                Integer.parseInt(
                    data.get("categoriaId").toString()
                );

            int usuarioId =
                (int) req.getAttribute("userId");

            SolicitudHabilidadDAO dao =
                new SolicitudHabilidadDAO();

            boolean ok = dao.crearSolicitud(
                nombre,
                descripcion,
                categoriaId,
                usuarioId
            );

            if (ok) {

                resp.getWriter()
                    .write("{\"msg\":\"Solicitud enviada\"}");

            } else {

                resp.setStatus(400);

                resp.getWriter()
                    .write("{\"error\":\"No se pudo enviar\"}");
            }

        } catch (Exception e) {

            e.printStackTrace();

            resp.setStatus(500);

            resp.getWriter()
                .write("{\"error\":\"Error servidor\"}");
        }
    }
}
