/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;

import DAO.CategoriaDAO;
import Modelos.Categoria;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author fernan
 */
@WebServlet("/categorias")
public class CategoriaServlet extends HttpServlet{
    
     @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            CategoriaDAO dao = new CategoriaDAO();
            List<Categoria> lista = dao.listarCategorias();

            Gson gson = new Gson();
            resp.getWriter().write(gson.toJson(lista));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
        }
    }
}
