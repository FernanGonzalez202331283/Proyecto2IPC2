/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.ClienteDAO;
import DAO.ContratoDAO;
import DAO.PropuestaDAO;
import DAO.ProyectoDAO;
import DAO.SaldoDAO;
import Modelos.Propuesta;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 *
 * @author fernan
 */
@WebServlet("/AceptarPropuesta")
public class AceptarPropuesta extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

         try {
            BufferedReader reader = req.getReader();
            Gson gson = new Gson();

            JsonObject json = gson.fromJson(reader, JsonObject.class);
            int propuestaId = json.get("propuestaId").getAsInt();

            // Obtener propuesta
            PropuestaDAO propuestaDAO = new PropuestaDAO();
            Propuesta p = propuestaDAO.obtenerPropuesta(propuestaId);

            if (p == null) {
                resp.getWriter().write("No existe propuesta");
                return;
            }

            // Obtener cliente
            ProyectoDAO proyectoDAO = new ProyectoDAO();
            int clienteId = proyectoDAO.obtenerClienteId(p.getProyectoId());

            //  Obtener usuario del cliente
            ClienteDAO clienteDAO = new ClienteDAO();
            int usuarioId = clienteDAO.obtenerUsuarioId(clienteId);

            // Obtener saldo
            SaldoDAO saldoDAO = new SaldoDAO();
            double saldo = saldoDAO.obtenerSaldo(usuarioId);

            //  VALIDAR SALDO
            if (saldo < p.getMonto()) {
                resp.getWriter().write("Saldo insuficiente");
                return;
            }

            //  RESTAR SALDO
            double nuevoSaldo = saldo - p.getMonto();
            saldoDAO.actualizarSaldo(usuarioId, nuevoSaldo);

            // CREAR CONTRATO
            ContratoDAO contratoDAO = new ContratoDAO();
            contratoDAO.crearContrato(propuestaId, p.getMonto());

            // ACTUALIZAR ESTADOS
            propuestaDAO.actualizarEstado(propuestaId, "ACEPTADA");
            proyectoDAO.actualizarEstado(p.getProyectoId(), "EN_PROGRESO");

            // RESPUESTA FINAL
            resp.getWriter().write("Propuesta aceptada correctamente");

        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("ERROR: " + e.getMessage());
        }
    }
}
