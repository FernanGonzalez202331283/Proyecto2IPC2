/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.SolicitudHabilidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class SolicitudHabilidadDAO {

    // CREAR SOLICITUD
    public boolean crearSolicitud(
            String nombre,
            String descripcion,
            int categoriaId,
            int usuarioId) {

        String sql =
            "INSERT INTO solicitud_habilidad "
          + "(nombre, descripcion, categoria_id, estado, usuario_id) "
          + "VALUES (?, ?, ?, 'PENDIENTE', ?)";

        try (
            Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setInt(3, categoriaId);
            ps.setInt(4, usuarioId);

            ps.executeUpdate();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    //LISTAR PENDIENTES
    public List<SolicitudHabilidad> listarPendientes() {

        List<SolicitudHabilidad> lista =
                new ArrayList<>();

        String sql =
            "SELECT * FROM solicitud_habilidad " +
            "WHERE estado='PENDIENTE'";

        try (

            Connection con =
                ConexionBD.getConnection();

            PreparedStatement ps =
                con.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery()

        ) {

            while(rs.next()) {

                SolicitudHabilidad s =
                    new SolicitudHabilidad();

                s.setId(rs.getInt("id"));

                s.setNombre(
                    rs.getString("nombre")
                );

                s.setDescripcion(
                    rs.getString("descripcion")
                );

                s.setEstado(
                    rs.getString("estado")
                );

                s.setUsuarioId(
                    rs.getInt("usuario_id")
                );

                s.setCategoriaId(
                    rs.getInt("categoria_id")
                );

                lista.add(s);
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    //ACEPTAR SOLICITUD
    public boolean aceptar(int solicitudId) {

        Connection con = null;

        try {

            con = ConexionBD.getConnection();

            con.setAutoCommit(false);

            // OBTENER SOLICITUD
            String sqlSolicitud =
                "SELECT * FROM solicitud_habilidad " +
                "WHERE id=?";

            PreparedStatement ps1 =
                con.prepareStatement(sqlSolicitud);

            ps1.setInt(1, solicitudId);

            ResultSet rs = ps1.executeQuery();

            if(!rs.next()) {
                return false;
            }

            String nombre =
                rs.getString("nombre");

            int categoriaId =
                rs.getInt("categoria_id");

            // INSERTAR HABILIDAD
            String sqlInsert =
                "INSERT INTO habilidad " +
                "(nombre, categoria_id, estado) " +
                "VALUES (?, ?, 1)";

            PreparedStatement ps2 =
                con.prepareStatement(sqlInsert);

            ps2.setString(1, nombre);

            ps2.setInt(2, categoriaId);

            ps2.executeUpdate();

            // ACTUALIZAR SOLICITUD
            String sqlUpdate =
                "UPDATE solicitud_habilidad " +
                "SET estado='ACEPTADA' " +
                "WHERE id=?";

            PreparedStatement ps3 =
                con.prepareStatement(sqlUpdate);

            ps3.setInt(1, solicitudId);

            ps3.executeUpdate();

            con.commit();

            return true;

        } catch(Exception e) {

            try {

                if(con != null) {
                    con.rollback();
                }

            } catch(Exception ex) {}

            e.printStackTrace();
        }

        return false;
    }

    // RECHAZAR SOLICITUD
    public boolean rechazar(int solicitudId) {

        String sql =
            "UPDATE solicitud_habilidad " +
            "SET estado='RECHAZADA' " +
            "WHERE id=?";

        try (

            Connection con =
                ConexionBD.getConnection();

            PreparedStatement ps =
                con.prepareStatement(sql)

        ) {

            ps.setInt(1, solicitudId);

            return ps.executeUpdate() > 0;

        } catch(Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}