/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.Propuesta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author fernan
 */
public class PropuestaDAO {

    public Propuesta obtenerPropuesta(int id) {
        Propuesta p = null;

        try {
            Connection con = ConexionBD.getConnection();
            String sql = "SELECT * FROM propuesta WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                p = new Propuesta();
                p.setId(rs.getInt("id"));
                p.setProyectoId(rs.getInt("proyecto_id"));
                p.setMonto(rs.getDouble("monto"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    public void actualizarEstado(int id, String estado) {
    try {
        Connection con = ConexionBD.getConnection();
        String sql = "UPDATE propuesta SET estado=? WHERE id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, estado);
        ps.setInt(2, id);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
}
