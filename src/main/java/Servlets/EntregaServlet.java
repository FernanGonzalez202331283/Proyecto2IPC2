/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.EntregaDAO;
import Modelos.Entrega;
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
@WebServlet("/entregas")
public class EntregaServlet extends HttpServlet {
  
    //LISTAR ENTREGAS
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        int contratoId = Integer.parseInt(req.getParameter("contratoId"));

        EntregaDAO dao = new EntregaDAO();
        List<Entrega> lista = dao.listarPorContrato(contratoId);

        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(lista));
    }

    //SUBIR ENTREGA
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Gson gson = new Gson();
        Entrega data = gson.fromJson(req.getReader(), Entrega.class);

        EntregaDAO dao = new EntregaDAO();

        boolean ok = dao.crearEntrega(
            data.getContratoId(),
            data.getDescripcion(),
            data.getArchivo()
        );

        resp.setContentType("application/json");

        if (ok) {
            resp.getWriter().write("{\"msg\":\"Entrega subida correctamente\"}");
        } else {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"No se pudo subir la entrega\"}");
        }
    }
}
