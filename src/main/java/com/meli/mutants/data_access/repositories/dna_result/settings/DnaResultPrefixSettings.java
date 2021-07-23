package com.meli.mutants.data_access.repositories.dna_result.settings;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mutants.data-access")
@Setter
public class DnaResultPrefixSettings {
    String dnaEntriesKeyPrefix;

    public String getEntryKey(String sufix) {
        return dnaEntriesKeyPrefix + sufix;
    }


}
