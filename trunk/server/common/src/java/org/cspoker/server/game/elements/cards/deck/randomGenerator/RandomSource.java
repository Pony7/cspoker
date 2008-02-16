package org.cspoker.server.game.elements.cards.deck.randomGenerator;

import java.util.Random;

public interface RandomSource {

    /**
     * Returns a random-object.
     * 
     * The default implementation uses the current time as seed.
     * 
     * @return A random-object.
     */
    public abstract Random getRandom();

}