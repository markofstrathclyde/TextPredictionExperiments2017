import uk.org.textentry.predictionexperiments.utilities.WeightedWord;
import uk.org.textentry.predictionexperiments.utilities.Word;

public class UnigramDictionary {

    private WordFinder<WeightedWord> uniGramStore = new WordFinder<WeightedWord>(new WeightedWord(new Word("")));
    public Word STARTOFSENTENCE = new Word("\n");

    private long totalWordOccurences = 0;


    public Word add(String wordString){
        Word word = new Word(wordString);
        totalWordOccurences++;
        return uniGramStore.addWord(word, null).getTheWord();
    }

    /**
     * Adds all words in a string/sentence to the finder structure
     * @param sentence a series of whitespace (regex \s) separated words
     */
    public void addAllWords(String sentence){
        String[] words = sentence.split("\\s+");
        for (String word:words) {
            add(word);
        }
    }

    /**
     * Finds the uk.org.textentry.predictionexperiments.utilities.Word (base object) for the given string
     * Throws
     * @param word a single word to be found
     * @return
     */
    public WeightedWord find(String word)  {
        return uniGramStore.findWord(word);
    }

    public long getTotalWordOccurences() {
        return totalWordOccurences;
    }

}
