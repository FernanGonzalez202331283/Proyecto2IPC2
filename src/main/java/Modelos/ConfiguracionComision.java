/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author fernan
 */
public class ConfiguracionComision {
     private int id;
    private double porcentajeActual;

    public ConfiguracionComision() {
    }

    public ConfiguracionComision(
        int id,
        double porcentajeActual
    ) {
        this.id = id;
        this.porcentajeActual = porcentajeActual;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPorcentajeActual() {
        return porcentajeActual;
    }

    public void setPorcentajeActual(
        double porcentajeActual
    ) {
        this.porcentajeActual = porcentajeActual;
    }
}
