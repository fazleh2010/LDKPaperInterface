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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class Interface {

    public static String qald9Dir = "/home/elahi/new/RecentOntoLexShort/src/main/resources/qald9/data/";

    public static void main(String str[]) throws Exception {
        Logger LOGGER = Logger.getLogger(Interface.class.getName());
        String prediction = "predict_po_for_s_given_localized_l", lexicalElement = "russian", parts_of_speech = "JJ";
        String outputDir = qald9Dir + "/" + prediction + "/" + "dic/";

        String stringAdd = "Test";
        Boolean flag = false;
        /*Map<String,String> lexicalPos=new TreeMap<String, String>();
        lexicalPos.put("russian", "JJ");*/

        prediction = str[0];
        lexicalElement = str[1];
        if (str.length < 2) {
            throw new Exception("less number of argument!!!");
        } else //lexicalElement="russian";
        {
            if (lexicalElement != null) {
                lexicalElement = lexicalElement.toLowerCase().replace(" ", "_").strip();
                //parts_of_speech=lexicalPos.get(lexicalElement);
                stringAdd = resultStr(outputDir, "A_" + lexicalElement, lexicalElement, parts_of_speech);
                System.out.println(stringAdd);
            }

        }

    }

    public static String resultStr(String outputDir, String prediction, String lexicalElement, String parts_of_speech) throws Exception {
        Boolean flag = false;
        String content = "";
        List<File> files = getSpecificFiles(outputDir, prediction);

        for (File file : files) {
            String stringAdd = "";
            Map<String, List<String>> lexiconDic = fileToHash(file, lexicalElement, parts_of_speech);
            for (String key : lexiconDic.keySet()) {
                flag = true;
                List<String> LineInfos = lexiconDic.get(key);
                Map<Double, String> sortedLines = new TreeMap<Double, String>();
                for (String lineinfo : LineInfos) {
                    String doubleValue = lineinfo.split(",")[2];
                    doubleValue = doubleValue.replace("\"", "");
                    Double value = Double.parseDouble(doubleValue);
                    //System.out.println(value);
                    sortedLines.put(value, lineinfo);

                }
                List<Double> keyValues = new ArrayList<Double>(sortedLines.keySet());
                Collections.sort(keyValues, Collections.reverseOrder());
                String lines = "";
                Integer index = 0;
                for (Double value : keyValues) {
                    index = index + 1;
                    String string = sortedLines.get(value);
                    string = modifyLine(string, parts_of_speech);
                    String line = string + "\n";
                    lines += line;
                    if (index >= 10) {
                        break;
                    }

                }

                stringAdd = stringAdd + lines;
                //break;
            }
            content += stringAdd;
        }
        if (flag) {
            return content;
        } else {
            return "No answer found";
        }

    }

    private static String modifyLine(String line, String part_of_speech) {
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
            rankLine += part_of_speech;
            //System.out.println(rankLine);
            rankLine = rankLine.replace("\"", "");

        }
        return rankLine;
    }

    /*
                    if (line.contains(",")) {
                        String[] info = line.split(",");
                        String lexicalElement = info[0];
                        lexicalElement = lexicalElement.replace("\"", "");
                        System.out.println(line);
                        String rankLine = getValue(info[1]) + "+" +  getValue(info[4])+"+";
                        List<String> list=getValueSpace(info[5]);
                        for(String value:list){
                          rankLine+=  value+"+";
                        }
                        rankLine+=part_of_speech;
                        //System.out.println(rankLine);
                        rankLine = rankLine.replace("\"", "");
                        if (hash.containsKey(lexicalElement)) {
                            temp = hash.get(lexicalElement);
                        }
                        temp.add(rankLine);
                        hash.put(lexicalElement, temp);
                    }
     */
 /*for (String className : lexiconDic.keySet()) {
                 List<String> rankList = lexiconDic.get(className);
                 for(String line:rankList){
                     System.out.println(line); 
                 }
                
             }*/

 /*if (lexiconDic.containsKey(lexicalElement)) {
                flag = true;
                //stringAdd = lexicalElement + "\n";
               
                List<String> rankList = lexiconDic.get(lexicalElement);
                String lines = "";
                for (String string : rankList) {
                    System.out.println("string:"+string);
                    String line = string+"\n";
                    lines +=line;
                }
                 stringAdd= stringAdd+lines;
            }
            content = content + stringAdd;*/
 /*public static String resultStr(String outputDir, String prediction, String lexicalElement, String parts_of_speech) throws Exception {
        Boolean flag = false;
        String content = "";
        List<File> files = getSpecificFiles(outputDir, prediction);

        for (File file : files) {
            Map<String, List<String>> lexiconDic = fileToHash(file,lexicalElement,parts_of_speech);            
            String stringAdd="";
            if (lexiconDic.containsKey(lexicalElement)) {
                flag = true;
                //stringAdd = lexicalElement + "\n";
               
                List<String> rankList = lexiconDic.get(lexicalElement);
                String lines = "";
                for (String string : rankList) {
                    String line = string+"\n";
                    lines +=line;
                }
                 stringAdd= stringAdd+lines;
            }
            content=content+stringAdd;
            

        }
        if(flag)
            return content;
        else
            return "No answer found";
       

    }*/

 /* private static List<File> getSpecificFiles(String fileDir, String category, String extension) {
        List<File> selectedFiles = new ArrayList<File>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(category) && fileName.contains(extension)) {
                    selectedFiles.add(new File(fileDir + fileName));
                }
            }

        } catch (Exception exp) {
            System.out.println("file not found!!");
            return selectedFiles;
        }

        return selectedFiles;
    }*/
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

    /*private static Map<String, List<String>> fileToHash(File fileName,String part_of_speech) throws FileNotFoundException, IOException {
        Map<String, List<String>> hash = new TreeMap<String, List<String>>();
        BufferedReader reader;
        String line = "";
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {
                    List<String> temp = new ArrayList<String>();
                    if(line.contains("csv:")){
                        line =line.split("csv:")[1];
                       
                    }
                    if (line.contains(",")) {
                        String[] info = line.split(",");
                        String lexicalElement = info[0];
                        lexicalElement = lexicalElement.replace("\"", "");
                        System.out.println(line);
                        String rankLine = getValue(info[1]) + "+" +  getValue(info[4])+"+";
                        List<String> list=getValueSpace(info[5]);
                        for(String value:list){
                          rankLine+=  value+"+";
                        }
                        rankLine+=part_of_speech;
                        //System.out.println(rankLine);
                        rankLine = rankLine.replace("\"", "");
                        if (hash.containsKey(lexicalElement)) {
                            temp = hash.get(lexicalElement);
                        }
                        temp.add(rankLine);
                        hash.put(lexicalElement, temp);
                    }

                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hash;
    }*/
    private static Map<String, List<String>> fileToHash(File fileName, String lexicalElement, String part_of_speech) throws FileNotFoundException, IOException {
        Map<String, List<String>> hash = new TreeMap<String, List<String>>();
        BufferedReader reader;
        String line = "";
        Integer index = 0;
        String className = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            line = reader.readLine();
            while (line != null) {
                line = reader.readLine();
                if (line != null) {

                    if (line.contains("csv:")) {
                        line = line.split("csv:")[1];
                        if (line.contains(",")) {
                            if (line.contains("XMLSchema#integer")) {
                                continue;
                            }
                            className = line.split(",")[4];
                            //String doubleValue= line.split(",")[2];
                            //System.out.println(index+" "+doubleValue+" "+line+"");
                            index = index + 1;
                            //LineInfo lineInfo=new LineInfo(line,doubleValue);

                            if (hash.containsKey(className)) {
                                List<String> temp = hash.get(className);
                                temp.add(line);
                                hash.put(className, temp);
                            } else {
                                List<String> temp = new ArrayList<String>();
                                temp.add(line);
                                hash.put(className, temp);
                            }

                        }

                    }

                }

            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hash;
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