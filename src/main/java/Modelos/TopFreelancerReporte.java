/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author fernan
 */
public class TopFreelancerReporte {
     private String nombre;

    private int contratosCompletados;

    private double totalGenerado;

    private double comisionPlataforma;

    public TopFreelancerReporte() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getContratosCompletados() {
        return contratosCompletados;
    }

    public void setContratosCompletados(
        int contratosCompletados
    ) {
        this.contratosCompletados =
            contratosCompletados;
    }

    public double getTotalGenerado() {
        return totalGenerado;
    }

    public void setTotalGenerado(
        double totalGenerado
    ) {
        this.totalGenerado =
            totalGenerado;
    }

    public double getComisionPlataforma() {
        return comisionPlataforma;
    }

    public void setComisionPlataforma(
        double comisionPlataforma
    ) {
        this.comisionPlataforma =
            comisionPlataforma;
    }
}
