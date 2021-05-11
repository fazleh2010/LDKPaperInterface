/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static main.Grep.filename;

/**
 *
 * @author elahi
 */
public class Interface {

   //public static String outputDir = "../data/";
    public static String outputDir = "/var/www/html/ontologyLexicalization/data/";
    public static String javaScriptDir = "/var/www/html/ontologyLexicalization/";
    //public static String javaScriptDir ="../";
    //public static String javaScriptDir = "/var/www/html/ontologyLexicalization/";
    public static String javaScriptFileName = "table.js";


    public static void main(String str[]) throws Exception {
        Logger LOGGER = Logger.getLogger(Interface.class.getName());
        String prediction = "predict_po_for_s_given_localized_l", interestingness="Coherence",lexicalElement = "bear", parts_of_speech = "VB";
        String stringAdd = "";
        Boolean flag = false;
         
        prediction = str[0];
        interestingness = str[1];
        lexicalElement = str[2];
        
        
        System.out.println(prediction+" "+interestingness+" "+lexicalElement);
      
        if (str.length < 3) {
            throw new Exception("less number of argument!!!");
        } else //lexicalElement="russian";*/
        {   
            lexicalElement = " \"" + lexicalElement + "\" ";
            if (lexicalElement != null) {
                List<String> rows = resultStr(outputDir, lexicalElement, parts_of_speech, prediction,interestingness);
                  stringAdd=createTable(rows);
                  System.out.println("row size:"+rows.size());
                  System.out.println(stringAdd);

                //System.out.println(stringAdd);
		  if(rows.isEmpty()){
                     return;
                    }

                  stringAdd=createTable(rows);
		  System.out.println(stringAdd);
                  stringToFiles(stringAdd,javaScriptDir+"table.js");
                  
            }

        }

    }
    
    public static List<String> resultStr(String outputDir, String lexicalElement, String parts_of_speech, String prediction,String interestingness) throws Exception {
        Boolean flag = false;
        String content = "";
        lexicalElement = lexicalElement.toLowerCase().strip();
        List<String> rows=new ArrayList<String>();

        Map<Double, Set<String>> sortedLines = new TreeMap<Double, Set<String>>();
        Map<String, List<String>> lexiconDic = fileToHash(outputDir, lexicalElement, parts_of_speech,interestingness,prediction);
        
        
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

        if (flag) {
            return rows;
        }
        return new ArrayList<String>();

    }
    
     private static Map<String, List<String>> fileToHash(String outputDir, String lexicalElement, String part_of_speech,String interestingness,String prediction) throws FileNotFoundException, IOException {
        Map<String, List<String>> classNameLines = new TreeMap<String, List<String>>();
        Process process = null; String className = null,line=null;
        File folder = new File(outputDir);
        //String[] listOfFiles = folder.list();
        //System.out.println(outputDir+" prediction:"+prediction+" interestingness:"+interestingness);
        List<String> listOfFiles=getSpecificFiles(outputDir,prediction,interestingness);
       
        
        try {
            for (String fileName : listOfFiles) {
                fileName=outputDir+fileName;
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

    private static List<String> getSpecificFiles(String fileDir, String prediction,String extension) {
        List<String> selectedFiles = new ArrayList<String>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(extension)&&fileName.contains(prediction)) {
                    selectedFiles.add(fileName);
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

    private static String createTable(List<String> rows) {
         String tableStr = "$(document).ready(function() {\n"
                + "    $('#example').DataTable( {\n"
                + "        data: dataSet,\n"
                + "        columns: [\n"
                + "            { title: \"1\" },\n"
                + "            { title: \"2\" },\n"
                + "            { title: \"3\" },\n"
                + "            { title: \"4.\" },\n"
                + "            { title: \"5\" },\n"
                + "            { title: \"6\" },\n"
                + "            { title: \"7\" },\n"
                + "            { title: \"8\" },\n"
                + "            { title: \"9\" }\n"
                + "        ]\n"
                + "    } );\n"
                + "} );";
        String start = "var dataSet = [";
        String end = "];";
        String str = "";
        for (Integer index = 0; index < rows.size(); index++) {
            String row = rows.get(index);
            row = row.replace("+", "\"" + ", " + "\"");
            String line = null;
            if (index == (rows.size() - 1)) {
                line = "[" + "\"" + row + "\"" + "]" + "\n";
            } else {
                line = "[" + "\"" + row + "\"" + "]," + "\n";
            }

            str += line;

        }
        str=start + str + end+"\n"+tableStr;
        
        return str;
       
    }
    
    public static void stringToFiles(String str, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
