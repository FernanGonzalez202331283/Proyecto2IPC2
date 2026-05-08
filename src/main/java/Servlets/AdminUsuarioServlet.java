/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.UsuarioDAO;
import Modelos.PerfilUsuario;
import Modelos.Usuario;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 * @author fernan
 */
@WebServlet("/admin/usuarios")
public class AdminUsuarioServlet extends HttpServlet{
    
   @Override
    protected void doGet(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws IOException {

        resp.setContentType("application/json");

        UsuarioDAO dao = new UsuarioDAO();

        Gson gson = new Gson();

        try {

            String id =
                req.getParameter("id");

            // =========================
            // VER PERFIL
            // =========================

            if(id != null){

                PerfilUsuario perfil =
                    dao.obtenerPerfil(
                        Integer.parseInt(id)
                    );

                resp.getWriter().write(
                    gson.toJson(perfil)
                );

            }

            // =========================
            // LISTAR USUARIOS
            // =========================

            else{

                resp.getWriter().write(
                    gson.toJson(
                        dao.listarUsuarios()
                    )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            resp.setStatus(500);

            resp.getWriter().write(
                "{\"error\":\"Error servidor\"}"
            );
        }
    }

    // =========================
    // PUT
    // ACTIVAR/DESACTIVAR
    // =========================

    @Override
    protected void doPut(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws IOException {

        resp.setContentType("application/json");

        try {

            Gson gson = new Gson();

            Usuario u =
                gson.fromJson(
                    req.getReader(),
                    Usuario.class
                );

            UsuarioDAO dao = new UsuarioDAO();

            boolean ok =
                dao.cambiarEstado(
                    u.getId(),
                    u.getEstado()
                );

            if (ok) {

                resp.getWriter().write(
                    "{\"msg\":\"Estado actualizado\"}"
                );

            } else {

                resp.setStatus(400);

                resp.getWriter().write(
                    "{\"error\":\"No se pudo actualizar\"}"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            resp.setStatus(500);

            resp.getWriter().write(
                "{\"error\":\"Error servidor\"}"
            );
        }
    }

    // =========================
    // POST
    // CREAR ADMIN
    // =========================

    @Override
    protected void doPost(
        HttpServletRequest req,
        HttpServletResponse resp
    ) throws IOException {

        resp.setContentType("application/json");

        try {

            Gson gson = new Gson();

            Usuario u =
                gson.fromJson(
                    req.getReader(),
                    Usuario.class
                );

            UsuarioDAO dao = new UsuarioDAO();

            boolean ok =
                dao.crearAdmin(u);

            if(ok){

                resp.getWriter().write(
                    "{\"msg\":\"Administrador creado\"}"
                );

            } else {

                resp.setStatus(400);

                resp.getWriter().write(
                    "{\"error\":\"No se pudo crear\"}"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            resp.setStatus(500);

            resp.getWriter().write(
                "{\"error\":\"Error servidor\"}"
            );
        }
    }
}
