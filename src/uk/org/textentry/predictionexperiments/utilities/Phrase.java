package uk.org.textentry.predictionexperiments.utilities;

public class Phrase{

    private Word[] words;

    public Phrase(Word[] words){
        this.words = words;
    }

    public Word[] getWords() {
        return words;
    }

    @Override
    public String toString(){
        StringBuffer sb = new StringBuffer();
        for (Word w : words) sb.append(w+" ");
        return sb.toString().trim();
    }

    /**
     * Equals is based on actual checking - slow and shouldn't be done, the whole point is to use pointers
     * @param otherO
     * @return
     */
    @Override
    public boolean equals(Object otherO){
        if (otherO.getClass()!=this.getClass()) return false;

        Phrase other = (Phrase)otherO;
        System.err.println("Warning - use of Phrase.equals is inefficient");
        boolean eq = this.words.length==other.words.length;
        if (eq){
            for (int i=0; i<words.length; i++)
                eq = eq && this.words[i].equals(other.words[i]);
        }
        return eq;
    }

}