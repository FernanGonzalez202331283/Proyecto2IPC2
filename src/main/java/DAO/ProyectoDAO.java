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
import java.util.HashSet;
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

    String sqlHistorial = "INSERT INTO proyecto_estado_historial (proyecto_id, estado, fecha) VALUES (?, 'ABIERTO', NOW())";

    String sqlHab = "INSERT INTO proyecto_habilidad (proyecto_id, habilidad_id) VALUES (?, ?)";

    Connection con = null;

    try {
        con = ConexionBD.getConnection();
        con.setAutoCommit(false);

        if (p.getTitulo() == null || p.getTitulo().trim().isEmpty() ||
            p.getDescripcion() == null || p.getDescripcion().trim().isEmpty() ||
            p.getPresupuesto() <= 0 ||
            p.getFechaLimite() == null ||
            p.getCategoriaId() == 0) {

            throw new Exception("Datos del proyecto incompletos");
        }

       //VALIDAR
        if (p.getHabilidades() == null || p.getHabilidades().length == 0) {
            throw new Exception("Debe seleccionar al menos una habilidad");
        }

        //ELIMINAR DUPLICADOS
        java.util.Set<Integer> habilidadesUnicas = new java.util.HashSet<>();

        for (int hab : p.getHabilidades()) {
            habilidadesUnicas.add(hab);
        }

        
        PreparedStatement ps = con.prepareStatement(sqlProyecto, PreparedStatement.RETURN_GENERATED_KEYS);

        ps.setInt(1, clienteId);
        ps.setInt(2, p.getCategoriaId());
        ps.setString(3, p.getTitulo().trim());
        ps.setString(4, p.getDescripcion().trim());
        ps.setDouble(5, p.getPresupuesto());
        ps.setDate(6, java.sql.Date.valueOf(p.getFechaLimite()));

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        int proyectoId = 0;

        if (rs.next()) {
            proyectoId = rs.getInt(1);
        } else {
            throw new Exception("No se pudo obtener el ID del proyecto");
        }

        PreparedStatement psHist = con.prepareStatement(sqlHistorial);
        psHist.setInt(1, proyectoId);
        psHist.executeUpdate();

        PreparedStatement psHab = con.prepareStatement(sqlHab);

        for (int habId : habilidadesUnicas) {
            psHab.setInt(1, proyectoId);
            psHab.setInt(2, habId);
            psHab.addBatch();
        }

        psHab.executeBatch();
        con.commit();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        try {
            if (con != null) con.rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;

    } finally {
        try {
            if (con != null) con.setAutoCommit(true);
            if (con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        String sql = "SELECT p.id, p.titulo, p.descripcion, p.presupuesto, p.categoria_id, p.fecha_limite, c.nombre AS categoria " +
                     "FROM proyecto p " +
                     "JOIN categoria c ON p.categoria_id = c.id " +
                     "WHERE p.estado = 'ABIERTO'";

        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Proyecto p = new Proyecto();

            p.setId(rs.getInt("id"));
            p.setTitulo(rs.getString("titulo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPresupuesto(rs.getDouble("presupuesto"));
            p.setCategoriaId(rs.getInt("categoria_id"));
            p.setFechaLimite(rs.getDate("fecha_limite").toString());
            
            p.setCategoria(rs.getString("categoria"));

            //
            p.setHabilidades(obtenerHabilidadesPorProyecto(p.getId(), con));

            lista.add(p);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return lista;
}
   public Proyecto obtenerPorId(int id) {

    Proyecto p = null;

    try {
        Connection con = ConexionBD.getConnection();

        String sql = "SELECT p.id, p.titulo, p.descripcion, p.presupuesto, p.categoria_id, p.fecha_limite, c.nombre AS categoria " +
                     "FROM proyecto p " +
                     "JOIN categoria c ON p.categoria_id = c.id " +
                     "WHERE p.id = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            p = new Proyecto();

            p.setId(rs.getInt("id"));
            p.setTitulo(rs.getString("titulo"));
            p.setDescripcion(rs.getString("descripcion"));
            p.setPresupuesto(rs.getDouble("presupuesto"));
            p.setCategoriaId(rs.getInt("categoria_id"));
            p.setFechaLimite(rs.getDate("fecha_limite").toString());
            p.setCategoria(rs.getString("categoria"));

            
            p.setHabilidades(obtenerHabilidadesPorProyecto(id, con));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return p;
}
   
   private int[] obtenerHabilidadesPorProyecto(int proyectoId, Connection con) {

    List<Integer> lista = new ArrayList<>();

    try {
        String sql = "SELECT habilidad_id FROM proyecto_habilidad WHERE proyecto_id = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, proyectoId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            lista.add(rs.getInt("habilidad_id"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
    int[] arr = new int[lista.size()];
    for (int i = 0; i < lista.size(); i++) {
        arr[i] = lista.get(i);
    }

    return arr;
}
    }
