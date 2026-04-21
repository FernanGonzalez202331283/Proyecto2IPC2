/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 *
 * @author fernan
 */
public class ContratoDAO {
      public void crearContrato(int propuestaId, double monto) {
        try {
            Connection con = ConexionBD.getConnection();
            String sql = "INSERT INTO contrato (propuesta_id, monto, estado) VALUES (?, ?, 'EN_PROGRESO')";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, propuestaId);
            ps.setDouble(2, monto);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
