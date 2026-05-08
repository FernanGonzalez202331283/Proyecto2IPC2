/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.HistorialComision;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class ConfiguracionComisionDAO {
    
    // OBTENER COMISION ACTUAL
    public double obtenerPorcentajeActual() {

        double porcentaje = 0;

        try {

            Connection con =
                ConexionBD.getConnection();

            String sql =
                "SELECT porcentaje_actual " +
                "FROM configuracion_comision " +
                "WHERE id = 1";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery();

            if (rs.next()) {

                porcentaje =
                    rs.getDouble(
                        "porcentaje_actual"
                    );
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return porcentaje;
    }

    // ACTUALIZAR PORCENTAJE
    public void actualizarPorcentaje(
        double nuevoPorcentaje
    ) {

        Connection con = null;

        try {

            con =
                ConexionBD.getConnection();

            con.setAutoCommit(false);

            // =========================
            // 1. CERRAR HISTORIAL ACTUAL
            // =========================

            String cerrarHistorial =
                "UPDATE historial_comision " +
                "SET fecha_fin = NOW() " +
                "WHERE fecha_fin IS NULL";

            PreparedStatement psCerrar =
                con.prepareStatement(
                    cerrarHistorial
                );

            psCerrar.executeUpdate();

            // =========================
            // 2. ACTUALIZAR CONFIGURACION
            // =========================

            String actualizar =
                "UPDATE configuracion_comision " +
                "SET porcentaje_actual=? " +
                "WHERE id=1";

            PreparedStatement psActualizar =
                con.prepareStatement(
                    actualizar
                );

            psActualizar.setDouble(
                1,
                nuevoPorcentaje
            );

            psActualizar.executeUpdate();

            // =========================
            // 3. INSERTAR NUEVO HISTORIAL
            // =========================

            String insertarHistorial =
                "INSERT INTO historial_comision " +
                "(porcentaje, fecha_inicio, fecha_fin) " +
                "VALUES (?, NOW(), NULL)";

            PreparedStatement psInsertar =
                con.prepareStatement(
                    insertarHistorial
                );

            psInsertar.setDouble(
                1,
                nuevoPorcentaje
            );

            psInsertar.executeUpdate();

            con.commit();

        } catch (Exception e) {

            try {

                if (con != null) {

                    con.rollback();
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            e.printStackTrace();

        } finally {

            try {

                if (con != null) {

                    con.setAutoCommit(true);

                    con.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
    
    public List<HistorialComision>listarHistorial() {
    List<HistorialComision> lista = new ArrayList<>();
    try {

        Connection con =
            ConexionBD.getConnection();

        String sql =
            "SELECT * " +
            "FROM historial_comision " +
            "ORDER BY fecha_inicio DESC";

        PreparedStatement ps =
            con.prepareStatement(sql);

        ResultSet rs =
            ps.executeQuery();

        while (rs.next()) {

            HistorialComision h =
                new HistorialComision();

            h.setId(
                rs.getInt("id")
            );

            h.setPorcentaje(
                rs.getDouble("porcentaje")
            );

            h.setFechaInicio(
                rs.getString("fecha_inicio")
            );

            h.setFechaFin(
                rs.getString("fecha_fin")
            );

            lista.add(h);
        }

        con.close();

    } catch (Exception e) {

        e.printStackTrace();
    }

    return lista;
}
}

