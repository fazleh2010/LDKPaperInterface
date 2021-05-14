/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import static main.Interface.adjective;
import static main.Interface.noun;
import static main.Interface.outputDir;
import static main.Interface.verb;

/**
 *
 * @author elahi
 */
public class CheckPosTag {

    private Boolean found = false;
    private String posTag = null;

    public CheckPosTag(String outputDir, String prediction, String interestingness, String lexicalElement) throws IOException {
        List<File> listOfFiles = FileUtils.getSpecificFiles(outputDir, prediction, interestingness, "z_", ".txt");

        for (File file : listOfFiles) {
            if (file.getName().contains("JJ")) {
                adjective = getList(file);
            }
            if (file.getName().contains("NN")) {
                noun = getList(file);
            }
            if (file.getName().contains("VB")) {
                verb = getList(file);
            }
        }

        findPosTag(lexicalElement);

    }

    private void findPosTag(String lexicalElement) {
        lexicalElement=lexicalElement.replace("\"","");
        lexicalElement = lexicalElement.toLowerCase().strip().replace(" ", "_");
        if (adjective.contains(lexicalElement)) {
            posTag = "JJ";
            found = true;
        } else if (noun.contains(lexicalElement)) {
            posTag = "NN";
            found = true;
        } else if (verb.contains(lexicalElement)) {
            posTag = "VB";
            found = true;
        }

    }
    
    private  Set<String> getList(File fileName) throws FileNotFoundException, IOException {
        Set<String> entities = new TreeSet<String>();

        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();

            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    String url = line.toLowerCase().strip().replace(" ", "_").trim();
                    entities.add(url);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entities;
    }

    public Boolean getFound() {
        return found;
    }

    public String getPosTag() {
        return posTag;
    }

}
