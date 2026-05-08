/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.ContratoDetalle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class ContratoDAO {
     
      
      public List<ContratoDetalle> listarActivosPorFreelancer(int freelancerId) {

        List<ContratoDetalle> lista = new ArrayList<>();

        String sql = "SELECT c.id AS contrato_id, c.monto, pr.id AS proyecto_id, pr.titulo, pr.descripcion, pr.estado " +
                     "FROM contrato c " +
                     "JOIN propuesta p ON c.propuesta_id = p.id " +
                     "JOIN proyecto pr ON p.proyecto_id = pr.id " +
                     "WHERE p.freelancer_id = ? AND c.estado = 'EN_PROGRESO'";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, freelancerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ContratoDetalle c = new ContratoDetalle();

                c.setContratoId(rs.getInt("contrato_id"));
                c.setMonto(rs.getDouble("monto"));
                c.setProyectoId(rs.getInt("proyecto_id"));
                c.setTitulo(rs.getString("titulo"));
                c.setDescripcion(rs.getString("descripcion"));
                c.setEstadoProyecto(rs.getString("estado"));

                lista.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
    public boolean cancelarContrato(int contratoId, String motivo) {

    Connection con = null;

    try {
        con = ConexionBD.getConnection();
        con.setAutoCommit(false);

        //1. CANCELAR CONTRATO
        String sql1 =
            "UPDATE contrato SET estado='CANCELADO', motivo_cancelacion=? WHERE id=?";
        PreparedStatement ps1 = con.prepareStatement(sql1);
        ps1.setString(1, motivo);
        ps1.setInt(2, contratoId);
        ps1.executeUpdate();

        //2. CANCELAR PROYECTO
        String sql2 =
            "UPDATE proyecto p " +
            "JOIN propuesta pr ON pr.proyecto_id = p.id " +
            "JOIN contrato c ON c.propuesta_id = pr.id " +
            "SET p.estado = 'CANCELADO' " +
            "WHERE c.id = ?";
        PreparedStatement ps2 = con.prepareStatement(sql2);
        ps2.setInt(1, contratoId);
        ps2.executeUpdate();

        //3. REEMBOLSAR AL CLIENTE
        String sqlReembolso =
            "SELECT cli.usuario_id, c.monto " +
            "FROM contrato c " +
            "JOIN propuesta pr ON c.propuesta_id = pr.id " +
            "JOIN proyecto p ON pr.proyecto_id = p.id " +
            "JOIN cliente cli ON p.cliente_id = cli.id " +
            "WHERE c.id = ?";

        PreparedStatement psSel = con.prepareStatement(sqlReembolso);
        psSel.setInt(1, contratoId);
        ResultSet rs = psSel.executeQuery();

        if (rs.next()) {

            int usuarioId = rs.getInt("usuario_id");
            double monto = rs.getDouble("monto");

            //3.1 historial
            PreparedStatement psMov = con.prepareStatement(
                "INSERT INTO movimiento_saldo (usuario_id, tipo, monto, fecha) " +
                "VALUES (?, 'REEMBOLSO', ?, NOW())"
            );
            psMov.setInt(1, usuarioId);
            psMov.setDouble(2, monto);
            psMov.executeUpdate();

            // 3.2 actualizar saldo real del cliente
            PreparedStatement psSaldo = con.prepareStatement(
                "UPDATE cliente SET saldo = saldo + ? WHERE usuario_id = ?"
            );
            psSaldo.setDouble(1, monto);
            psSaldo.setInt(2, usuarioId);
            psSaldo.executeUpdate();
        }

        con.commit();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        try { if (con != null) con.rollback(); } catch (Exception ex) {}
        return false;
    }
}
public List<ContratoDetalle> listarActivosPorCliente(int clienteId) {

    List<ContratoDetalle> lista = new ArrayList<>();

    String sql =
        "SELECT c.id AS contrato_id, c.monto, p.titulo, p.descripcion " +
        "FROM contrato c " +
        "JOIN propuesta pr ON c.propuesta_id = pr.id " +
        "JOIN proyecto p ON pr.proyecto_id = p.id " +
        "WHERE p.cliente_id = ? AND c.estado = 'EN_PROGRESO'";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, clienteId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ContratoDetalle c = new ContratoDetalle();

            c.setContratoId(rs.getInt("contrato_id"));
            c.setMonto(rs.getDouble("monto"));
            c.setTitulo(rs.getString("titulo"));
            c.setDescripcion(rs.getString("descripcion"));

            lista.add(c);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}

public int contarActivosPorCliente(int clienteId) {

    int total = 0;

    String sql =
        "SELECT COUNT(*) " +
        "FROM contrato c " +
        "JOIN propuesta pr ON c.propuesta_id = pr.id " +
        "JOIN proyecto p ON pr.proyecto_id = p.id " +
        "WHERE p.cliente_id = ? AND c.estado = 'EN_PROGRESO'";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, clienteId);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            total = rs.getInt(1);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return total;
}
}
