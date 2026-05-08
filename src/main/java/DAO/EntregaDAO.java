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
    public List<Entrega> listarPorContrato(int contratoId) {

        List<Entrega> lista = new ArrayList<>();

        String sql = "SELECT * FROM entrega WHERE contrato_id=? ORDER BY numero DESC";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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
    public boolean crearEntrega(int contratoId, String descripcion, String archivo) {

        Connection con = null;

        try {
            con = ConexionBD.getConnection();
            con.setAutoCommit(false);

            // VALIDAR EL CONTRATO
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
            String sqlUpdate
                    = "UPDATE proyecto p "
                    + "JOIN propuesta pr ON pr.proyecto_id = p.id "
                    + "JOIN contrato c ON c.propuesta_id = pr.id "
                    + "SET p.estado = 'ENTREGA_PENDIENTE' "
                    + "WHERE c.id = ?";

            PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
            psUpdate.setInt(1, contratoId);
            psUpdate.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception ex) {
            }
            return false;
        }
    }

    public boolean aprobarEntrega(int contratoId) {

        Connection con = null;

        try {
            con = ConexionBD.getConnection();
            con.setAutoCommit(false);

            // 1 OBTENER ÚLTIMA ENTREGA
            String sqlUltima = "SELECT id, estado FROM entrega WHERE contrato_id=? ORDER BY numero DESC LIMIT 1";
            PreparedStatement psUltima = con.prepareStatement(sqlUltima);
            psUltima.setInt(1, contratoId);
            ResultSet rs = psUltima.executeQuery();

            if (!rs.next()) {
                return false;
            }

            int entregaId = rs.getInt("id");
            String estado = rs.getString("estado");

            if (!"PENDIENTE".equals(estado)) {
                return false;
            }

            // 2 APROBAR ENTREGA
            PreparedStatement ps1 = con.prepareStatement(
                    "UPDATE entrega SET estado='APROBADA' WHERE id=?"
            );
            ps1.setInt(1, entregaId);
            ps1.executeUpdate();

            // 3 COMPLETAR CONTRATO
            PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE contrato SET estado='COMPLETADO', fecha_fin=NOW() WHERE id=?"
            );
            ps2.setInt(1, contratoId);
            ps2.executeUpdate();

            //4 COMPLETAR PROYECTO
            PreparedStatement ps3 = con.prepareStatement(
                    "UPDATE proyecto p "
                    + "JOIN propuesta pr ON pr.proyecto_id = p.id "
                    + "JOIN contrato c ON c.propuesta_id = pr.id "
                    + "SET p.estado = 'COMPLETADO' WHERE c.id=?"
            );
            ps3.setInt(1, contratoId);
            ps3.executeUpdate();

            //5 OBTENER DATOS
            String sqlData
                    = "SELECT c.monto, cli.usuario_id AS cliente_user, f.usuario_id AS freelancer_user "
                    + "FROM contrato c "
                    + "JOIN propuesta pr ON c.propuesta_id = pr.id "
                    + "JOIN proyecto p ON pr.proyecto_id = p.id "
                    + "JOIN cliente cli ON p.cliente_id = cli.id "
                    + "JOIN freelancer f ON pr.freelancer_id = f.id "
                    + "WHERE c.id = ?";

            PreparedStatement psData = con.prepareStatement(sqlData);
            psData.setInt(1, contratoId);
            ResultSet rsData = psData.executeQuery();

            if (rsData.next()) {

                double monto = rsData.getDouble("monto");
                int clienteUser = rsData.getInt("cliente_user");
                int freelancerUser = rsData.getInt("freelancer_user");

                double pagoFreelancer = monto * 0.9;
                double comision = monto * 0.1;

                //6 DESCONTAR AL CLIENTE
                PreparedStatement psPago = con.prepareStatement(
                        "INSERT INTO movimiento_saldo (usuario_id, tipo, monto, fecha) "
                        + "VALUES (?, 'PAGO', ?, NOW())"
                );
                psPago.setInt(1, clienteUser);
                psPago.setDouble(2, monto);
                psPago.executeUpdate();

                //7 PAGAR AL FREELANCER
                PreparedStatement psIngreso = con.prepareStatement(
                        "INSERT INTO movimiento_saldo (usuario_id, tipo, monto, fecha) "
                        + "VALUES (?, 'INGRESO', ?, NOW())"
                );
                psIngreso.setInt(1, freelancerUser);
                psIngreso.setDouble(2, pagoFreelancer);
                psIngreso.executeUpdate();

                // GUARDAR COMISIÓN
                PreparedStatement psComision = con.prepareStatement(
                        "INSERT INTO comision_contrato (contrato_id, porcentaje, monto) "
                        + "VALUES (?, ?, ?)"
                );
                psComision.setInt(1, contratoId);
                psComision.setDouble(2, 10.0);
                psComision.setDouble(3, comision);
                psComision.executeUpdate();
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception ex) {
            }
            return false;
        }
    }

    public boolean rechazarEntrega(int contratoId, String motivo) {

        Connection con = null;

        try {
            con = ConexionBD.getConnection();
            con.setAutoCommit(false);

            // OBTENER ÚLTIMA ENTREGA
            String sqlUltima = "SELECT id, estado FROM entrega WHERE contrato_id=? ORDER BY numero DESC LIMIT 1";
            PreparedStatement psUltima = con.prepareStatement(sqlUltima);
            psUltima.setInt(1, contratoId);
            ResultSet rs = psUltima.executeQuery();

            if (!rs.next()) {
                return false;
            }

            int entregaId = rs.getInt("id");
            String estado = rs.getString("estado");

            // VALIDAR
            if (!"PENDIENTE".equals(estado)) {
                return false;
            }

            //RECHAZAR
            String sql1 = "UPDATE entrega SET estado='RECHAZADA', comentario_cliente=? WHERE id=?";
            PreparedStatement ps1 = con.prepareStatement(sql1);
            ps1.setString(1, motivo);
            ps1.setInt(2, entregaId);
            ps1.executeUpdate();

            //VOLVER A EN_PROGRESO
            String sql2
                    = "UPDATE proyecto p "
                    + "JOIN propuesta pr ON pr.proyecto_id = p.id "
                    + "JOIN contrato c ON c.propuesta_id = pr.id "
                    + "SET p.estado = 'EN_PROGRESO' "
                    + "WHERE c.id = ?";

            PreparedStatement ps2 = con.prepareStatement(sql2);
            ps2.setInt(1, contratoId);
            ps2.executeUpdate();

            //VOLVER CONTRATO A EN_PROGRESO
            String sql3 = "UPDATE contrato SET estado='EN_PROGRESO' WHERE id=?";
            PreparedStatement ps3 = con.prepareStatement(sql3);
            ps3.setInt(1, contratoId);
            ps3.executeUpdate();

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (Exception ex) {
            }
            return false;
        }
    }

    public Entrega obtenerUltimaEntrega(int contratoId) {

        String sql = "SELECT * FROM entrega WHERE contrato_id=? ORDER BY numero DESC LIMIT 1";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, contratoId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Entrega e = new Entrega();

                e.setId(rs.getInt("id"));
                e.setContratoId(rs.getInt("contrato_id"));
                e.setDescripcion(rs.getString("descripcion"));
                e.setArchivo(rs.getString("archivo"));
                e.setEstado(rs.getString("estado"));
                e.setComentarioCliente(rs.getString("comentario_cliente"));
                e.setFecha(rs.getString("fecha"));
                e.setNumero(rs.getInt("numero"));

                return e;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
