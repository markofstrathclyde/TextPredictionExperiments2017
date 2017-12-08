import de.vogella.algorithms.sort.mergesort.Mergesort;
import uk.org.textentry.predictionexperiments.utilities.*;

import java.util.Arrays;
import java.util.TreeSet;

public class NGramDictionary {

    public static final int LIMITOFNGRAMS = 4;

    private final UnigramDictionary baseDictionary;
    private WordFinder<MainNGNode> nGramStore = new WordFinder<MainNGNode>(new MainNGNode(null, null));

    public NGramDictionary(UnigramDictionary baseDictionary){
        this.baseDictionary = baseDictionary;
    }

    public void learn(String phrase){
        phrase = phrase.trim();
        if (phrase.length()!=0){
            String[] wordStrings = phrase.split("\\s+");

            int maxN = Math.min(LIMITOFNGRAMS, wordStrings.length);
            int len = wordStrings.length;
            for (int n=1; n<=maxN; n++){//loop by length so that unigrams are added to base dictionary first, then bigrams then longergrams
                for (int j=0; j<=len-n; j++){
                    if (n == 1) {
                        //one word - learn current word only as unigram
                        baseDictionary.add(wordStrings[j]);
                    } else {
                        //two words or more- learn ngram of [pastWords..., currentWord] futureWord
                        //no need to add to baseDictionary as we have already done that when j==i
                        learn(Arrays.copyOfRange(wordStrings, j, j+n-1), wordStrings[j+n-1], n == 2);//only learn pure bigrams in future
                    }
                }
            }
        }
    }



    /**
     * Add all n-grams for futureString in the given pastAndPresentString (including adding futureString to unigram base nGramStore
     * For example if pastAndPresentString is "the cat sat on the" and futureString "mat" then "mat" will be added to the base
     * nGramStore as a unigram occurence and added in the pastAndPresentString of "the", "on the", "sat on the" etc up
     * to LIMITOFNGRAMS
     * Assumes all words in pastAndPresentString have been seen
     *
     * @param pastAndPresentString the pastAndPresentString sentence so far, whitespace separated prior words
     * @param futureString the futureString to be predicted by the pastAndPresentString
     */
    public void learn(String pastAndPresentString, String futureString, boolean incrementFuture) {
        pastAndPresentString = pastAndPresentString.trim();
        learn(pastAndPresentString.length()==0? new String[0] : pastAndPresentString.split("\\s+"), futureString, incrementFuture);
    }

    public void learn(String[] pastAndPresentStrings, String futureString, boolean incrementFuture) {
        debug("learn","Learning: "+Arrays.toString(pastAndPresentStrings)+" -> "+futureString +" "+(incrementFuture?"":"(without future)"));
        //Add this occurence of futureString to the base nGramStore

        //find the WeightedWords for all words in the pastAndPresentStrings - we assume these have been seen
        Word[] pastWords;
        Word futureWord = baseDictionary.find(futureString).getTheWord();
        Word presentWord;

        if (pastAndPresentStrings.length==0){//special case of no context for futureWord
            pastWords = new Word[0];
            presentWord = baseDictionary.STARTOFSENTENCE;
        } else {//normal case of context of at least one word

            pastWords = new Word[Math.min(LIMITOFNGRAMS - 2, pastAndPresentStrings.length - 1)];
            int startAt = pastAndPresentStrings.length - pastWords.length - 1;
            for (int i = startAt; i < pastAndPresentStrings.length - 1; i++)
                pastWords[i - startAt] = baseDictionary.find(pastAndPresentStrings[i]).getTheWord();

            presentWord = baseDictionary.find(pastAndPresentStrings[pastAndPresentStrings.length - 1]).getTheWord();
        }

        nGramStore.addWord(presentWord, new ContextPair(pastWords, incrementFuture?futureWord:null));
    }

    /**
     * Finds the uk.org.textentry.predictionexperiments.utilities.Word (base object) for the given string
     * Throws
     * @param word a single word to be found
     * @return
     */
    public MainNGNode find(String word) {
        return nGramStore.findWord(word);
    }

    public DoubleWeightedWord[] predictions(String context, String word) {
        MainNGNode node = find(word);
        WeightedWord[] futurePredictions = node.possibleFutures.toArray(new WeightedWord[node.possibleFutures.size()]);
        long totalFuturesCount = 0;
        for (WeightedWord ww : futurePredictions) totalFuturesCount+=ww.getTheWeight();

        DoubleWeightedWord[] finalPredictions = new DoubleWeightedWord[futurePredictions.length];
        for (int i=0; i<futurePredictions.length; i++) {
            WeightedWord ww = futurePredictions[i];
            debug("predictions", ww.getTheWord()
                    + " Uni=" + baseDictionary.find(ww.getTheWord().getTheWord()).getTheWeight() + "/" + baseDictionary.getTotalWordOccurences()
                    + " Bi=" + ww.getTheWeight() + "/" + totalFuturesCount
                    );

            double unigram = 1.0 * baseDictionary.find(ww.getTheWord().getTheWord()).getTheWeight() / baseDictionary.getTotalWordOccurences();
            double bigram = 1.0 * ww.getTheWeight() / totalFuturesCount;
            double weight = 0.5 * unigram + 0.5 * bigram;
            finalPredictions[i] = new DoubleWeightedWord(ww.getTheWord(), weight);
        }

        Mergesort<DoubleWeightedWord> sorter = new Mergesort<DoubleWeightedWord>(new DoubleWeightedWord(new Word("")));
        sorter.sort(finalPredictions);
        return finalPredictions;
    }


    private class MainNGNode extends WordFinderable {
        private Word presentWord;
        private int presentWordOccurencesAsPresentWord;
        private TreeSet<WeightedWord> possibleFutures;
/*        private WordFinder<UnigramDictionary>[] past
         */

        public MainNGNode(Word present, Object contextO){
            if (present!=null) {//null => test contrsuctor for typing
                ContextPair context = (ContextPair) contextO;
                presentWord = present;
                presentWordOccurencesAsPresentWord = 1;
                if (((ContextPair)contextO).future!=null){
                    possibleFutures = new TreeSet<WeightedWord>();
                    possibleFutures.add(new WeightedWord(context.future));
                }
            }
        }

        @Override
        public Object newInstance(Word word, Object contextPair) {
            return new MainNGNode(word, contextPair);
        }

        @Override
        public void seen(Word word, Object contextPairO) {
            ContextPair context = (ContextPair) contextPairO;

            //update present
            presentWordOccurencesAsPresentWord++;
            WeightedWord newFuture = new WeightedWord(context.future);

            //update future
            if (context.future!=null) {
                if (possibleFutures.contains(newFuture)) {
                    WeightedWord old = possibleFutures.ceiling(new WeightedWord(context.future));
                    possibleFutures.remove(old);
                    old.increment();
                    possibleFutures.add(old);
                } else {
                    possibleFutures.add(newFuture);
                }
            }

            //update past
            //TODO
        }
    }

    private class ContextPair{
        final Word[] past;
        final Word future;

        ContextPair(Word[] past, Word future) {
            this.past = past;
            this.future = future;
        }
    }

    private static void debug (String method, String message) {
        System.out.println("                                                                                  NGramDictionary/" + method + ": " + message);
    }

}
