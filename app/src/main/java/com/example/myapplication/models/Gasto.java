package com.example.myapplication.models;

import java.util.Date;

public class Gasto {
    private int id;
    private String descripcion;
    private double cantidad;
    private Date fecha;
    private String categoria;

    public Gasto(int id, String descripcion, double cantidad, Date fecha, String categoria) {
        this.id = id;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    public Gasto(String descripcion, double cantidad, Date fecha, String categoria) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getCantidad() { return cantidad; }
    public void setCantidad(double cantidad) { this.cantidad = cantidad; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return descripcion + " - " + cantidad + "€ - " + fecha.toString() + " (" + categoria + ")";
    }
}
