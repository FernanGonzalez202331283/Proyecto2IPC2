/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ClienteDAO;
import DAO.ContratoDAO;
import Modelos.ContratoDetalle;
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
@WebServlet("/cliente/contratos")
public class ClinteContratosServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        int userId = (int) req.getAttribute("userId");

        //obtener cliente_id
        ClienteDAO cdao = new ClienteDAO();
        int clienteId = cdao.obtenerIdPorUsuario(userId);

        ContratoDAO dao = new ContratoDAO();
        List<ContratoDetalle> lista = dao.listarActivosPorCliente(clienteId);
        System.out.println("Contratos encontrados: " + lista.size());
        Gson gson = new Gson();
        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(lista));
    }
}
