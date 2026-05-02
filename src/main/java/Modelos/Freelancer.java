/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

/**
 *
 * @author fernan
 */
public class Freelancer {
    private int idUsuario;
    private String nivelExperiencia;
    private double tarifaHora;
    private int perfilCompleto;
    private int [] habilidades;
    private String biografia;

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }
    
    
    public int getIdUsuario() {
        return idUsuario;
    }

    public int[] getHabilidades() {
        return habilidades;
    }

    public void setHabilidades(int[] habilidades) {
        this.habilidades = habilidades;
    }
    
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNivelExperiencia() {
        return nivelExperiencia;
    }

    public void setNivelExperiencia(String nivelExperiencia) {
        this.nivelExperiencia = nivelExperiencia;
    }

    public double getTarifaHora() {
        return tarifaHora;
    }

    public void setTarifaHora(double tarifaHora) {
        this.tarifaHora = tarifaHora;
    }    
    public int getPerfilCompleto() {
        return perfilCompleto;
    }

    public void setPerfilCompleto(int perfilCompleto) {
        this.perfilCompleto = perfilCompleto;
    }
    
    
}
