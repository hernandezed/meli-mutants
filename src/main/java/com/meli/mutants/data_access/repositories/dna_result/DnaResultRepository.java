package com.meli.mutants.data_access.repositories.dna_result;

import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;

public interface DnaResultRepository {
    void save(DnaResult dnaResult);

    Long count(DnaResultType dnaResultType);
}
