/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ClienteDAO;
import DAO.ProyectoDAO;
import Modelos.Proyecto;
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
@WebServlet("/proyectos")
public class ProyectoServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Gson gson = new Gson();
            Proyecto p = gson.fromJson(req.getReader(), Proyecto.class);

            int userId = (int) req.getAttribute("userId");

            ProyectoDAO dao = new ProyectoDAO();

            //obtener clienteId correctamente
            int clienteId = dao.obtenerClienteIdPorUsuario(userId);

            if (clienteId == 0) {
                resp.setStatus(400);
                resp.getWriter().write("{\"error\":\"Cliente no encontrado\"}");
                return;
            }

            boolean ok = dao.crearProyecto(clienteId, p);

            if (ok) {
                resp.getWriter().write("{\"msg\":\"Proyecto creado\"}");
            } else {
                resp.setStatus(500);
                resp.getWriter().write("{\"error\":\"Error al crear\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"Error servidor\"}");
        }
    }

    @Override
protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.setContentType("application/json");

    try {
        ProyectoDAO dao = new ProyectoDAO();
        Gson gson = new Gson();

        String idParam = req.getParameter("id");

        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            Proyecto p = dao.obtenerPorId(id);

            resp.getWriter().write(gson.toJson(p));
            return;
        }
        int userId = (int) req.getAttribute("userId");
        String rol = (String) req.getAttribute("rol");

        List<Proyecto> lista;

        if ("CLIENTE".equals(rol)) {
            int clienteId = dao.obtenerClienteIdPorUsuario(userId);
            lista = dao.listarProyectosPorCliente(clienteId);
        } else if ("FREELANCER".equals(rol)) {
            lista = dao.listarAbiertos();
        } else {
            lista = new java.util.ArrayList<>();
        }

        resp.getWriter().write(gson.toJson(lista));

    } catch (Exception e) {
        e.printStackTrace();
        resp.setStatus(500);
    }
}
}