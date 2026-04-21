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
}
