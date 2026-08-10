package com.astrea.core;

public abstract class NaveEspacial {
    protected String matricula;
    protected String modelo;
    protected double combustible;
    protected double capacidadCombustible;

    public NaveEspacial(String matricula, String modelo, double combustibleInicial, double capacidadCombustible) throws AstreaException {
        if (combustibleInicial <= 0 || capacidadCombustible <= 0) {
            throw new AstreaException("Los valores de combustible deben ser estrictamente positivos.");
        }
        if (combustibleInicial > capacidadCombustible) {
            throw new AstreaException("El combustible inicial no puede superar la capacidad máxima.");
        }
        this.matricula = matricula;
        this.modelo = modelo;
        this.combustible = combustibleInicial;
        this.capacidadCombustible = capacidadCombustible;
    }

    public void repostarCombustible(double cantidad) throws AstreaException {
        if (cantidad <= 0) {
            throw new AstreaException("La cantidad a repostar debe ser positiva.");
        }
        if (this.combustible + cantidad > this.capacidadCombustible) {
            throw new AstreaException("El repostaje excede la capacidad máxima del tanque.");
        }
        this.combustible += cantidad;
    }

    public String getMatricula() { return matricula; }
    public String getModelo() { return modelo; }
    public double getCombustible() { return combustible; }
    public double getCapacidadCombustible() { return capacidadCombustible; }

    public abstract void viajar(double distanciaAniosLuz) throws CombustibleInsuficienteException, AstreaException;

}