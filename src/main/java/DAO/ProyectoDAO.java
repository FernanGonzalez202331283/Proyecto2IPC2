/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.Proyecto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class ProyectoDAO {
     public int obtenerClienteIdPorUsuario(int usuarioId) {
    int clienteId = 0;

    try {
        Connection con = ConexionBD.getConnection();
        String sql = "SELECT id FROM cliente WHERE usuario_id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, usuarioId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            clienteId = rs.getInt("id");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return clienteId;
}
     
     public void actualizarEstado(int proyectoId, String estado) {
    try {
        Connection con = ConexionBD.getConnection();
        String sql = "UPDATE proyecto SET estado=? WHERE id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, estado);
        ps.setInt(2, proyectoId);
        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
     
     public boolean crearProyecto(int clienteId, Proyecto p) {

    String sqlProyecto = "INSERT INTO proyecto (cliente_id, categoria_id, titulo, descripcion, presupuesto, fecha_limite, estado, fecha_creacion) VALUES (?, ?, ?, ?, ?, ?, 'ABIERTO', NOW())";

    try (Connection con = ConexionBD.getConnection()) {

        con.setAutoCommit(false);

        //INSERTAR PROYECTO
        PreparedStatement ps = con.prepareStatement(sqlProyecto, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setInt(1, clienteId);
        ps.setInt(2, p.getCategoriaId());
        ps.setString(3, p.getTitulo());
        ps.setString(4, p.getDescripcion());
        ps.setDouble(5, p.getPresupuesto());
        ps.setDate(6, java.sql.Date.valueOf(p.getFechaLimite()));

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        int proyectoId = 0;

        if (rs.next()) {
            proyectoId = rs.getInt(1);
        }

        //HISTORIAL
        String sqlHistorial = "INSERT INTO proyecto_estado_historial (proyecto_id, estado, fecha) VALUES (?, 'ABIERTO', NOW())";

        PreparedStatement psHist = con.prepareStatement(sqlHistorial);
        psHist.setInt(1, proyectoId);
        psHist.executeUpdate();

        //HABILIDADES
        String sqlHab = "INSERT INTO proyecto_habilidad (proyecto_id, habilidad_id) VALUES (?, ?)";

        PreparedStatement psHab = con.prepareStatement(sqlHab);

       if (p.getHabilidades() != null) {
            for (int habId : p.getHabilidades()) {
                psHab.setInt(1, proyectoId);
                psHab.setInt(2, habId);
                psHab.addBatch();
            }
            psHab.executeBatch();
        }
        con.commit();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    public List<Proyecto> listarProyectos() {

    List<Proyecto> lista = new ArrayList<>();

    String sql = "SELECT p.id, p.titulo, p.descripcion, p.presupuesto, p.estado, p.fecha_limite, c.nombre AS categoria " +
                 "FROM proyecto p " +
                 "JOIN categoria c ON p.categoria_id = c.id " +
                 "WHERE p.estado = 'ABIERTO'";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Proyecto p = new Proyecto();

            p.setId(rs.getInt("id"));
            p.setTitulo(rs.getString("titulo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPresupuesto(rs.getDouble("presupuesto"));
            p.setEstado(rs.getString("estado"));
            p.setFechaLimite(rs.getDate("fecha_limite").toString());

            lista.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}
    
    public List<Proyecto> listarProyectosPorCliente(int clienteId) {

    List<Proyecto> lista = new ArrayList<>();

    String sql = "SELECT id, titulo, descripcion, presupuesto, estado, fecha_limite " +
                 "FROM proyecto WHERE cliente_id = ?";

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, clienteId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Proyecto p = new Proyecto();

            p.setId(rs.getInt("id"));
            p.setTitulo(rs.getString("titulo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPresupuesto(rs.getDouble("presupuesto"));
            p.setEstado(rs.getString("estado"));
            p.setFechaLimite(rs.getDate("fecha_limite").toString());

            lista.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}
    
    public List<Proyecto> listarAbiertos() {

    List<Proyecto> lista = new ArrayList<>();

    try {
        Connection con = ConexionBD.getConnection();

        String sql = "SELECT id, titulo, descripcion, presupuesto FROM proyecto WHERE estado = 'ABIERTO'";
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Proyecto p = new Proyecto();
            p.setId(rs.getInt("id"));
            p.setTitulo(rs.getString("titulo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPresupuesto(rs.getDouble("presupuesto"));

            lista.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}
   
    }
