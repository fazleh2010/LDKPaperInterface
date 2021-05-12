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
import java.util.TreeSet;
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
    //public static String javaScriptDir = "/var/www/html/ontologyLexicalization/";
    public static String javaScriptDir = "/home/melahi/";
    //public static String javaScriptDir ="../";
    //public static String javaScriptDir = "/var/www/html/ontologyLexicalization/";
    public static String javaScriptFileName = "table.js";
    public static Set<String> adjective=new TreeSet<String>();
    public static Set<String> noun=new TreeSet<String>();
    public static Set<String> verb=new TreeSet<String>();
        


    public static void main(String str[]) throws Exception {
        Logger LOGGER = Logger.getLogger(Interface.class.getName());
        String prediction = "predict_po_for_s_given_localized_l", interestingness = "Coherence", lexicalElement = "australian", parts_of_speech = "JJ";
        String stringAdd = "";
        Boolean flag = false;

        prediction = str[0];
        interestingness = str[1];
        lexicalElement = str[2];
        
        
         List<File> listOfFiles = FileUtils.getSpecificFiles(outputDir, prediction, interestingness,"z_",".txt");
         
         for(File file:listOfFiles){
              System.out.println("fileName:"+file.getName());
             if(file.getName().contains("JJ"))
                 adjective = getList(file);
             if(file.getName().contains("NN"))
                 noun = getList(file);
             if(file.getName().contains("VB"))
                 verb = getList(file);
         }

                 System.out.println("adjective:"+adjective.toString());
                   System.out.println("noun:"+noun.toString());
                    System.out.println("verb:"+verb.toString());

        parts_of_speech = findPosTag(lexicalElement);
        System.out.println(parts_of_speech);

        System.out.println(prediction+" "+interestingness+" "+lexicalElement+" "+parts_of_speech);
        
        /*if (str.length < 3) {
            throw new Exception("less number of argument!!!");
        } else 
        {
            lexicalElement = " \"" + lexicalElement + "\" ";
            if (lexicalElement != null) {
                Result result = new Result();
                result.resultStr(outputDir, lexicalElement, parts_of_speech, prediction, interestingness);
                List<String> rows = result.getRows();
                //System.out.println(result.getContent());

                if (!rows.isEmpty()) {
                    stringAdd = createTable(rows, prediction, interestingness);
                    //System.out.println(stringAdd);

                    flag = FileUtils.stringToFiles(stringAdd, javaScriptDir + javaScriptFileName);
                    //System.out.println("flile add status!!"+flag);
                }

            }

        }*/

    }

  
    private static String createTable(List<String> rows, String prediction,String interestingness) {
         String kbTitle=getKB(prediction); 
         String tableStr = "$(document).ready(function() {\n"
                + "    $('#example').DataTable( {\n"
                + "        data: dataSet,\n"
                + "        columns: [\n"
                + "            { title: \""+kbTitle+"\" },\n"
                + "            { title: \""+"Class"+"\" },\n"
                + "            { title: \""+interestingness+"\" },\n"
                + "            { title: \""+"condAB"+".\" },\n"
                + "            { title: \""+"condBA"+"\" },\n"
                + "            { title: \""+"supA"+"\" },\n"
                + "            { title: \""+"supB"+"\" },\n"
                + "            { title: \""+"PosTag"+"\" },\n"
                + "            { title: \""+"rule"+"\" }\n"
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
    
   
    private static String getKB(String prediction) {
        if(prediction.contains(PredictionRules.predict_po_for_s_given_localized_l))
            return "predicate object pair";
        return "kb";
    }

    private static String findPosTag(String lexicalElement) {
        lexicalElement = lexicalElement.toLowerCase().strip().replace(" ", "_");
        if (adjective.contains(lexicalElement)) {
            return "JJ";
        } else if (noun.contains(lexicalElement)) {
            return "NN";
        } else if (verb.contains(lexicalElement)) {
            return "VB";
        } else {
            return "JJ";
        }
    }
    
    public static Set<String> getList(File fileName) throws FileNotFoundException, IOException {
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

}
