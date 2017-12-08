package uk.org.textentry.predictionexperiments.utilities;

public class WeightedWord extends WordFinderableWithSecondSort<WeightedWord> implements Comparable<WeightedWord>{

    private Word theWord;
    private int theWeight;

    public WeightedWord(Word theWord) {
        this.theWord = theWord;
        this.theWeight = 1;
    }

    public Word getTheWord() {
        return theWord;
    }

    public int getTheWeight() {
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
    public int compareTo(WeightedWord that) {
        return this.theWord.compareTo(that.theWord);
    }

    /**
     * Sorts by weight into decreasing order
     * @param other
     * @return
     */
    @Override
    public int compareTo2(WeightedWord other) {
        if (this.theWeight < other.theWeight)
            return 1;
        else if (this.theWeight == other.theWeight)
            return 0;
        else
            return -1;
    }

    @Override
    public String toString(){
        return theWord+"#"+theWeight;
    }

    @Override
    public WeightedWord newInstance(Word word, Object payload) {
        return new WeightedWord(word);
    }

    @Override
    public void seen(Word word, Object payload) {
        if (this.theWord!=word) throw new IllegalStateException("Words do not equal when the should ("+word+","+theWord+")");
        theWeight++;
    }
}

