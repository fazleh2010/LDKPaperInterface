/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author elahi
 */
public class Result {

    private List<String> rows = new ArrayList<String>();
    private String content = "";
    private Boolean flag = false;
    private static String seperator = "+";

    public void resultStr(String outputDir, String lexicalElement, String parts_of_speech, String prediction, String interestingness) throws Exception {
        lexicalElement = lexicalElement.toLowerCase().strip();

        Map<Double, Set<String>> sortedLines = new TreeMap<Double, Set<String>>();
        Map<String, List<String>> lexiconDic = fileToHash(outputDir, lexicalElement, parts_of_speech, interestingness, prediction);

        for (String key : lexiconDic.keySet()) {
            flag = true;
            List<String> LineInfos = lexiconDic.get(key);
            for (String lineinfo : LineInfos) {
                String doubleValue = lineinfo.split(",")[2];
                doubleValue = doubleValue.replace("\"", "");
                Double value = Double.parseDouble(doubleValue);
                Set<String> tempList = new HashSet<String>();
                if (sortedLines.containsKey(value)) {
                    tempList = sortedLines.get(value);
                    tempList.add(lineinfo);
                    sortedLines.put(value, tempList);
                } else {
                    tempList.add(lineinfo);
                    sortedLines.put(value, tempList);
                }
            }
        }

        content = "";
        List<Double> keyValues = new ArrayList<Double>(sortedLines.keySet());
        Collections.sort(keyValues, Collections.reverseOrder());
        Integer index = 0;
        for (Double value : keyValues) {
            index = index + 1;
            Set<String> stringList = sortedLines.get(value);
            for (String string : stringList) {
                string = modifyLine(string, parts_of_speech, prediction);
                String line = string + "\n";
                rows.add(string);
                content += line;

            }
        }

    }

    private Map<String, List<String>> fileToHash(String outputDir, String lexicalElement, String part_of_speech, String interestingness, String prediction) throws FileNotFoundException, IOException {
        Map<String, List<String>> classNameLines = new TreeMap<String, List<String>>();
        Process process = null;
        String className = null, line = null;
        File folder = new File(outputDir);
        //String[] listOfFiles = folder.list();
        //System.out.println(outputDir+" prediction:"+prediction+" interestingness:"+interestingness);
        List<String> listOfFiles = FileUtils.getSpecificFiles(outputDir, prediction, interestingness);
        if (listOfFiles.isEmpty()) {
            return new TreeMap<String, List<String>>();
        }

        try {
            for (String fileName : listOfFiles) {
                fileName = outputDir + fileName;
                String command = "grep -w " + lexicalElement + " " + fileName;
                process = Runtime.getRuntime().exec(command);
                List<String> lines = new ArrayList<String>();
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = br.readLine()) != null) {
                    if (line.contains("XMLSchema#integer")) {
                        continue;
                    }
                    className = line.split(",")[4];
                    lines.add(line);

                }
                classNameLines.put(className, lines);
            }

            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();

        }

        return classNameLines;
    }

    private static String modifyLine(String line, String part_of_speech, String prediction) {
        String rankLine = line;
        if (line.contains(",")) {
            String[] info = line.split(",");
            rankLine = addQuote(getValue(info[8])) + seperator
                    + addQuote(getValue(info[9])) + seperator
                    + addQuote(checkLabel(getValue(info[10]))) + seperator
                    + addQuote(getValue(info[4])) + seperator;
            //System.out.println("info[10]::"+info[10]);

            List<String> list = getValueSpace(info[5]);
            for (String value : list) {
                rankLine +=  addQuote(value) + seperator;
            }
            if (part_of_speech.contains("JJ")) {
                part_of_speech = "adjective";
            } else if (part_of_speech.contains("NN")) {
                part_of_speech = "noun";
            } else if (part_of_speech.contains("VB")){
                part_of_speech = "verb";
            }
            else
                 part_of_speech = "Not found";
            rankLine += addQuote(part_of_speech )+ seperator + addQuote(prediction);
            //System.out.println(rankLine);
            //rankLine = rankLine.replace("\"", "");

        }
        return rankLine;
    }

    /*private static String modifyLine(String line, String part_of_speech, String prediction) {
        String rankLine = line;
        if (line.contains(",")) {
            String[] info = line.split(",");
            String lexicalElement = info[0];
            lexicalElement = lexicalElement.replace("\"", "");
            rankLine = getValue(info[1]) + "+" + getValue(info[4]) + "+";
            List<String> list = getValueSpace(info[5]);
            for (String value : list) {
                rankLine += value + "+";
            }
            rankLine += part_of_speech + "+" + prediction;
            //System.out.println(rankLine);
            rankLine = rankLine.replace("\"", "");

        }
        return rankLine;
    }*/
   /* private static String getValueQuoteUnchanged(String string) {
        if (string.contains("=")) {
            String[] info = string.split("=");
            string = string.replace("\"", "");
            if(string.contains("@"))
               return info[1];
           
        }

        return string;
    }*/
    private static String getValue(String string) {

        string = string.replace("\"", "");

        if (string.contains("=")) {
            String[] info = string.split("=");
            return info[1];
        }

        return string.strip().stripLeading().stripTrailing();
    }
    
    private static String checkLabel(String string) {
        if (string.contains("@")) {
            String[] info = string.split("@");
            string=addQuote(info[0])+"@"+info[1];
            return string;
        }

        return string;
    }
     private static String addQuote(String string) {
        return "\""+string+"\"";
        //return string;
    }

    private static List<String> getValueSpace(String string) {
        List<String> arrayList = new ArrayList<String>();
        string = string.replace("\"", "");
        String[] info = string.split(" ");
        for (String key : info) {
            if (key.isEmpty()) {
                continue;
            }
            key = getValue(key);
            if (key.length() >= 4) {
                key = key.substring(0, 4);

            }
            arrayList.add(key);
        }
        return arrayList;
    }

    public Result() {
    }

    public List<String> getRows() {
        return rows;
    }

    public String getContent() {
        return content;
    }

}
