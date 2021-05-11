/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elahi
 */
public class FileUtils {

    public static List<String> getSpecificFiles(String fileDir, String prediction, String extension) {
        List<String> selectedFiles = new ArrayList<String>();
        try {
            String[] files = new File(fileDir).list();
            for (String fileName : files) {
                if (fileName.contains(extension) && fileName.contains(prediction)) {
                    selectedFiles.add(fileName);
                }
            }

        } catch (Exception exp) {
            System.out.println("file not found!!");
            return selectedFiles;
        }

        return selectedFiles;
    }
    
     public static Boolean stringToFiles(String str, String fileName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(str);
            writer.close();
            return true;
        } catch (IOException ex) {
             System.out.println("file not found!!:"+fileName+" "+ex.getMessage());
            Logger.getLogger(Interface.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

}
