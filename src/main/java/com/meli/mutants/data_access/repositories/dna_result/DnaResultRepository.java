package com.meli.mutants.data_access.repositories.dna_result;

import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;
import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResultType;

public interface DnaResultRepository {
    void saveAndLog(DnaResult dnaResult);

    long count(DnaResultType dnaResultType);
}
