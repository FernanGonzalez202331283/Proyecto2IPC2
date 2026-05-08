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
public class SaldoDAO {
    public double obtenerSaldo(int usuarioId) {
        double saldo = 0;

        try {
            Connection con = ConexionBD.getConnection();
            String sql = "SELECT monto FROM saldo WHERE usuario_id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, usuarioId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                saldo = rs.getDouble("monto");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return saldo;
    }
    
    public void actualizarSaldo(int usuarioId, double nuevoSaldo) {
    try {
        Connection con = ConexionBD.getConnection();
        String sql = "UPDATE saldo SET monto=? WHERE usuario_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setDouble(1, nuevoSaldo);
        ps.setInt(2, usuarioId);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public void sumarSaldoPlataforma(double monto) {

    try {

        Connection con = ConexionBD.getConnection();

        String sql =
            "UPDATE saldo_plataforma " +
            "SET monto = monto + ? " +
            "WHERE id = 1";

        PreparedStatement ps =
            con.prepareStatement(sql);

        ps.setDouble(1, monto);

        ps.executeUpdate();

    } catch (Exception e) {

        e.printStackTrace();

    }
}
    public double obtenerSaldoPlataforma() {

    double saldo = 0;

    try {

        Connection con = ConexionBD.getConnection();

        String sql =
            "SELECT monto " +
            "FROM saldo_plataforma " +
            "WHERE id = 1";

        PreparedStatement ps =
            con.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            saldo = rs.getDouble("monto");

        }

    } catch (Exception e) {

        e.printStackTrace();

    }

    return saldo;
}
}
