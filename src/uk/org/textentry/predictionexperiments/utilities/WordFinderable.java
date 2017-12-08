package uk.org.textentry.predictionexperiments.utilities;

import uk.org.textentry.predictionexperiments.utilities.Word;

public abstract class WordFinderable<T> {
    public abstract T newInstance(Word word, Object payload);
    public abstract void seen(Word word, Object payload);
}