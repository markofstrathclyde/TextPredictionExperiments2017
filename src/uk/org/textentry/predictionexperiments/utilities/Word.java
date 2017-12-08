package uk.org.textentry.predictionexperiments.utilities;

/**
 * A strange class but makes it clear that all word strings are single words that
 * are represented by object reference and not a literal string
 */
public class Word implements Comparable {

    private String theWord;

    /**
     * Constructs a uk.org.textentry.predictionexperiments.utilities.Word for the given word with weight of 1
     * Natural sorting is by theWord (using string sorting)
     * @param theWord   the word!
     */
    public Word(String theWord) {
        this.theWord = theWord;
    }

    public String getTheWord() {
        return theWord;
    }

    public void setTheWord(String theWord) {
        this.theWord = theWord;
    }

    public int length(){
        return theWord.length();
    }
    public char charAt(int index){
        return theWord.charAt(index);
    }


    @Override
    public String toString(){
        return theWord;
    }

    @Override
    public int compareTo(Object other) {
        return theWord.compareTo(((Word)other).theWord);
    }

    @Override
    public boolean equals(Object other){
        System.err.println("+ Warning - use of Word.equals is inefficient ");
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for(int i=0; i<elements.length; i++) {
            System.err.println("| "+elements[i]);
        }


        return theWord.equals( ((Word)other).theWord);
    }

}
