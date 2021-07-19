package com.meli.mutants.data_access.repositories.dna_result;

import com.meli.mutants.data_access.repositories.dna_result.entities.DnaResult;

public interface DnaResultRepository {
    void saveAndLog(DnaResult dnaResult);
}
