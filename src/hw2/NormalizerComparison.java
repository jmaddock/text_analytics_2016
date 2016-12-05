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
import hw2.TokenizerComparison;

public class NormalizerComparison {

    public static void main(String[] args) throws Exception {
        // the name of the file to read
        String inFileName = "/Users/klogg/dev/text_analytics/hw2/text/wsj_0036.txt";
        String outFileName = "/Users/klogg/dev/text_analytics/hw2/output/normalized_token_comparison.txt";

        ArrayList<String> normalizerList = new ArrayList<String>();
        normalizerList.add("Original");
        normalizerList.add("Core NLP");
        normalizerList.add("English");
        normalizerList.add("Porter");
        normalizerList.add("KStem");

        HashMap<String, ArrayList<String>> results = new HashMap<String, ArrayList<String>>();

        for (String n: normalizerList) {
            results.put(n,new ArrayList<String>());
        }
        // read the file
        ArrayList<String> fileText = readWrite.readFileAsList(inFileName);
        
        // annotate the document
        for (String line: fileText) {
            for (String normalizer: normalizerList) {
                String tokenizedLine;
                if (normalizer.equals("Original")) {
                    tokenizedLine = line;
                } else if (normalizer.equals("Core NLP")) {
                    tokenizedLine = getCoreNLPTokens(line);
                } else if (normalizer.equals("English")) {
                    tokenizedLine = luceneStandardAnalyzer(line);
                } else {
                    tokenizedLine = luceneFilter(line,normalizer);
                }
                results.get(normalizer).add(tokenizedLine);
                
            }
            
        }
        
        String outputDocument = TokenizerComparison.compareSentences(results);
        readWrite.writeFile(outputDocument,outFileName);
    }

    private static String getCoreNLPTokens(String fileText) {
        // set up the nlp pipeline
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
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
        
    public static String luceneStandardAnalyzer(String fileText) throws IOException{
        
        // define settings for analyzer and create a new standard analyzer
        Analyzer tokenizer = new EnglishAnalyzer();
        
        // create a new token stream from each sentence
        TokenStream stream  = tokenizer.tokenStream(null, new StringReader(fileText));
        stream.reset();
        String output = "";
        while(stream.incrementToken()){
            output += stream.getAttribute(CharTermAttribute.class).toString() +  " ";
        }
        stream.close();
        output.trim();
        tokenizer.close();
        return output;
    }

    private static String luceneFilter(String fileText, String filterName) throws IOException{
        
        // define settings for analyzer and create a new standard analyzer
        Tokenizer tokenizer = new StandardTokenizer();
        tokenizer.setReader(new StringReader(fileText));
        tokenizer.reset();
        String output = "";
        TokenFilter filter;
        if (filterName.equals("Porter")) {
            filter = new PorterStemFilter(tokenizer);
        } else if (filterName.equals("KStem")) {
            filter = new KStemFilter(tokenizer);
        } else {
            return null;
        }
        while(filter.incrementToken()) {
            output += tokenizer.getAttribute(CharTermAttribute.class).toString() +  " ";
        }
        output.trim();
        return output;
    }

    
}

/*
class PorterAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer source = new StandardTokenizer();
        source.setReader(reader);
        return new TokenStreamComponents(source, new PorterStemFilter(source));
    }
}
*/
