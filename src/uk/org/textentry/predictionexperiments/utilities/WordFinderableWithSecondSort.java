package uk.org.textentry.predictionexperiments.utilities;

public abstract class WordFinderableWithSecondSort<T> extends WordFinderable<T>{
    public abstract int compareTo2(T other);
}