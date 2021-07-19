package com.meli.mutants.data_access.repositories.dna_result.entities;

public enum DnaResultType {
    MUTANT,
    HUMAN;

    public static DnaResultType valueOf(boolean result) {
        if (result) {
            return MUTANT;
        }
        return HUMAN;
    }
}
