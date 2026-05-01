/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author fernan
 */
public class UsuarioDAO {
     public Usuario login(String username, String password) {
        System.out.println("Intentando login para: " + username);
        Usuario u = null;

        String sql = "SELECT * FROM usuario WHERE username=? AND estado=1";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    String hashed = rs.getString("password");

                    if (BCrypt.checkpw(password, hashed)) {
                        System.out.println("Login exitoso!");

                        u = new Usuario();
                        u.setId(rs.getInt("id"));
                        u.setNombre(rs.getString("nombre"));
                        u.setUsername(rs.getString("username"));
                        u.setRol(rs.getString("rol"));
                        u.setEstado(rs.getInt("estado"));
                        u.setPerfilCompleto(rs.getInt("perfil_completo"));

                    } else {
                        System.out.println("Password incorrecto");
                    }

                } else {
                    System.out.println("Usuario no encontrado");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return u;
    }

    public boolean registrar(Usuario u) {
        String sql = "INSERT INTO usuario (nombre, username, password, correo, telefono, " +
                     "direccion, cui, fecha_nacimiento, rol, perfil_completo, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsername());

            // 🔥 HASH SOLO AQUÍ
            String hash = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
            ps.setString(3, hash);

            ps.setString(4, u.getCorreo());
            ps.setString(5, u.getTelefono());
            ps.setString(6, u.getDireccion());
            ps.setString(7, u.getCui());
            ps.setDate(8, java.sql.Date.valueOf(u.getFechaNacimiento()));
            ps.setString(9, u.getRol().toUpperCase());
            ps.setInt(10, u.getPerfilCompleto());
            ps.setInt(11, 1);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existeUsuario(String username) {
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT id FROM usuario WHERE username = ?")) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
