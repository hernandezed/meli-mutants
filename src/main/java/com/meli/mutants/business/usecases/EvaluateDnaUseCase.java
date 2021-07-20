package com.meli.mutants.business.usecases;

import com.meli.mutants.business.domain.DnaSampleBO;

public interface EvaluateDnaUseCase {
    boolean execute(DnaSampleBO dnaSampleBO);
}
