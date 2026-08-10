// NaveCarga.java
package com.astrea.core;

public class NaveCarga extends NaveEspacial {
    private double cargaActual;
    private double cargaMaxima;

    public NaveCarga(String matricula, String modelo, double combustibleInicial, double capacidadCombustible, double cargaMaxima) throws AstreaException {
        super(matricula, modelo, combustibleInicial, capacidadCombustible);
        if (cargaMaxima <= 0) {
            throw new AstreaException("La carga máxima debe ser mayor a cero.");
        }
        this.cargaMaxima = cargaMaxima;
        this.cargaActual = 0.0;
    }

    public void cargar(double peso) throws AstreaException {
        if (peso <= 0 || this.cargaActual + peso > cargaMaxima) {
            throw new AstreaException("Exceso de carga o peso no válido.");
        }
        this.cargaActual += peso;
    }

    @Override
    public void viajar(double distanciaAniosLuz) throws CombustibleInsuficienteException, AstreaException {
        if (distanciaAniosLuz <= 0) {
            throw new AstreaException("La distancia debe ser positiva.");
        }

        double consumoBase = 1.5;
        if (this.cargaActual > (this.cargaMaxima * 0.5)) {
            consumoBase = 3.0; // Consumo se duplica por sobrecarga de inercia
        }

        double consumoTotal = distanciaAniosLuz * consumoBase;

        if (consumoTotal > this.combustible) {
            throw new CombustibleInsuficienteException("Combustible insuficiente para el viaje. Atomicidad preservada.");
        }

        this.combustible -= consumoTotal;
    }

    public double getCargaActual() { return cargaActual; }
    public double getCargaMaxima() { return cargaMaxima; }
}