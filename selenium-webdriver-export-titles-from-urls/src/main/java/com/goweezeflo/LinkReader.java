package com.goweezeflo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LinkReader {
    private final List<String> links;

    public LinkReader(String fileName) {
        File linksFile = new File(Objects.requireNonNull(
            Main.class.getClassLoader()
                .getResource(fileName)
        ).getFile());

        List<String> links = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(
            new FileReader(linksFile.getAbsolutePath())
        )) {
            String link;
            while ((link = br.readLine()) != null) {
                links.add(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.links = links;
    }

    public List<String> getLinks() {
        return links;
    }
}
