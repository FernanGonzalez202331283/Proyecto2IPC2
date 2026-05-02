/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.Categoria;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fernan
 */
public class CategoriaDAO {
     public List<Categoria> listarCategorias() {

        List<Categoria> lista = new ArrayList<>();

        try {
            Connection con = ConexionBD.getConnection();
            String sql = "SELECT id, nombre FROM categoria WHERE estado = 1";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                lista.add(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
