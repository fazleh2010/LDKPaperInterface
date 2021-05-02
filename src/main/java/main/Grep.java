/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.unix4j.Unix4j;
import org.unix4j.line.Line;

/**
 *
 * @author elahi
 */
public class Grep {

    /*private static void printStream(InputStream in) throws IOException {
        BufferedReader is = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = is.readLine()) != null) {
            System.out.println(line);
        }
    }*/
    
    public static String qald9Dir = "/home/elahi/new/LDKPaperInterface/src/main/resources/qald9/data/";
    public static String predict_po_for_s_given_localized_l = "predict_po_for_s_given_localized_l/dic/";
   // public static String filename = "JJ-rules-predict_po_for_s_given_localized_l-AcademicJournal-100-10000-10-4-5-5-100-5-5-5.csv";
    public static String filename = "JJ-*.csv";
   
    
    public static String whenGrepWithSimpleString_thenCorrect(String outputDir, String lexicalElement) {
        List<Line> mainlines = new ArrayList<Line>();
        String str = "";
        String[] files = new File(outputDir).list();
        for (String fileName : files) {
            File file = new File(outputDir + fileName);
            List<Line> lines = Unix4j.grep(lexicalElement, file).toLineList();
            mainlines.addAll(lines);
        }
        for (Line line : mainlines) {
            System.out.println(line);
            str += line.getContent();
        }
          System.out.println("!!!!!!!!!!!!!!!!!!!!!!! END !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        //String fileName="A_"+lexicalElement+".csv";
        //StringToFile(str, outputDir+fileName);
        return str;
    }
    
    public static void StringToFile(String content, String path) {
        try {
            Files.writeString(Paths.get(path), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void TEST(String director,String lexicalELement) {
        lexicalELement = " \"" + lexicalELement + "\" ";
        Process p;
        String output = null;
        try {
            //grep -w "Australian" *.csv >>A_Australian.csv
            //grep -w  "russian"  /home/elahi/new/LDKPaperInterface/src/main/resources/qald9/data/predict_po_for_s_given_localized_l/dic/JJ-rules-predict_po_for_s_given_localized_l-AcademicJournal-100-10000-10-4-5-5-100-5-5-5.csv
            String command = "grep -w "+lexicalELement+" "+director+filename;
            System.out.println(command);

            p = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            while ((output = br.readLine()) != null) {
                System.out.println("test:"+output);
                // Process your output here
            }

            System.out.println("exit: " + p.exitValue());
            p.destroy();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public static void main(String args[]) {
        String outputDir=qald9Dir+predict_po_for_s_given_localized_l;
        String lexicalELement="russian";
        TEST(outputDir,lexicalELement);
        /*try {
            Runtime rt = Runtime.getRuntime();
            String[] cmd = {"/bin/sh", "-c", "grep 'Report Process started' server.log|wc -l"};
            Process proc = rt.exec(cmd);
            printStream(proc.getInputStream());
            System.out.println("Error : ");
            printStream(proc.getErrorStream());
        } catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }

}
