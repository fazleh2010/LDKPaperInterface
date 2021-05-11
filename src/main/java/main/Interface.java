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
        
        
        //System.out.println(prediction+" "+interestingness+" "+lexicalElement);
      
        if (str.length < 3) {
            throw new Exception("less number of argument!!!");
        } else //lexicalElement="russian";*/
        {
            lexicalElement = " \"" + lexicalElement + "\" ";
            if (lexicalElement != null) {
                Result result=new Result();
                result.resultStr(outputDir, lexicalElement, parts_of_speech, prediction, interestingness);
                List<String> rows = result.getRows();
                System.out.println(result.getContent());

                //System.out.println(stringAdd);
                if (!rows.isEmpty()) {
                    stringAdd = createTable(rows,prediction,interestingness);
                    FileUtils.stringToFiles(stringAdd, javaScriptDir + "table.js");
                }

            }

        }

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

}
