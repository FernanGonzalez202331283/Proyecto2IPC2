/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author fernan
 */
public class TopCategoriaReporte {
    private String categoria;

    private int cantidadContratos;

    private double totalComisiones;

    public TopCategoriaReporte() {
    }

    public TopCategoriaReporte(
        String categoria,
        int cantidadContratos,
        double totalComisiones
    ) {
        this.categoria = categoria;
        this.cantidadContratos = cantidadContratos;
        this.totalComisiones = totalComisiones;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getCantidadContratos() {
        return cantidadContratos;
    }

    public void setCantidadContratos(
        int cantidadContratos
    ) {
        this.cantidadContratos =
            cantidadContratos;
    }

    public double getTotalComisiones() {
        return totalComisiones;
    }

    public void setTotalComisiones(
        double totalComisiones
    ) {
        this.totalComisiones =
            totalComisiones;
    }
}
