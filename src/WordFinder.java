import uk.org.textentry.predictionexperiments.utilities.Word;
import uk.org.textentry.predictionexperiments.utilities.WordFinderable;

/**
 * Implements a quick way to find uk.org.textentry.predictionexperiments.utilities.Word objects
 * Uses a 128ary tree on the ASCII code
 */
public class WordFinder<T extends WordFinderable> {

    private WordFinderNode<T> root = new WordFinderNode<T>();
    private T exampleT;

    public WordFinder(T exampleT){
        this.exampleT = exampleT;
    }
    /**
     * Add the given word to the store and return the object that was added or incremented
     * @param word
     * @return
     */
    public T addWord(Word word, Object payload){
        return addWord(root, word, 0, payload);
    }
    private T addWord(  WordFinderNode tree, Word word, int stringPos, Object payload){
        debug("addWord", word+"@"+stringPos);
        char letter = word.charAt(stringPos);
        if ((stringPos+1)>=word.length()) {//at end of word
            if (tree.currents[letter] ==null) {
                tree.currents[letter] = exampleT.newInstance(word, payload);//first time seen
            } else {
                ((T)(tree.currents[letter])).seen(word, payload);//seen before
            }
            return (T)(tree.currents[letter]);
        } else {
            //recurse
            int currentChar = word.charAt(stringPos);
            if (tree.continuations[currentChar]==null)
                tree.continuations[currentChar]=new WordFinderNode();
            return addWord(tree.continuations[currentChar], word, stringPos+1, payload);
        }
    }

    public T findWord(String word) {
        return findWord(root, word, 0);
    }

    private T findWord(WordFinderNode tree, String word, int stringPos) {
        char letter = word.charAt(stringPos);
        if ((stringPos+1)>=word.length()) {//at end of word
            if (tree.currents[letter]==null) {
                throw new IllegalStateException("Word \""+word+"\" not found (full length)");
            } else {
                return (T)tree.currents[letter];
            }
        } else {
            //recurse
            int currentChar = word.charAt(stringPos);
            if (tree.continuations[currentChar]==null)
                throw new IllegalStateException("Word \""+word+"\" not found (part way through)");
            return findWord(tree.continuations[currentChar], word, stringPos+1);
        }
    }

    private class WordFinderNode<T> {
        WordFinderNode<T>[] continuations = new WordFinderNode[128];
        T[] currents = (T[]) new Object[128];
    }


    private static void debug (String method, String message) {
//        System.out.println("                                                                                  WordFinder/" + method + ": " + message);
    }

}
