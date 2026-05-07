/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ContratoDAO;
import DAO.EntregaDAO;
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
@WebServlet("/entregas/revisar")
public class EntregaRevisionServlet extends HttpServlet {
    
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        Gson gson = new Gson();
        Map<String, Object> data = gson.fromJson(req.getReader(), Map.class);

        String accion = (String) data.get("accion");

        if (accion == null) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Acción requerida\"}");
            return;
        }

        Number contratoNum = (Number) data.get("contratoId");

        if (contratoNum == null) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"Contrato requerido\"}");
            return;
        }

        int contratoId = contratoNum.intValue();

        EntregaDAO edao = new EntregaDAO();
        ContratoDAO cdao = new ContratoDAO();

        boolean ok = false;

        switch (accion) {

            case "APROBAR":
                ok = edao.aprobarEntrega(contratoId);
                break;

            case "RECHAZAR":
                String motivo = (String) data.get("motivo");

                if (motivo == null || motivo.isEmpty()) {
                    resp.setStatus(400);
                    resp.getWriter().write("{\"error\":\"Motivo requerido\"}");
                    return;
                }

                ok = edao.rechazarEntrega(contratoId, motivo);
                break;

            case "CANCELAR":
                String motivoCancel = (String) data.get("motivo");

                if (motivoCancel == null || motivoCancel.isEmpty()) {
                    resp.setStatus(400);
                    resp.getWriter().write("{\"error\":\"Motivo requerido\"}");
                    return;
                }

                ok = cdao.cancelarContrato(contratoId, motivoCancel);
                break;
        }

        resp.setContentType("application/json");

        if (ok) {
            resp.getWriter().write("{\"msg\":\"Operación realizada\"}");
        } else {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"No se pudo procesar\"}");
        }
    }
}
