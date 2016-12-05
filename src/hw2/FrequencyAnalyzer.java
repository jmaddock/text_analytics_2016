package hw2;

import java.io.*;
import java.util.*;
import java.nio.file.Path;
import java.util.regex.*;

import edu.stanford.nlp.io.*;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.*;

import org.apache.lucene.analysis.tokenattributes.*;
import org.apache.lucene.util.AttributeFactory;
import org.apache.lucene.analysis.en.*;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;

import hw2.readWrite;
import hw2.NormalizerComparison;

public class FrequencyAnalyzer {

    public static void main(String[] args) throws Exception {
        // the name of the file to read
        String inFileName = "/Users/klogg/dev/text_analytics/hw2/text/classbios.txt";
        String outFileName = "/Users/klogg/dev/text_analytics/hw2/output/normalized_token_comparison.txt";

        // read the file
        ArrayList<String> fileText = readWrite.readFileAsList(inFileName);
        String tokenizedText = "";
        
        // annotate the document
        for (String line: fileText) {
            for (String sentence: line.split("\\. ")) {
                tokenizedText += NormalizerComparison.luceneStandardAnalyzer(sentence) + "\n";
            }   
        }
        readWrite.writeFile(tokenizedText,outFileName);
        printTop(sortMap(unigram(tokenizedText)));
        System.out.println();
        printTop(sortMap(bigram(tokenizedText)));
        System.out.println();
    }

    public static HashMap<String,Integer> unigram(String document) {
        HashMap<String,Integer> frequency = new HashMap<String,Integer>();
        for (String word: document.split(" |\n")) {
            if (frequency.get(word) == null) {
                frequency.put(word,1);
            } else {
                frequency.put(word,frequency.get(word) + 1);
            }
        }
        return frequency;
    }

    public static HashMap<String,Integer> bigram(String document) {
        HashMap<String,Integer> frequency = new HashMap<String,Integer>();
        String[] sentenceList = document.split("\n");
        for (String sentence: sentenceList) {
            String[] wordList = sentence.split(" ");
            for (int i = 0; i < wordList.length-1; i++) {
                String word = wordList[i] + " " + wordList[i+1];
                if (frequency.get(word) == null) {
                    frequency.put(word,1);
                } else {
                    frequency.put(word,frequency.get(word) + 1);
                }
            }
        }
        return frequency;
    }

    public static SortedMap<Integer,String> sortMap(HashMap<String,Integer> unsorted) {
        SortedMap<Integer,String> sorted = new TreeMap<Integer,String>();
        for (Map.Entry<String,Integer> entry:unsorted.entrySet()) {
            sorted.put(entry.getValue(),entry.getKey());
        }
        return sorted;
    }

    public static void printTop(Map<Integer,String> sorted) {
        int count = 0;
        for (Map.Entry<Integer,String> entry:sorted.entrySet()) {
            if (count > (sorted.size()-21)) {
                System.out.println(entry.getValue() + ", " + entry.getKey());
            }
            count++;
        }
    }
       
}
