/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.PlataformaDAO;
import Modelos.ModeloComision;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fernan
 */
@WebServlet("/admin/saldos")
public class AdminSaldoServlet extends HttpServlet{
    
    @Override
    protected void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {

        response.setContentType("application/json");

        PlataformaDAO dao =
            new PlataformaDAO();

        double saldo =
            dao.obtenerSaldoPlataforma();

        List<ModeloComision> lista =
            dao.listarComisiones();

        Map<String, Object> data =
            new HashMap<>();

        data.put("saldo", saldo);
        data.put("comisiones", lista);

        Gson gson = new Gson();

        response.getWriter().write(
            gson.toJson(data)
        );
    }
}
