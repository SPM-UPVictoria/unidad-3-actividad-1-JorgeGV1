package com.astrea.core;

public interface Defendible {
    void recibirImpacto(double potenciaDano) throws EscudoCriticoException;
}