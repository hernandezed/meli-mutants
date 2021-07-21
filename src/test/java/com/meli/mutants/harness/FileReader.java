package com.meli.mutants.harness;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {
    public static String read(String path) throws IOException {
        return Files.readString(Paths.get(path));
    }
}
