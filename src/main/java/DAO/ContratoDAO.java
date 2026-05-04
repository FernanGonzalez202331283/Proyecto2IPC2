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
}
