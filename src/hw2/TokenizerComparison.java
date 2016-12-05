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
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import hw2.readWrite;

public class TokenizerComparison {

    public static void main(String[] args) {
        // the name of the file to read
        String inFileName = "/Users/klogg/dev/text_analytics/hw2/text/wsj_0036.txt";
        String outFileName = "/Users/klogg/dev/text_analytics/hw2/output/token_comparison.txt";
        
        ArrayList<String> coreNLPOutput = new ArrayList<String>();
        ArrayList<String> luceneOutput = new ArrayList<String>();
        // read the file
        ArrayList<String> fileText = readWrite.readFileAsList(inFileName);
        // annotate the document

        for (String line: fileText) {
            coreNLPOutput.add(getCoreNLPTokens(line));
            try {
                luceneOutput.add(getLuceneTokens(line));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        HashMap<String, ArrayList<String>> results = new HashMap<String, ArrayList<String>>();
        results.put("Original", fileText);
        results.put("Core NLP", coreNLPOutput);
        results.put("Lucene", luceneOutput);
        String outputDocument = compareSentences(results);
        readWrite.writeFile(outputDocument,outFileName);
        
    }

    private static String getCoreNLPTokens(String fileText) {
        // set up the nlp pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit");
        props.setProperty("ssplit.newlineIsSentenceBreak", "always");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        Annotation document = new Annotation(fileText);
        pipeline.annotate(document);
        
        List<CoreMap> sentenceList = document.get(CoreAnnotations.SentencesAnnotation.class);
        String outputSentence = "";
        for(CoreMap sentence: sentenceList) {
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                outputSentence += token.get(CoreAnnotations.TextAnnotation.class) + " ";
            }
        }
        return outputSentence;
    }

    private static String getLuceneTokens(String fileText) throws IOException{
        
        // define settings for analyzer and create a new standard analyzer
        Tokenizer tokenizer = new StandardTokenizer();
        tokenizer.setReader(new StringReader(fileText));
        tokenizer.reset();
        String output = "";
        while(tokenizer.incrementToken()) {
            output += tokenizer.getAttribute(CharTermAttribute.class).toString() +  " ";
        }
        output.trim();
        tokenizer.close();
        return output;
    }

    public static String compareSentences(HashMap<String, ArrayList<String>> listOfLists) {
        String document = "";
        int maxLength = listOfLists.get("Original").size();
        for (int i=0; i < maxLength; i++) {
            for (Map.Entry<String, ArrayList<String>> entry: listOfLists.entrySet()) {
                if (entry.getValue().get(i).length() > 0) {
                    document += "line " + i + ", " + entry.getKey() + ": " + entry.getValue().get(i) + "\n";
                }
            }
            document += "\n";
        }
        return document;
    }
}
