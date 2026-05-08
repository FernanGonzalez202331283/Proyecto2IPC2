/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import Conexion.ConexionBD;
import Modelos.Cliente;
import Modelos.Freelancer;
import Modelos.PerfilUsuario;
import Modelos.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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
        String sql = "INSERT INTO usuario (nombre, username, password, correo, telefono, "
                + "direccion, cui, fecha_nacimiento, rol, perfil_completo, estado) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getUsername());
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
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(
                "SELECT id FROM usuario WHERE username = ?")) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Usuario> listarUsuarios() {

        List<Usuario> lista = new ArrayList<>();

        String sql = "SELECT id, nombre, username, correo, rol, estado FROM usuario";

        try (Connection con = Conexion.ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Usuario u = new Usuario();

                u.setId(rs.getInt("id"));
                u.setNombre(rs.getString("nombre"));
                u.setUsername(rs.getString("username"));
                u.setCorreo(rs.getString("correo"));
                u.setRol(rs.getString("rol"));
                u.setEstado(rs.getInt("estado"));

                lista.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean cambiarEstado(int id, int estado) {

        String sql = "UPDATE usuario SET estado = ? WHERE id = ?";

        try (Connection con = Conexion.ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, estado);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PerfilUsuario obtenerPerfil(int id) {

        PerfilUsuario perfil = new PerfilUsuario();

        Usuario usuario = null;

        Freelancer freelancer = null;

        Cliente cliente = null;

        String sqlUsuario
                = "SELECT * FROM usuario WHERE id = ?";

        String sqlFreelancer
                = "SELECT * FROM freelancer WHERE usuario_id = ?";

        String sqlCliente
                = "SELECT * FROM cliente WHERE usuario_id = ?";

        try (
                Connection con = ConexionBD.getConnection()) {

            try (
                    PreparedStatement ps
                    = con.prepareStatement(sqlUsuario)) {

                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    usuario = new Usuario();

                    usuario.setId(
                            rs.getInt("id")
                    );

                    usuario.setNombre(
                            rs.getString("nombre")
                    );

                    usuario.setUsername(
                            rs.getString("username")
                    );

                    usuario.setCorreo(
                            rs.getString("correo")
                    );

                    usuario.setTelefono(
                            rs.getString("telefono")
                    );

                    usuario.setDireccion(
                            rs.getString("direccion")
                    );

                    usuario.setCui(
                            rs.getString("cui")
                    );

                    usuario.setFechaNacimiento(
                            rs.getString("fecha_nacimiento")
                    );

                    usuario.setRol(
                            rs.getString("rol")
                    );

                    usuario.setEstado(
                            rs.getInt("estado")
                    );
                }
            }

            try (
                    PreparedStatement ps
                    = con.prepareStatement(sqlFreelancer)) {

                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    freelancer = new Freelancer();

                    freelancer.setIdUsuario(
                            rs.getInt("usuario_id")
                    );

                    freelancer.setBiografia(
                            rs.getString("biografia")
                    );

                    freelancer.setNivelExperiencia(
                            rs.getString("nivel")
                    );

                    freelancer.setTarifaHora(
                            rs.getDouble("tarifa")
                    );

                    freelancer.setPerfilCompleto(
                            rs.getInt("perfil_completo")
                    );
                }
            }
            try (
                    PreparedStatement ps
                    = con.prepareStatement(sqlCliente)) {

                ps.setInt(1, id);

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {

                    cliente = new Cliente();

                    cliente.setId(
                            rs.getInt("id")
                    );

                    cliente.setUsuarioId(
                            rs.getInt("usuario_id")
                    );

                    cliente.setDescripcion(
                            rs.getString("descripcion")
                    );

                    cliente.setSector(
                            rs.getString("sector")
                    );

                    cliente.setSitioWeb(
                            rs.getString("sitio_web")
                    );
                }
            }

            perfil.setUsuario(usuario);

            perfil.setFreelancer(freelancer);

            perfil.setCliente(cliente);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return perfil;
    }

    public boolean crearAdmin(Usuario u) {

        String sql
                = "INSERT INTO usuario "
                + "(nombre, username, password, correo, rol, estado) "
                + "VALUES (?, ?, ?, ?, 'ADMIN', 1)";

        try (
                Connection con = ConexionBD.getConnection(); PreparedStatement ps
                = con.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());

            ps.setString(2, u.getUsername());

            String hash
                    = BCrypt.hashpw(
                            u.getPassword(),
                            BCrypt.gensalt()
                    );

            ps.setString(3, hash);

            ps.setString(4, u.getCorreo());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

}
