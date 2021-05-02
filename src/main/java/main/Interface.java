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
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import static main.Grep.filename;

/**
 *
 * @author elahi
 */
public class Interface {

    public static String qald9Dir = "/home/elahi/new/LDKPaperInterface/src/main/resources/qald9/data/";
    public static String predict_po_for_s_given_localized_l = "predict_po_for_s_given_localized_l/";
    public static String filename = "JJ-rules-predict_po_for_s_given_localized_l-AcademicJournal-100-10000-10-4-5-5-100-5-5-5.csv";
     

    public static void main(String str[]) throws Exception {
        Logger LOGGER = Logger.getLogger(Interface.class.getName());
        String prediction = "predict_po_for_s_given_localized_l", lexicalElement = "russian", parts_of_speech = "JJ";
        String outputDir = qald9Dir + "/" + prediction + "/" + "dic/";
        String stringAdd = "Test";
        Boolean flag = false;
         
        prediction = str[0];
        lexicalElement = str[1];
        if (str.length < 2) {
            throw new Exception("less number of argument!!!");
        } else //lexicalElement="russian";
        {
            if (lexicalElement != null) {
                lexicalElement = lexicalElement.toLowerCase().replace(" ", "_").strip();
                stringAdd = resultStr(outputDir, lexicalElement, parts_of_speech, prediction);
                System.out.println(stringAdd);
            }

        }

    }
    
    public static String resultStr(String outputDir, String lexicalElement, String parts_of_speech, String prediction) throws Exception {
        Boolean flag = false;
        String content = "";
        Map<Double, Set<String>> sortedLines = new TreeMap<Double, Set<String>>();
        lexicalElement = " \"" + lexicalElement + "\" ";
        Map<String, List<String>> lexiconDic = fileToHash(outputDir, lexicalElement, parts_of_speech);
        
        
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
        // System.out.println("sortedLines:"+sortedLines.keySet());
        List<Double> keyValues = new ArrayList<Double>(sortedLines.keySet());
        Collections.sort(keyValues, Collections.reverseOrder());
        Integer index = 0;
        for (Double value : keyValues) {
            index = index + 1;
            Set<String> stringList = sortedLines.get(value);
            for (String string : stringList) {
                string = modifyLine(string, parts_of_speech, prediction);
                //System.out.println("value:"+value);
                String line = string + "\n";
                //System.out.println(string);
                content += line;

            }
        }

        if (flag) {
            return content;
        }
        return "No answer found";

    }
    
     private static Map<String, List<String>> fileToHash(String outputDir, String lexicalElement, String part_of_speech) throws FileNotFoundException, IOException {
        Map<String, List<String>> classNameLines = new TreeMap<String, List<String>>();
        Process process = null; String className = null,line=null;
        File folder = new File(outputDir);
        String[] listOfFiles = folder.list();
        
        try {
            for (String fileName : listOfFiles) {
                fileName=outputDir+fileName;
                String command = "grep -w " + lexicalElement + " " + fileName;
                //System.out.println("command:"+command);
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

    private static String modifyLine(String line, String part_of_speech,String prediction) {
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
            rankLine += part_of_speech+ "+"+prediction;
            //System.out.println(rankLine);
            rankLine = rankLine.replace("\"", "");

        }
        return rankLine;
    }

    private static List<File> getSpecificFiles(String fileDir, String extension) {
        List<File> selectedFiles = new ArrayList<File>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(extension)) {
                    selectedFiles.add(new File(fileDir + fileName));
                }
            }

        } catch (Exception exp) {
            System.out.println("file not found!!");
            return selectedFiles;
        }

        return selectedFiles;
    }
   
    private static String getValue(String string) {
        string = string.replace("\"", "");

        if (string.contains("=")) {
            String[] info = string.split("=");
            return info[1];
        }

        return string;
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

}
