package com.astrea.core;

public interface Propulsable {
    void activarHiperviaje(double factorWarp) throws FallaSistemasException,
            CombustibleInsuficienteException;
}
