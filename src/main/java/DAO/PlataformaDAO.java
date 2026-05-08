/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.ModeloComision;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class PlataformaDAO {

    public double obtenerSaldoPlataforma() {

        double saldo = 0;

        try {

            Connection con = ConexionBD.getConnection();

            String sql
                    = "SELECT monto FROM saldo_plataforma WHERE id = 1";

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                saldo = rs.getDouble("monto");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return saldo;
    }

    public List<ModeloComision> listarComisiones() {

        List<ModeloComision> lista
                = new ArrayList<>();

        try {

            Connection con
                    = ConexionBD.getConnection();

            String sql
                    = "SELECT * FROM comision_contrato "
                    + "ORDER BY id DESC";

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ModeloComision c
                        = new ModeloComision();

                c.setContratoId(
                        rs.getInt("contrato_id")
                );

                c.setPorcentaje(
                        rs.getDouble("porcentaje")
                );

                c.setMonto(
                        rs.getDouble("monto")
                );

                lista.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}
