/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelos;

import java.sql.Date;

/**
 *
 * @author fernan
 */
public class Propuesta {
    private int id;
    private int proyectoId;
    private int freelancerId;
    private double monto;
    private int tiempo;
    private String descripcion;
    private String estado;
    private Date fecha;
    
    public Propuesta(){}
    
    public Propuesta(int proyectoId, int freelancerId, double monto, int tiempo, String descripcion) {
        this.proyectoId = proyectoId;
        this.freelancerId = freelancerId;
        this.monto = monto;
        this.tiempo = tiempo;
        this.descripcion = descripcion;
    }
    
    
    public int getId() {
        return id;
    }

    public int getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(int freelancerId) {
        this.freelancerId = freelancerId;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
    
    
}
