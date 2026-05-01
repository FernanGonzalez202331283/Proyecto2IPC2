/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.Freelancer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author fernan
 */
public class FreelancerDAO {
    public boolean completarPerfil(int userId, Freelancer f) {

    Connection con = ConexionBD.getConnection();

    try {
        if (f.getBiografia() == null || f.getBiografia().isEmpty()) {
            throw new RuntimeException("Biografía requerida");
        }

        if (f.getNivelExperiencia() == null) {
            throw new RuntimeException("Nivel requerido");
        }

        if (f.getTarifaHora() <= 0) {
            throw new RuntimeException("Tarifa inválida");
        }

        if (f.getHabilidades() == null || f.getHabilidades().length == 0) {
            throw new RuntimeException("Debe seleccionar al menos una habilidad");
        }
        String check = "SELECT id FROM freelancer WHERE usuario_id = ?";
        PreparedStatement psCheck = con.prepareStatement(check);
        psCheck.setInt(1, userId);
        java.sql.ResultSet rsCheck = psCheck.executeQuery();

        if (rsCheck.next()) {
            throw new RuntimeException("El perfil ya fue completado");
        }
        String sql = "INSERT INTO freelancer (usuario_id, biografia, nivel, tarifa) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setInt(1, userId);
        ps.setString(2, f.getBiografia());
        ps.setString(3, f.getNivelExperiencia());
        ps.setDouble(4, f.getTarifaHora());

        ps.executeUpdate();
        java.sql.ResultSet rs = ps.getGeneratedKeys();
        int freelancerId;

        if (rs.next()) {
            freelancerId = rs.getInt(1);
        } else {
            throw new RuntimeException("Error al obtener ID del freelancer");
        }

        String sqlHab = "INSERT INTO freelancer_habilidad (freelancer_id, habilidad_id) VALUES (?, ?)";
        PreparedStatement psHab = con.prepareStatement(sqlHab);

        for (int idHab : f.getHabilidades()) {
            psHab.setInt(1, freelancerId);
            psHab.setInt(2, idHab);
            psHab.executeUpdate();
        }
        String update = "UPDATE usuario SET perfil_completo = 1 WHERE id = ?";
        PreparedStatement psUp = con.prepareStatement(update);
        psUp.setInt(1, userId);
        psUp.executeUpdate();

        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    public double obtenerSaldo(int userId) {

    double saldo = 0;

    try {
        Connection con = ConexionBD.getConnection();

        String sql = "SELECT SUM(monto) as total FROM movimiento_saldo WHERE usuario_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            saldo = rs.getDouble("total");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return saldo;
}
}
