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

            String sql
                    = "SELECT id, nombre, estado FROM categoria";

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Categoria c = new Categoria();

                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));
                c.setEstado(rs.getInt("estado"));

                lista.add(c);

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return lista;
    }

    public boolean crearCategoria(String nombre) {

        String sql
                = "INSERT INTO categoria(nombre, estado) VALUES(?,1)";

        try (
                Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);

            ps.executeUpdate();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public boolean editarCategoria(int id, String nombre) {

        String sql
                = "UPDATE categoria SET nombre=? WHERE id=?";

        try (
                Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, id);

            ps.executeUpdate();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public boolean cambiarEstado(int id, int estado) {

        String sql
                = "UPDATE categoria SET estado=? WHERE id=?";

        try (
                Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, estado);
            ps.setInt(2, id);

            ps.executeUpdate();

            return true;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }
}
