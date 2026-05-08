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
import java.util.ArrayList;
import java.util.List;

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
            p.setFreelancerId(rs.getInt("freelancer_id"));
            p.setMonto(rs.getDouble("monto"));
            p.setTiempo(rs.getInt("tiempo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setEstado(rs.getString("estado"));
            p.setFecha(rs.getDate("fecha"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return p;
}

    public void actualizarEstado(Connection con, int id, String estado) {
    try {
        String sql = "UPDATE propuesta SET estado=? WHERE id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, estado);
        ps.setInt(2, id);
        ps.executeUpdate();

    } catch (Exception e) {
        throw new RuntimeException("Error al actualizar estado");
    }
}
  public boolean enviarPropuesta(int freelancerId, Propuesta p) {

    try {
        Connection con = ConexionBD.getConnection();

        // VALIDACIÓN GENERAL
        if (freelancerId == 0) {
            throw new RuntimeException("Freelancer no encontrado");
        }

        if (p.getMonto() <= 0 ||
            p.getTiempo() <= 0 ||
            p.getDescripcion() == null || p.getDescripcion().trim().isEmpty()) {

            throw new RuntimeException("Datos incompletos en la propuesta");
        }

        //1. VALIDAR DUPLICADO (IGNORANDO RETIRADAS)
        String check = "SELECT id FROM propuesta " +
                       "WHERE proyecto_id=? AND freelancer_id=? " +
                       "AND estado IN ('EN_REVISION','SELECCIONADA','ACEPTADA')";

        PreparedStatement psCheck = con.prepareStatement(check);
        psCheck.setInt(1, p.getProyectoId());
        psCheck.setInt(2, freelancerId);

        if (psCheck.executeQuery().next()) {
            throw new RuntimeException("Ya tienes una propuesta activa en este proyecto");
        }

        // 2. VALIDAR ESTADO DEL PROYECTO Y PRESUPUESTO
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

        // 3. VALIDAR HABILIDADES
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

        //4. INSERTAR PROPUESTA
        String sql = "INSERT INTO propuesta (proyecto_id, freelancer_id, monto, tiempo, descripcion, estado, fecha) " +
                     "VALUES (?, ?, ?, ?, ?, 'EN_REVISION', NOW())";

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
    public List<Propuesta> listarPorProyecto(int proyectoId) {

    List<Propuesta> lista = new ArrayList<>();

    try {
        Connection con = ConexionBD.getConnection();

        String sql = "SELECT p.id, p.proyecto_id, p.freelancer_id, p.monto, p.tiempo, " +
                     "p.descripcion, p.estado, p.fecha " +
                     "FROM propuesta p " +
                     "WHERE p.proyecto_id = ? " +
                     "ORDER BY p.fecha DESC";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, proyectoId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Propuesta p = new Propuesta();

            p.setId(rs.getInt("id"));
            p.setProyectoId(rs.getInt("proyecto_id"));
            p.setFreelancerId(rs.getInt("freelancer_id"));
            p.setMonto(rs.getDouble("monto"));
            p.setTiempo(rs.getInt("tiempo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setEstado(rs.getString("estado"));
            p.setFecha(rs.getDate("fecha"));

            lista.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}
    public List<Propuesta> listarPorCliente(int userId) {

    List<Propuesta> lista = new ArrayList<>();

    try {
        Connection con = ConexionBD.getConnection();

        String sql =
        "SELECT " +
        "p.id, " +
        "p.proyecto_id, " +
        "pr.titulo AS proyectoTitulo, " +
        "p.freelancer_id, " +
        "p.monto, " +
        "p.tiempo, " +
        "p.descripcion, " +
        "p.estado, " +
        "p.fecha " +
        "FROM propuesta p " +
        "JOIN proyecto pr ON p.proyecto_id = pr.id " +
        "JOIN cliente c ON pr.cliente_id = c.id " +
        "WHERE c.usuario_id = ? " +
        "ORDER BY p.fecha DESC";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Propuesta p = new Propuesta();

            p.setId(rs.getInt("id"));
            p.setProyectoId(rs.getInt("proyecto_id"));
            p.setProyectoTitulo(rs.getString("proyectoTitulo")); 
            p.setFreelancerId(rs.getInt("freelancer_id"));
            p.setMonto(rs.getDouble("monto"));
            p.setTiempo(rs.getInt("tiempo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setEstado(rs.getString("estado"));

            lista.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}
    
    
    public void rechazarPropuesta(int propuestaId) {

    try (Connection con = ConexionBD.getConnection()) {

        String sql = "UPDATE propuesta SET estado='RECHAZADA' WHERE id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, propuestaId);

        int filas = ps.executeUpdate();

        if (filas == 0) {
            throw new RuntimeException("Propuesta no encontrada");
        }

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error al rechazar propuesta");
    }
}
    
    public void seleccionarPropuesta(int propuestaId) {

    try (Connection con = ConexionBD.getConnection()) {

        // 1. obtener proyecto
        String sql = "SELECT proyecto_id FROM propuesta WHERE id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, propuestaId);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new RuntimeException("Propuesta no encontrada");
        }

        int proyectoId = rs.getInt("proyecto_id");

        // 2. validar estado del proyecto
        String validar = "SELECT estado FROM proyecto WHERE id=?";
        PreparedStatement psVal = con.prepareStatement(validar);
        psVal.setInt(1, proyectoId);
        ResultSet rsVal = psVal.executeQuery();

        if (rsVal.next() && !rsVal.getString("estado").equals("ABIERTO")) {
            throw new RuntimeException("El proyecto ya no está disponible");
        }

        // 3. poner proyecto EN_REVISION
        String sqlProyecto = "UPDATE proyecto SET estado='EN_REVISION' WHERE id=?";
        PreparedStatement ps2 = con.prepareStatement(sqlProyecto);
        ps2.setInt(1, proyectoId);
        ps2.executeUpdate();

        //4. marcar la seleccionada
        String sqlSeleccionada = "UPDATE propuesta SET estado='SELECCIONADA' WHERE id=?";
        PreparedStatement psSel = con.prepareStatement(sqlSeleccionada);
        psSel.setInt(1, propuestaId);
        psSel.executeUpdate();

        //5. rechazar las demás
        String sqlRechazar = "UPDATE propuesta SET estado='RECHAZADA' WHERE proyecto_id=? AND id<>?";
        PreparedStatement ps3 = con.prepareStatement(sqlRechazar);
        ps3.setInt(1, proyectoId);
        ps3.setInt(2, propuestaId);
        ps3.executeUpdate();

    } catch (Exception e) {
        throw new RuntimeException(e.getMessage());
    }
}

   public void confirmarPropuesta(int propuestaId) {

    Connection con = null;

    try {

        con = ConexionBD.getConnection();
        con.setAutoCommit(false);

        // 1. obtener propuesta
        String sql =
            "SELECT * FROM propuesta WHERE id=?";

        PreparedStatement ps =
            con.prepareStatement(sql);

        ps.setInt(1, propuestaId);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {

            throw new RuntimeException(
                "Propuesta no encontrada"
            );
        }

        int proyectoId =
            rs.getInt("proyecto_id");

        double monto =
            rs.getDouble("monto");

        // 2. validar proyecto EN_REVISION
        String val =
            "SELECT estado, cliente_id " +
            "FROM proyecto " +
            "WHERE id=?";

        PreparedStatement psVal =
            con.prepareStatement(val);

        psVal.setInt(1, proyectoId);

        ResultSet rsVal =
            psVal.executeQuery();

        if (!rsVal.next() ||
            !rsVal.getString("estado")
                .equals("EN_REVISION")) {

            throw new RuntimeException(
                "El proyecto no está listo para confirmar"
            );
        }

        int clienteId =
            rsVal.getInt("cliente_id");

        // 3. bloquear saldo cliente
        String sqlSaldo =
            "SELECT saldo " +
            "FROM cliente " +
            "WHERE id=? FOR UPDATE";

        PreparedStatement psS =
            con.prepareStatement(sqlSaldo);

        psS.setInt(1, clienteId);

        ResultSet rsS =
            psS.executeQuery();

        if (!rsS.next()) {

            throw new RuntimeException(
                "Cliente no encontrado"
            );
        }

        double saldo =
            rsS.getDouble("saldo");

        if (saldo < monto) {

            throw new RuntimeException(
                "Saldo insuficiente"
            );
        }

        // 4. descontar saldo
        String upd =
            "UPDATE cliente " +
            "SET saldo = saldo - ? " +
            "WHERE id=?";

        PreparedStatement psU =
            con.prepareStatement(upd);

        psU.setDouble(1, monto);
        psU.setInt(2, clienteId);

        psU.executeUpdate();

        // 5. propuesta aceptada
        actualizarEstado(
            con,
            propuestaId,
            "ACEPTADA"
        );

        // 6. proyecto EN_PROGRESO
        String sql3 =
            "UPDATE proyecto " +
            "SET estado='EN_PROGRESO' " +
            "WHERE id=?";

        PreparedStatement ps3 =
            con.prepareStatement(sql3);

        ps3.setInt(1, proyectoId);

        ps3.executeUpdate();

        // 7. crear contrato
        String sqlContrato =
            "INSERT INTO contrato " +
            "(propuesta_id, monto, estado, fecha_inicio) " +
            "VALUES (?, ?, 'EN_PROGRESO', NOW())";

        PreparedStatement ps4 =
            con.prepareStatement(
                sqlContrato,
                PreparedStatement.RETURN_GENERATED_KEYS
            );

        ps4.setInt(1, propuestaId);
        ps4.setDouble(2, monto);

        ps4.executeUpdate();

        // 8. obtener contrato generado
        ResultSet rsContrato =
            ps4.getGeneratedKeys();

        int contratoId = 0;

        if (rsContrato.next()) {

            contratoId =
                rsContrato.getInt(1);
        }

        // =========================
        // COMISION PLATAFORMA
        // =========================

        ConfiguracionComisionDAO daoConfig =
            new ConfiguracionComisionDAO();

        double porcentaje =
            daoConfig.obtenerPorcentajeActual();

        if (porcentaje <= 0) {

            throw new RuntimeException(
                "No existe configuración de comisión"
            );
        }

        // calcular comisión
        double comision =
            monto * (porcentaje / 100.0);

        // 9. guardar comisión
        String sqlComision =
            "INSERT INTO comision_contrato " +
            "(contrato_id, porcentaje, monto) " +
            "VALUES (?, ?, ?)";

        PreparedStatement psCom =
            con.prepareStatement(sqlComision);

        psCom.setInt(1, contratoId);
        psCom.setDouble(2, porcentaje);
        psCom.setDouble(3, comision);

        psCom.executeUpdate();

        // 10. aumentar saldo plataforma
        String sqlPlat =
            "UPDATE saldo_plataforma " +
            "SET monto = monto + ? " +
            "WHERE id = 1";

        PreparedStatement psPlat =
            con.prepareStatement(sqlPlat);

        psPlat.setDouble(1, comision);

        psPlat.executeUpdate();

        con.commit();

    } catch (Exception e) {

        try {

            if (con != null) {
                con.rollback();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        throw new RuntimeException(
            e.getMessage()
        );

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
    
    public void retirarPropuesta(int propuestaId, int freelancerId) {

    try (Connection con = ConexionBD.getConnection()) {

        // 1. obtener propuesta + proyecto
        String sql = "SELECT p.estado, p.freelancer_id, pr.estado AS estadoProyecto " +
                     "FROM propuesta p " +
                     "JOIN proyecto pr ON p.proyecto_id = pr.id " +
                     "WHERE p.id=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, propuestaId);

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new RuntimeException("Propuesta no encontrada");
        }

        // 2. validar que sea del freelancer
        if (rs.getInt("freelancer_id") != freelancerId) {
            throw new RuntimeException("No puedes retirar esta propuesta");
        }

        // 3. validar estado del proyecto
        if (!rs.getString("estadoProyecto").equals("ABIERTO")) {
            throw new RuntimeException("El proyecto ya no está abierto");
        }

        // 4. validar estado de la propuesta
        String estado = rs.getString("estado");

        if (!estado.equals("EN_REVISION")) {
            throw new RuntimeException("No puedes retirar esta propuesta");
        }

        // 5. actualizar estado
        String update = "UPDATE propuesta SET estado='RETIRADA' WHERE id=?";
        PreparedStatement ps2 = con.prepareStatement(update);
        ps2.setInt(1, propuestaId);
        ps2.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Error al retirar propuesta");
    }
}
    public List<Propuesta> listarPorFreelancer(int freelancerId) {

    List<Propuesta> lista = new ArrayList<>();

    try {
        Connection con = ConexionBD.getConnection();

        String sql =
        "SELECT p.*, pr.titulo AS proyectoTitulo, pr.estado AS estadoProyecto " +
        "FROM propuesta p " +
        "JOIN proyecto pr ON p.proyecto_id = pr.id " +
        "WHERE p.freelancer_id = ? " +
        "ORDER BY p.fecha DESC";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, freelancerId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

        Propuesta p = new Propuesta();

        p.setId(rs.getInt("id"));
        p.setProyectoId(rs.getInt("proyecto_id"));
        p.setProyectoTitulo(rs.getString("proyectoTitulo"));
        p.setMonto(rs.getDouble("monto"));
        p.setTiempo(rs.getInt("tiempo"));
        p.setDescripcion(rs.getString("descripcion"));

        //IMPORTANTE
        p.setEstado(rs.getString("estado"));
        p.setEstadoProyecto(rs.getString("estadoProyecto"));

        lista.add(p);
    }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}
    
}
