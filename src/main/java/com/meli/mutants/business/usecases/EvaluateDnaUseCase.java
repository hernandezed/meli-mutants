package com.meli.mutants.business.usecases;

import com.meli.mutants.business.domain.SampleBO;

public interface EvaluateDnaUseCase {
    boolean execute(SampleBO sampleBO);
}
