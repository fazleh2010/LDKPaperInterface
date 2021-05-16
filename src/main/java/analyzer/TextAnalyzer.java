/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package analyzer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;

/**
 *
 * @author elahi
 */
public interface TextAnalyzer {

    public static final String POS_TAGGER_WORDS = "POS_TAGGER_WORDS";
    public static final String POS_TAGGER_TEXT = "POS_TAGGER_TEXT";

    public static final String ADJECTIVE = "JJ";
    public static final String NOUN = "NN";
    public static final String VERB = "VB";
    public static final String PRONOUN = "PRP";
    public static final List<String> POSTAGS = new ArrayList<String>(Arrays.asList(NOUN, ADJECTIVE, VERB));

    public static String resources = "/var/www/html/ontologyLexicalization/resources/";
    public static String modelDir = resources + "models/";
    public static String posTagFile = "en-pos-maxent.bin";
    public static String lemmaDictionary = "en-lemmatizer.txt";

}
