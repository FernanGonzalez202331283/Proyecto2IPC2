/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author fernan
 */
public class ClienteDAO {
     public int obtenerUsuarioId(int clienteId) {
        int usuarioId = 0;

        try {
            Connection con = ConexionBD.getConnection();
            String sql = "SELECT usuario_id FROM cliente WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, clienteId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                usuarioId = rs.getInt("usuario_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return usuarioId;
    }
     public boolean crearCliente(int userId, Cliente c) {
    String sqlCliente = "INSERT INTO cliente (usuario_id, descripcion, sector, sitio_web) VALUES (?, ?, ?, ?)";
    String sqlUsuario = "UPDATE usuario SET perfil_completo = 1 WHERE id = ?";

    try (Connection con = ConexionBD.getConnection()) {

        con.setAutoCommit(false);

        // 1. insertar cliente
        PreparedStatement ps1 = con.prepareStatement(sqlCliente);
        ps1.setInt(1, userId);
        ps1.setString(2, c.getDescripcion());
        ps1.setString(3, c.getSector());
        ps1.setString(4, c.getSitioWeb());
        ps1.executeUpdate();

        // 2. actualizar usuario
        PreparedStatement ps2 = con.prepareStatement(sqlUsuario);
        ps2.setInt(1, userId);
        ps2.executeUpdate();

        con.commit();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
     public Map<String, Object> obtenerDashboard(int userId) {

    Map<String, Object> data = new HashMap<>();

    try (Connection con = ConexionBD.getConnection()) {

        // 1. Obtener saldo
        String sqlSaldo = "SELECT saldo FROM cliente WHERE usuario_id=?";
        PreparedStatement ps1 = con.prepareStatement(sqlSaldo);
        ps1.setInt(1, userId);
        ResultSet rs1 = ps1.executeQuery();

        double saldo = 0;
        if (rs1.next()) {
            saldo = rs1.getDouble("saldo");
        }

        // 2. Obtener clienteId
        int clienteId = 0;
        String sqlCliente = "SELECT id FROM cliente WHERE usuario_id=?";
        PreparedStatement psCliente = con.prepareStatement(sqlCliente);
        psCliente.setInt(1, userId);
        ResultSet rsCliente = psCliente.executeQuery();

        if (rsCliente.next()) {
            clienteId = rsCliente.getInt("id");
        }

        // 3. Obtener cantidad de proyectos
        String sqlProyectos = "SELECT COUNT(*) AS total FROM proyecto WHERE cliente_id = ?";
        PreparedStatement ps2 = con.prepareStatement(sqlProyectos);
        ps2.setInt(1, clienteId);
        ResultSet rs2 = ps2.executeQuery();

        int totalProyectos = 0;
        if (rs2.next()) {
            totalProyectos = rs2.getInt("total");
        }

        data.put("saldo", saldo);
        data.put("totalProyectos", totalProyectos);

    } catch (Exception e) {
        e.printStackTrace();
    }

    return data;
}
     public boolean recargarSaldo(int usuarioId, double monto) {

    String sqlRecarga = "INSERT INTO recarga (usuario_id, monto, fecha) VALUES (?, ?, NOW())";
    String sqlUpdate = "UPDATE cliente SET saldo = saldo + ? WHERE usuario_id = ?";

    try (Connection con = ConexionBD.getConnection()) {

        con.setAutoCommit(false);

        //  NSERTAR RECARGA
        PreparedStatement ps1 = con.prepareStatement(sqlRecarga);
        ps1.setInt(1, usuarioId);
        ps1.setDouble(2, monto);
        ps1.executeUpdate();

        // ACTUALIZAR SALDO
        PreparedStatement ps2 = con.prepareStatement(sqlUpdate);
        ps2.setDouble(1, monto);
        ps2.setInt(2, usuarioId);
        ps2.executeUpdate();

        con.commit();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
     
     public int contarPropuestas(int userId) {
    int total = 0;

    try {
        Connection con = ConexionBD.getConnection();

        String sql =
        "SELECT COUNT(*) AS total " +
        "FROM propuesta p " +
        "JOIN proyecto pr ON p.proyecto_id = pr.id " +
        "JOIN cliente c ON pr.cliente_id = c.id " +
        "WHERE c.usuario_id = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            total = rs.getInt("total");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return total;
}
     
     public int obtenerIdPorUsuario(int userId) {

    int clienteId = 0;

    try {
        Connection con = ConexionBD.getConnection();

        String sql = "SELECT id FROM cliente WHERE usuario_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            clienteId = rs.getInt("id");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return clienteId;
}
}
