/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Modelos.Calificacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author fernan
 */
public class CalificacionDAO {

    public boolean guardarCalificacion(Calificacion c) {

        String sql = "INSERT INTO calificacion "
                + "(contrato_id, freelancer_id, estrellas, comentario) "
                + "VALUES (?, ?, ?, ?)";

        try (
                Connection con = Conexion.ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getContratoId());
            ps.setInt(2, c.getFreelancerId());
            ps.setInt(3, c.getEstrellas());
            ps.setString(4, c.getComentario());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int obtenerFreelancerPorContrato(int contratoId) {

        String sql
                = "SELECT pr.freelancer_id "
                + "FROM contrato c "
                + "JOIN propuesta pr ON c.propuesta_id = pr.id "
                + "WHERE c.id = ?";

        try (
                Connection con = Conexion.ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, contratoId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("freelancer_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
