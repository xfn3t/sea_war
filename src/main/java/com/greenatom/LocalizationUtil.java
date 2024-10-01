package com.greenatom;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class LocalizationUtil {

    private static String basePath = "resources\\lang\\";
    public static String loadFile(String locale, String fileName) {
        StringBuilder content = new StringBuilder();
        String filePath = Paths.get(basePath, locale, fileName).toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return content.toString();
    }
}
