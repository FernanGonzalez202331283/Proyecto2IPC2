/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author fernan
 */
public class ReporteIngresos {
    private int totalContratos;
    private double totalComisiones;

    public ReporteIngresos(int totalContratos, double totalComisiones) {
        this.totalContratos = totalContratos;
        this.totalComisiones = totalComisiones;
    }

    public int getTotalContratos() {
        return totalContratos;
    }

    public double getTotalComisiones() {
        return totalComisiones;
    }
}
