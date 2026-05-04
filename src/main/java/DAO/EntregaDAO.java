/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.Entrega;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class EntregaDAO {
    
    //LISTAR ENTREGAS
    public List<Entrega> listarPorContrato(int contratoId) {

        List<Entrega> lista = new ArrayList<>();

        String sql = "SELECT * FROM entrega WHERE contrato_id=? ORDER BY numero DESC";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, contratoId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Entrega e = new Entrega();

                e.setId(rs.getInt("id"));
                e.setContratoId(rs.getInt("contrato_id"));
                e.setDescripcion(rs.getString("descripcion"));
                e.setArchivo(rs.getString("archivo"));
                e.setEstado(rs.getString("estado"));
                e.setComentarioCliente(rs.getString("comentario_cliente"));
                e.setFecha(rs.getString("fecha"));
                e.setNumero(rs.getInt("numero"));

                lista.add(e);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    //CREAR ENTREGA (CORREGIDO)
    public boolean crearEntrega(int contratoId, String descripcion, String archivo) {

        Connection con = null;

        try {
            con = ConexionBD.getConnection();
            con.setAutoCommit(false);

            //VALIDAR QUE EL CONTRATO ESTÉ EN_PROGRESO
            String sqlEstado = "SELECT estado FROM contrato WHERE id=?";
            PreparedStatement psEstado = con.prepareStatement(sqlEstado);
            psEstado.setInt(1, contratoId);
            ResultSet rsEstado = psEstado.executeQuery();

            if (!rsEstado.next() || !"EN_PROGRESO".equals(rsEstado.getString("estado"))) {
                return false;
            }

            //VALIDAR QUE NO HAYA ENTREGA PENDIENTE
            String sqlCheck = "SELECT COUNT(*) FROM entrega WHERE contrato_id=? AND estado='PENDIENTE'";
            PreparedStatement psCheck = con.prepareStatement(sqlCheck);
            psCheck.setInt(1, contratoId);
            ResultSet rsCheck = psCheck.executeQuery();

            if (rsCheck.next() && rsCheck.getInt(1) > 0) {
                return false;
            }

            //OBTENER NÚMERO DE ENTREGA
            String sqlNum = "SELECT IFNULL(MAX(numero),0)+1 FROM entrega WHERE contrato_id=?";
            PreparedStatement psNum = con.prepareStatement(sqlNum);
            psNum.setInt(1, contratoId);
            ResultSet rsNum = psNum.executeQuery();

            int numero = 1;
            if (rsNum.next()) {
                numero = rsNum.getInt(1);
            }

            //INSERTAR ENTREGA
            String sqlInsert = "INSERT INTO entrega (contrato_id, descripcion, archivo, numero) VALUES (?, ?, ?, ?)";
            PreparedStatement psInsert = con.prepareStatement(sqlInsert);

            psInsert.setInt(1, contratoId);
            psInsert.setString(2, descripcion);
            psInsert.setString(3, archivo);
            psInsert.setInt(4, numero);

            psInsert.executeUpdate();

            //ACTUALIZAR PROYECTO  ENTREGA_PENDIENTE
            String sqlUpdate =
                "UPDATE proyecto p " +
                "JOIN propuesta pr ON pr.proyecto_id = p.id " +
                "JOIN contrato c ON c.propuesta_id = pr.id " +
                "SET p.estado = 'ENTREGA_PENDIENTE' " +
                "WHERE c.id = ?";

            PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, contratoId);
            psUpdate.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            return false;
        }
    }

     }
