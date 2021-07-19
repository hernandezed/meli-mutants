package com.meli.mutants.data_access.repositories.dna_result.entities;

import lombok.Value;

import java.io.Serializable;

@Value
public class DnaResult implements Serializable {

    public static final Long serialVersionUID = 1L;

    String[] dna;
    DnaResultType result;
}
