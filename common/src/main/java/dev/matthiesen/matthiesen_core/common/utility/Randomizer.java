package dev.matthiesen.matthiesen_core.common.utility;

import java.util.Random;

/**
 * A utility class for generating random numbers and probabilities.
 */
@SuppressWarnings("unused")
public final class Randomizer {
    private Randomizer() {}

    /**
     * A single instance of Random to be used throughout the application.
     */
    public static Random random = new Random();

    /**
     * Returns a random integer between the given min and max values (inclusive).
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @return a random integer between min and max (inclusive)
     */
    public static int getRandomNumberBetween(int min, int max) {
        return random.nextInt(Math.max(1, max - min + 1)) + min;
    }

    /**
     * Returns true if a random number between 0 and 1 is less than the given chance.
     * @param chance the probability of returning true, between 0.0 and 1.0
     * @return true if the random number is less than the chance, false otherwise
     */
    public static boolean getRandomChance(double chance) {
        if (chance >= (double)1.0F) {
            return true;
        } else {
            return random.nextDouble() < chance;
        }
    }
}
