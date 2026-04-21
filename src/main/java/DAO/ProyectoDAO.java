/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author fernan
 */
public class ProyectoDAO {
     public int obtenerClienteId(int proyectoId) {
        int clienteId = 0;

        try {
            Connection con = ConexionBD.getConnection();
            String sql = "SELECT cliente_id FROM proyecto WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, proyectoId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                clienteId = rs.getInt("cliente_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return clienteId;
    }
     
     public void actualizarEstado(int proyectoId, String estado) {
    try {
        Connection con = ConexionBD.getConnection();
        String sql = "UPDATE proyecto SET estado=? WHERE id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, estado);
        ps.setInt(2, proyectoId);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
