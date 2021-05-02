
import java.io.File;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.unix4j.Unix4j;
import org.unix4j.line.Line;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author elahi
 */
public class GrepTest {
    String directory="/home/elahi/new/LDKPaperInterface/src/main/resources/qald9/data/predict_po_for_s_given_localized_l/dic/";
    String fileName="JJ-rules-predict_po_for_s_given_localized_l-AcademicJournal-100-10000-10-4-5-5-100-5-5-5.csv";
    @Test
    public void whenGrepWithSimpleString_thenCorrect() {
        int expectedLineCount = 4;
        File file = new File(directory+fileName);
        List<Line> lines = Unix4j.grep("russian", file).toLineList();
        System.out.println(lines.size());
    
        //assertEquals(expectedLineCount, lines.size());
    }

}
