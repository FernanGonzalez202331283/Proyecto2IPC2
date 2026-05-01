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
    
   public boolean enviarPropuesta(int freelancerId, Propuesta p) {

    try {
        Connection con = ConexionBD.getConnection();

        // 1. validar duplicado
        String check = "SELECT id FROM propuesta WHERE proyecto_id=? AND freelancer_id=?";
        PreparedStatement psCheck = con.prepareStatement(check);
        psCheck.setInt(1, p.getProyectoId());
        psCheck.setInt(2, freelancerId);

        if (psCheck.executeQuery().next()) {
            throw new RuntimeException("Ya enviaste propuesta a este proyecto");
        }

        // 2. validar estado
        String estadoSql = "SELECT estado, presupuesto FROM proyecto WHERE id=?";
        PreparedStatement psEstado = con.prepareStatement(estadoSql);
        psEstado.setInt(1, p.getProyectoId());

        ResultSet rsEstado = psEstado.executeQuery();

        if (rsEstado.next()) {
            if (!rsEstado.getString("estado").equals("ABIERTO")) {
                throw new RuntimeException("El proyecto no está disponible");
            }

            double max = rsEstado.getDouble("presupuesto");

            if (p.getMonto() > max) {
                throw new RuntimeException("Monto supera presupuesto");
            }
        }

        // 3. validar habilidades
        String sqlHab = "SELECT COUNT(*) AS total " +
                        "FROM freelancer_habilidad fh " +
                        "JOIN proyecto_habilidad ph ON fh.habilidad_id = ph.habilidad_id " +
                        "WHERE fh.freelancer_id = ? AND ph.proyecto_id = ?";

        PreparedStatement psHab = con.prepareStatement(sqlHab);
        psHab.setInt(1, freelancerId);
        psHab.setInt(2, p.getProyectoId());

        ResultSet rsHab = psHab.executeQuery();

        if (rsHab.next() && rsHab.getInt("total") == 0) {
            throw new RuntimeException("No cumples con las habilidades requeridas");
        }

        // 4. insertar
        String sql = "INSERT INTO propuesta (proyecto_id, freelancer_id, monto, tiempo, descripcion, estado, fecha) " +
                     "VALUES (?, ?, ?, ?, ?, 'PENDIENTE', NOW())";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, p.getProyectoId());
        ps.setInt(2, freelancerId);
        ps.setDouble(3, p.getMonto());
        ps.setInt(4, p.getTiempo());
        ps.setString(5, p.getDescripcion());

        ps.executeUpdate();

        return true;

    } catch (RuntimeException e) {
        throw e;
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error al enviar propuesta");
    }
}
    public int obtenerFreelancerId(int usuarioId) {

    int freelancerId = 0;

    try {
        Connection con = ConexionBD.getConnection();

        String sql = "SELECT id FROM freelancer WHERE usuario_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, usuarioId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            freelancerId = rs.getInt("id");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return freelancerId;
}
    
}
