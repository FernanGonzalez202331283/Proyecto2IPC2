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
import java.util.List;

/**
 *
 * @author fernan
 */
@WebServlet("/admin/solicitudes-categoria")
public class AdminSolicitudCategoriaServlet extends HttpServlet{
    // LISTAR PENDIENTES
    @Override
    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");

        try {

            SolicitudCategoriaDAO dao =
                new SolicitudCategoriaDAO();

            List<SolicitarCategoria> lista =
                dao.listarPendientes();

            Gson gson = new Gson();

            resp.getWriter().write(
                gson.toJson(lista)
            );

        } catch(Exception e) {

            e.printStackTrace();

            resp.setStatus(500);

            resp.getWriter().write(
                "{\"error\":\"Error servidor\"}"
            );
        }
    }

    // ACEPTAR O RECHAZAR
    @Override
    protected void doPut(
            HttpServletRequest req,
            HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");

        try {

            int id = Integer.parseInt(
                req.getParameter("id")
            );

            String accion =
                req.getParameter("accion");

            SolicitudCategoriaDAO dao =
                new SolicitudCategoriaDAO();

            boolean ok = false;

            if("aceptar".equals(accion)) {

                ok = dao.aceptar(id);

            } else if(
                    "rechazar".equals(accion)) {

                ok = dao.rechazar(id);
            }

            if(ok) {

                resp.getWriter().write(
                    "{\"msg\":\"Operacion exitosa\"}"
                );

            } else {

                resp.setStatus(400);

                resp.getWriter().write(
                    "{\"error\":\"No se pudo realizar\"}"
                );
            }

        } catch(Exception e) {

            e.printStackTrace();

            resp.setStatus(500);

            resp.getWriter().write(
                "{\"error\":\"Error servidor\"}"
            );
        }
    }
}
