package com.meli.mutants.data_access.repositories.dna_result.settings;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mutants.data-access")
@Setter
public class DnaResultPrefixSettings {
    String dnaEntriesKeyPrefix;
    String dnaHllKeyPrefix;
    String dnaCounterKeyPrefix;

    public String getEntryKey(String sufix) {
        return dnaEntriesKeyPrefix + sufix;
    }

    public String getHllKey(String sufix) {
        return dnaHllKeyPrefix + sufix;
    }

    public String dnaCounterKey(String sufix) {
        return dnaCounterKeyPrefix + sufix;
    }
}
