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
public class SolicitudHabilidadDAO {
    public boolean crearSolicitud(String nombre, String descripcion, int usuarioId) {

    String sql = "INSERT INTO solicitud_habilidad (nombre, descripcion, estado, usuario_id) VALUES (?, ?, 'PENDIENTE', ?)";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, nombre);
        ps.setString(2, descripcion);
        ps.setInt(3, usuarioId);

        ps.executeUpdate();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}
