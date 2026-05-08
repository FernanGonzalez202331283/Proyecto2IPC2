/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.SolicitarCategoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class SolicitudCategoriaDAO {

    public boolean crearSolicitud(
            SolicitarCategoria s
    ) {

        String sql
                = "INSERT INTO solicitud_categoria "
                + "(nombre, estado, usuario_id) "
                + "VALUES (?, 'PENDIENTE', ?)";

        try (
                Connection con
                = ConexionBD.getConnection(); PreparedStatement ps
                = con.prepareStatement(sql)) {

            ps.setString(1, s.getNombre());

            ps.setInt(2, s.getUsuario_id());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }

    }

    public List<SolicitarCategoria> listarPendientes() {

        List<SolicitarCategoria> lista
                = new ArrayList<>();

        String sql
                = "SELECT * FROM solicitud_categoria "
                + "WHERE estado='PENDIENTE'";

        try (
                Connection con
                = ConexionBD.getConnection(); PreparedStatement ps
                = con.prepareStatement(sql); ResultSet rs
                = ps.executeQuery()) {

            while (rs.next()) {

                SolicitarCategoria s
                        = new SolicitarCategoria();

                s.setId(rs.getInt("id"));
                s.setNombre(rs.getString("nombre"));
                s.setEstado(rs.getString("estado"));
                s.setUsuario_id(
                        rs.getInt("usuario_id")
                );

                lista.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean aceptar(int solicitudId) {

        Connection con = null;

        try {

            con = ConexionBD.getConnection();

            con.setAutoCommit(false);

            //OBTENER SOLICITUD
            String sqlSolicitud
                    = "SELECT * FROM solicitud_categoria "
                    + "WHERE id=?";

            PreparedStatement ps1
                    = con.prepareStatement(sqlSolicitud);

            ps1.setInt(1, solicitudId);

            ResultSet rs = ps1.executeQuery();

            if (!rs.next()) {
                return false;
            }

            String nombre
                    = rs.getString("nombre");

            // INSERTAR CATEGORIA
            String sqlInsert
                    = "INSERT INTO categoria "
                    + "(nombre, estado) "
                    + "VALUES (?, 1)";

            PreparedStatement ps2
                    = con.prepareStatement(sqlInsert);

            ps2.setString(1, nombre);

            ps2.executeUpdate();

            // ACTUALIZAR SOLICITUD
            String sqlUpdate
                    = "UPDATE solicitud_categoria "
                    + "SET estado='ACEPTADA' "
                    + "WHERE id=?";

            PreparedStatement ps3
                    = con.prepareStatement(sqlUpdate);

            ps3.setInt(1, solicitudId);

            ps3.executeUpdate();

            con.commit();

            return true;

        } catch (Exception e) {

            try {

                if (con != null) {
                    con.rollback();
                }

            } catch (Exception ex) {
            }

            e.printStackTrace();
        }

        return false;
    }

    public boolean rechazar(int solicitudId) {

        String sql
                = "UPDATE solicitud_categoria "
                + "SET estado='RECHAZADA' "
                + "WHERE id=?";

        try (
                Connection con
                = ConexionBD.getConnection(); PreparedStatement ps
                = con.prepareStatement(sql)) {

            ps.setInt(1, solicitudId);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}
