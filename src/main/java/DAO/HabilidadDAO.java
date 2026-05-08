/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.Habilidad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class HabilidadDAO {

    public List<Habilidad> listar() {

        List<Habilidad> lista = new ArrayList<>();

        try {
            Connection con = ConexionBD.getConnection();

            String sql = "SELECT id, nombre FROM habilidad WHERE estado = 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Habilidad h = new Habilidad();
                h.setId(rs.getInt("id"));
                h.setNombre(rs.getString("nombre"));
                lista.add(h);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
