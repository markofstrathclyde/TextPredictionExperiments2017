package uk.org.textentry.predictionexperiments.utilities;

public class DoubleWeightedWord extends WordFinderableWithSecondSort<DoubleWeightedWord> implements Comparable<DoubleWeightedWord>{

    private Word theWord;
    private double theWeight;

    public DoubleWeightedWord(Word theWord) {
        this.theWord = theWord;
        this.theWeight = 1;
    }

    public DoubleWeightedWord(Word theWord, double theWeight) {
        this.theWord = theWord;
        this.theWeight = theWeight;
    }

    public Word getTheWord() {
        return theWord;
    }

    public double getTheWeight() {
        return theWeight;
    }

    public void increment(){
        this.theWeight++;
    }

    /**
     * Sorts by the alphabetic word in increasing order
     * @param that
     * @return
     */
    @Override
    public int compareTo(DoubleWeightedWord that) {
        return this.theWord.compareTo(that.theWord);
    }

    /**
     * Sorts by weight into decreasing order
     * @param other
     * @return
     */
    @Override
    public int compareTo2(DoubleWeightedWord other) {
        if (this.theWeight < other.theWeight)
            return 1;
        else if (this.theWeight == other.theWeight)
            return 0;
        else
            return -1;
    }

    @Override
    public String toString(){
        return String.format("%s#%.4f",theWord, theWeight);
    }

    @Override
    public DoubleWeightedWord newInstance(Word word, Object payload) {
        return new DoubleWeightedWord(word);
    }

    @Override
    public void seen(Word word, Object payload) {
        if (!word.equals(theWord)) throw new IllegalStateException("Words do not equal when the should ("+word+","+theWord+")");
        theWeight++;
    }
}

