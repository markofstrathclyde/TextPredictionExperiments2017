import uk.org.textentry.predictionexperiments.utilities.*;

import java.util.Arrays;

public class Main {

    public static void outputTest(UnigramDictionary unigrams, String w){
        try {
            System.out.println(w + "\t" + unigrams.find(w));
        } catch (Exception e) {
            System.out.println(w+" "+e.toString());
        }
    }

    private static void predict(NGramDictionary ngrams, String past, String current){
        DoubleWeightedWord[] predictions = ngrams.predictions(past, current);
        debug("main",past+" "+current+" -> "+ Arrays.toString(predictions));
    }

    public static void main(String[] args){

        debug("main","*** INITIALISING ***");

        UnigramDictionary unigrams = new UnigramDictionary();

        NGramDictionary ngrams = new NGramDictionary(unigrams);

        debug("main","*** TRAINING ***");
        ngrams.learn("i think this is neat");
        ngrams.learn("i think this is cool");
        ngrams.learn("today it is cool");
        ngrams.learn("that was cool");
        ngrams.learn("that was night");
        ngrams.learn("quiet was night");
        ngrams.learn("aye");
        ngrams.learn( "were issued earlier this week of winds reaching");

        debug("main","*** PREDICTING ***");
        predict(ngrams,"i think this", "is");
        predict(ngrams,"i think this", "was");

        debug("main","*** DONE ***");

        System.out.println("TEST"+ (new Word("hello").equals(new Word("hello"))));
    }

    private static void debug (String method, String message) {
        System.out.println("                                                                                  Main/" + method + ": " + message);
    }

}
