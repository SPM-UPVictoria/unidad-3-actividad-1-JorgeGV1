// NaveExploracion.java
package com.astrea.core;

import java.util.Random;

public class NaveExploracion extends NaveEspacial implements Propulsable, Defendible {
    private double integridadEscudo;
    private boolean hiperviajeListo;
    private Random random = new Random(); // Para simulación probabilística

    public NaveExploracion(String matricula, String modelo, double combustibleInicial, double capacidadCombustible) throws AstreaException {
        super(matricula, modelo, combustibleInicial, capacidadCombustible);
        this.integridadEscudo = 100.0;
        this.hiperviajeListo = true;
    }

    @Override
    public void viajar(double distanciaAniosLuz) throws CombustibleInsuficienteException, AstreaException {
        if (distanciaAniosLuz <= 0) {
            throw new AstreaException("La distancia debe ser positiva.");
        }

        double consumoTotal = distanciaAniosLuz * 0.8;

        if (consumoTotal > this.combustible) {
            throw new CombustibleInsuficienteException("Combustible insuficiente para viaje de exploración.");
        }

        this.combustible -= consumoTotal;
    }

    @Override
    public void activarHiperviaje(double factorWarp) throws FallaSistemasException, CombustibleInsuficienteException {
        if (this.combustible < 50.0) {
            throw new CombustibleInsuficienteException("Se requieren al menos 50.0 unidades de combustible para el hiperviaje.");
        }

        // Simulación probabilística si Warp > 9.0 (30% de falla)
        if (factorWarp > 9.0) {
            if (random.nextDouble() < 0.3) {
                this.hiperviajeListo = false;
                throw new FallaSistemasException("Falla en el núcleo FTL por Warp extremo.");
            }
        }

        this.combustible -= 50.0;
    }

    @Override
    public void recibirImpacto(double potenciaDano) throws EscudoCriticoException {
        if (this.integridadEscudo <= 0) {
            throw new EscudoCriticoException("Incapaz de operar: Escudos colapsados.");
        }

        this.integridadEscudo -= potenciaDano;

        if (this.integridadEscudo <= 0.0) {
            this.integridadEscudo = 0.0;
            throw new EscudoCriticoException("¡Alerta! Integridad de escudo destructiva (<=0.0).");
        }
    }

    public double getIntegridadEscudo() { return integridadEscudo; }
    public boolean isHiperviajeListo() { return hiperviajeListo; }
    // Permite inyectar Random para pruebas deterministas si fuera necesario
    public void setRandom(Random random) { this.random = random; }
}
