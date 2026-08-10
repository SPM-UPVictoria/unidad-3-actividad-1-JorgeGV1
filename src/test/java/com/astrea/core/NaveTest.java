// NaveTest.java
package com.astrea.core;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Random;

public class NaveTest {

    // --- HAPPY PATHS ---

    @Test
    public void testCreacionNaveExitosa() throws AstreaException {
        NaveCarga nave = new NaveCarga("AST-01", "Titan", 100.0, 200.0, 500.0);
        assertEquals("AST-01", nave.getMatricula());
        assertEquals(100.0, nave.getCombustible(), 0.001);
    }

    @Test
    public void testRepostarCombustibleValido() throws AstreaException {
        NaveCarga nave = new NaveCarga("AST-01", "Titan", 50.0, 200.0, 500.0);
        nave.repostarCombustible(50.0);
        assertEquals(100.0, nave.getCombustible(), 0.001);
    }

    @Test
    public void testViajeNaveCargaConsumoEstandar() throws AstreaException {
        NaveCarga nave = new NaveCarga("AST-01", "Titan", 100.0, 200.0, 500.0);
        nave.viajar(10.0); // 10 * 1.5 = 15.0 de consumo
        assertEquals(85.0, nave.getCombustible(), 0.001);
    }

    @Test
    public void testHiperviajeExitoso() throws AstreaException {
        NaveExploracion nave = new NaveExploracion("EXP-01", "Voyager", 100.0, 200.0);
        nave.activarHiperviaje(5.0);
        assertEquals(50.0, nave.getCombustible(), 0.001);
    }

    // --- EDGE CASES ---

    @Test
    public void testViajeNaveCargaConsumoSobrecarga() throws AstreaException {
        NaveCarga nave = new NaveCarga("AST-01", "Titan", 100.0, 200.0, 1000.0);
        nave.cargar(600.0); // > 50% de carga máxima
        nave.viajar(10.0);  // Consumo duplicado: 10 * 3.0 = 30.0
        assertEquals(70.0, nave.getCombustible(), 0.001);
    }

    @Test(expected = FallaSistemasException.class)
    public void testHiperviajeFallaProbabilisticaWarpExtremo() throws AstreaException {
        NaveExploracion nave = new NaveExploracion("EXP-01", "Voyager", 100.0, 200.0);

        // Simular que el valor de probabilidad siempre falla (fuerza la falla del 30%)
        nave.setRandom(new Random() {
            @Override
            public double nextDouble() {
                return 0.1; // Menor que 0.3, detona la excepción
            }
        });

        nave.activarHiperviaje(9.5); // Warp > 9.0
    }

    // --- NEGATIVE PATHS & ATOMICIDAD ---

    @Test(expected = CombustibleInsuficienteException.class)
    public void testViajeSinCombustibleSuficienteExcepcion() throws AstreaException {
        NaveCarga nave = new NaveCarga("AST-01", "Titan", 10.0, 200.0, 500.0);
        nave.viajar(20.0); // Requiere 30.0, solo tiene 10.0
    }

    @Test
    public void testAtomicidadEstadoAlFallarViaje() throws AstreaException {
        NaveCarga nave = new NaveCarga("AST-01", "Titan", 10.0, 200.0, 500.0);
        try {
            nave.viajar(20.0); // Falla por insuficiente combustible
            fail("Debió lanzar CombustibleInsuficienteException");
        } catch (CombustibleInsuficienteException e) {
            // Verificación del Principio de Atomicidad: el combustible se mantiene intacto
            assertEquals(10.0, nave.getCombustible(), 0.001);
        }
    }

    @Test(expected = AstreaException.class)
    public void testExcesoCapacidadRepostajeExcepcion() throws AstreaException {
        NaveCarga nave = new NaveCarga("AST-01", "Titan", 150.0, 200.0, 500.0);
        nave.repostarCombustible(100.0); // 150 + 100 > 200 (Excede)
    }

    @Test(expected = EscudoCriticoException.class)
    public void testEscudoCriticoTrasDanoDestructivo() throws AstreaException {
        NaveExploracion nave = new NaveExploracion("EXP-01", "Voyager", 100.0, 200.0);
        nave.recibirImpacto(150.0); // Daño mayor al escudo de 100.0
    }
}
