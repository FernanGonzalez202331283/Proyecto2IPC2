/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.ReporteContratoFreelancer;
import Modelos.ReporteGastoCategoria;
import Modelos.ReporteIngresos;
import Modelos.ReportePropuestaFreelancer;
import Modelos.ReporteProyecto;
import Modelos.ReporteRecarga;
import Modelos.TopCategoriaFreelancer;
import Modelos.TopCategoriaReporte;
import Modelos.TopFreelancerReporte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class ReporteDAO {

    public List<TopFreelancerReporte> topFreelancers(String fechaInicio, String fechaFin) {
        List<TopFreelancerReporte> lista = new ArrayList<>();
        try {
            Connection con = ConexionBD.getConnection();
            String sql
                    = "SELECT "
                    + "u.nombre AS nombre, "
                    + "COUNT(c.id) "
                    + "AS contratos_completados, "
                    + "SUM(c.monto) "
                    + "AS total_generado, "
                    + "SUM(cc.monto) "
                    + "AS comision_plataforma "
                    + "FROM contrato c "
                    + "INNER JOIN propuesta p "
                    + "ON c.propuesta_id = p.id "
                    + "INNER JOIN freelancer f "
                    + "ON p.freelancer_id = f.id "
                    + "INNER JOIN usuario u "
                    + "ON f.usuario_id = u.id "
                    + "INNER JOIN comision_contrato cc "
                    + "ON c.id = cc.contrato_id "
                    + "WHERE c.estado = 'COMPLETADO' "
                    + "AND c.fecha_fin "
                    + "BETWEEN ? AND ? "
                    + "GROUP BY u.id, u.nombre "
                    + "ORDER BY total_generado DESC "
                    + "LIMIT 5";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TopFreelancerReporte t
                        = new TopFreelancerReporte();
                t.setNombre(
                        rs.getString("nombre")
                );
                t.setContratosCompletados(
                        rs.getInt(
                                "contratos_completados"
                        )
                );
                t.setTotalGenerado(
                        rs.getDouble(
                                "total_generado"
                        )
                );
                t.setComisionPlataforma(
                        rs.getDouble(
                                "comision_plataforma"
                        )
                );
                lista.add(t);
            }
            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public List<TopCategoriaReporte> topCategorias(String fechaInicio, String fechaFin) {
        List<TopCategoriaReporte> lista = new ArrayList<>();
        try {
            Connection con = ConexionBD.getConnection();
            String sql
                    = "SELECT "
                    + "c.nombre AS categoria, "
                    + "COUNT(co.id) AS cantidadContratos, "
                    + "SUM(cc.monto) AS totalComisiones "
                    + "FROM contrato co "
                    + "INNER JOIN propuesta p "
                    + "ON co.propuesta_id = p.id "
                    + "INNER JOIN proyecto pr "
                    + "ON p.proyecto_id = pr.id "
                    + "INNER JOIN categoria c "
                    + "ON pr.categoria_id = c.id "
                    + "INNER JOIN comision_contrato cc "
                    + "ON co.id = cc.contrato_id "
                    + "WHERE co.estado = 'COMPLETADO' "
                    + "AND co.fecha_fin "
                    + "BETWEEN ? AND ? "
                    + "GROUP BY c.id, c.nombre "
                    + "ORDER BY cantidadContratos DESC "
                    + "LIMIT 5";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fechaInicio);
            ps.setString(2, fechaFin);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TopCategoriaReporte t
                        = new TopCategoriaReporte();
                t.setCategoria(
                        rs.getString("categoria")
                );
                t.setCantidadContratos(
                        rs.getInt(
                                "cantidadContratos"
                        )
                );
                t.setTotalComisiones(
                        rs.getDouble(
                                "totalComisiones"
                        )
                );
                lista.add(t);
            }
            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public ReporteIngresos reporteIngresos(String inicio, String fin) {

        ReporteIngresos data = null;

        try {

            Connection con = ConexionBD.getConnection();

            String sql
                    = "SELECT COUNT(c.id) AS total_contratos, "
                    + "COALESCE(SUM(cc.monto), 0) AS total_comisiones "
                    + "FROM contrato c "
                    + "INNER JOIN comision_contrato cc ON c.id = cc.contrato_id "
                    + "WHERE c.estado = 'COMPLETADO' "
                    + "AND c.fecha_fin BETWEEN ? AND ?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, inicio);
            ps.setString(2, fin);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                data = new ReporteIngresos(
                        rs.getInt("total_contratos"),
                        rs.getDouble("total_comisiones")
                );
            }

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public List<ReporteProyecto> obtenerReporteProyectos(
            int clienteId,
            String fechaInicio,
            String fechaFin
    ) {

        List<ReporteProyecto> lista = new ArrayList<>();

        String sql = """
    SELECT
        p.id,
        p.titulo,
        p.estado,
        p.presupuesto,
        p.fecha_creacion,

        u.nombre AS freelancer,

        c.monto AS monto_contrato

    FROM proyecto p

    LEFT JOIN propuesta pr
        ON p.id = pr.proyecto_id

    LEFT JOIN contrato c
        ON pr.id = c.propuesta_id

    LEFT JOIN freelancer f
        ON pr.freelancer_id = f.id

    LEFT JOIN usuario u
        ON f.usuario_id = u.id

    WHERE p.cliente_id = ?
    AND DATE(p.fecha_creacion)
    BETWEEN ? AND ?

    ORDER BY p.fecha_creacion DESC
""";

        try (
                Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, clienteId);
            ps.setString(2, fechaInicio);
            ps.setString(3, fechaFin);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ReporteProyecto r = new ReporteProyecto();

                r.setIdProyecto(rs.getInt("id"));
                r.setTitulo(rs.getString("titulo"));
                r.setEstado(rs.getString("estado"));
                r.setPresupuesto(rs.getDouble("presupuesto"));

                r.setFreelancer(
                        rs.getString("freelancer") != null
                        ? rs.getString("freelancer")
                        : "Sin freelancer"
                );

                r.setMontoContrato(
                        rs.getDouble("monto_contrato")
                );

                r.setFecha(
                        rs.getString("fecha_creacion")
                );

                lista.add(r);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public List<ReporteRecarga> obtenerHistorialRecargas(
            int usuarioId,
            String fechaInicio,
            String fechaFin
    ) {

        List<ReporteRecarga> lista
                = new ArrayList<>();

        String sql = """
        SELECT
            monto,
            fecha
        FROM recarga
        WHERE usuario_id = ?
        AND DATE(fecha)
        BETWEEN ? AND ?
        ORDER BY fecha DESC
    """;

        try (
                Connection con
                = ConexionBD.getConnection(); PreparedStatement ps
                = con.prepareStatement(sql);) {

            ps.setInt(1, usuarioId);
            ps.setString(2, fechaInicio);
            ps.setString(3, fechaFin);

            ResultSet rs
                    = ps.executeQuery();

            while (rs.next()) {

                ReporteRecarga r
                        = new ReporteRecarga();

                r.setMonto(
                        rs.getDouble("monto")
                );

                r.setFecha(
                        rs.getString("fecha")
                );

                lista.add(r);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    public List<ReporteGastoCategoria>obtenerGastosPorCategoria(int clienteId,String fechaInicio, String fechaFin) {
        List<ReporteGastoCategoria> lista = new ArrayList<>();
        String sql = """
        SELECT
            cat.nombre AS categoria,
            SUM(c.monto) AS total_gastado
        FROM proyecto p
        INNER JOIN categoria cat
            ON p.categoria_id = cat.id
        INNER JOIN propuesta pr
            ON p.id = pr.proyecto_id
        INNER JOIN contrato c
            ON pr.id = c.propuesta_id
        WHERE p.cliente_id = ?
        AND DATE(c.fecha_inicio)
        BETWEEN ? AND ?
        GROUP BY cat.nombre
        ORDER BY total_gastado DESC
    """;
        try (
                Connection con
                = ConexionBD.getConnection(); PreparedStatement ps
                = con.prepareStatement(sql);) {
            ps.setInt(1, clienteId);
            ps.setString(2, fechaInicio);
            ps.setString(3, fechaFin);
            ResultSet rs= ps.executeQuery();
            while (rs.next()) {
                ReporteGastoCategoria r = new ReporteGastoCategoria();
                r.setCategoria( rs.getString("categoria")
                );
                r.setTotalGastado(      rs.getDouble(
                                "total_gastado"
                        )
                );

                lista.add(r);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }
    public List<ReporteContratoFreelancer>
obtenerContratosFreelancer(
        int freelancerId,
        String fechaInicio,
        String fechaFin
) {

    List<ReporteContratoFreelancer> lista =
        new ArrayList<>();

    String sql = """
        SELECT

            u.nombre AS cliente,

            p.titulo AS proyecto,

            c.monto,

            c.fecha_fin,

            COALESCE(cal.estrellas, 0)
            AS calificacion

        FROM contrato c

        INNER JOIN propuesta pr
            ON c.propuesta_id = pr.id

        INNER JOIN proyecto p
            ON pr.proyecto_id = p.id

        INNER JOIN cliente cl
            ON p.cliente_id = cl.id

        INNER JOIN usuario u
            ON cl.usuario_id = u.id

        LEFT JOIN calificacion cal
            ON c.id = cal.contrato_id

        WHERE pr.freelancer_id = ?

        AND c.estado = 'COMPLETADO'

        AND DATE(c.fecha_fin)
        BETWEEN ? AND ?

        ORDER BY c.fecha_fin DESC
    """;

    try (

        Connection con =
            ConexionBD.getConnection();

        PreparedStatement ps =
            con.prepareStatement(sql);

    ) {

        ps.setInt(1, freelancerId);

        ps.setString(2, fechaInicio);

        ps.setString(3, fechaFin);

        ResultSet rs =
            ps.executeQuery();

        while (rs.next()) {

            ReporteContratoFreelancer r =
                new ReporteContratoFreelancer();

            r.setCliente(
                rs.getString("cliente")
            );

            r.setProyecto(
                rs.getString("proyecto")
            );

            r.setMonto(
                rs.getDouble("monto")
            );

            r.setCalificacion(
                rs.getInt("calificacion")
            );

            r.setFecha(
                rs.getString("fecha_fin")
            );

            lista.add(r);
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    return lista;
}

public List<TopCategoriaFreelancer>
topCategoriasFreelancer(int userId) {

    List<TopCategoriaFreelancer> lista =
        new ArrayList<>();

    String sql = """
        SELECT

            cat.nombre AS categoria,

            COUNT(c.id)
            AS cantidad_contratos,

            SUM(c.monto)
            AS total_ingresos

        FROM contrato c

        INNER JOIN propuesta p
            ON c.propuesta_id = p.id

        INNER JOIN freelancer f
            ON p.freelancer_id = f.id

        INNER JOIN proyecto pr
            ON p.proyecto_id = pr.id

        INNER JOIN categoria cat
            ON pr.categoria_id = cat.id

        WHERE f.usuario_id = ?
        AND c.estado = 'COMPLETADO'

        GROUP BY cat.id, cat.nombre

        ORDER BY cantidad_contratos DESC

        LIMIT 5
    """;

    try (

        Connection con =
            ConexionBD.getConnection();

        PreparedStatement ps =
            con.prepareStatement(sql);

    ) {

        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            TopCategoriaFreelancer t =
                new TopCategoriaFreelancer();

            t.setCategoria(
                rs.getString("categoria")
            );

            t.setCantidadContratos(
                rs.getInt("cantidad_contratos")
            );

            t.setTotalIngresos(
                rs.getDouble("total_ingresos")
            );

            lista.add(t);
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    return lista;
}
public List<ReportePropuestaFreelancer>
reportePropuestasFreelancer(
        int userId,
        String fechaInicio,
        String fechaFin
) {

    List<ReportePropuestaFreelancer> lista =
        new ArrayList<>();

    String sql = """
        SELECT

            pr.titulo AS proyecto,

            p.monto,

            p.estado,

            p.fecha

        FROM propuesta p

        INNER JOIN freelancer f
            ON p.freelancer_id = f.id

        INNER JOIN proyecto pr
            ON p.proyecto_id = pr.id

        WHERE f.usuario_id = ?

        AND DATE(p.fecha)
        BETWEEN ? AND ?

        ORDER BY p.fecha DESC
    """;

    try (

        Connection con =
            ConexionBD.getConnection();

        PreparedStatement ps =
            con.prepareStatement(sql);

    ) {

        ps.setInt(1, userId);
        ps.setString(2, fechaInicio);
        ps.setString(3, fechaFin);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            ReportePropuestaFreelancer r =
                new ReportePropuestaFreelancer();

            r.setProyecto(
                rs.getString("proyecto")
            );

            r.setMonto(
                rs.getDouble("monto")
            );

            r.setEstado(
                rs.getString("estado")
            );

            r.setFecha(
                rs.getString("fecha")
            );

            lista.add(r);
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    return lista;
}
}
