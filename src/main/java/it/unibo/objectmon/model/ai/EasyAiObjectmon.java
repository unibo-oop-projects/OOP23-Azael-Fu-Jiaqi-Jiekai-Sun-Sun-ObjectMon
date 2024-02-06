package it.unibo.objectmon.model.ai;

import java.util.Random;

import it.unibo.objectmon.api.data.objectmon.Objectmon;
import it.unibo.objectmon.model.ai.api.AiObjectmon;

/**
 * an easy AI implementation of objecmon, which skills are chosen randomly.
 */
public class EasyAiObjectmon implements AiObjectmon {
    private static final int MAX_INDEX = 3;
    private final Random random;
    /**
     * Constructor.
     */
    public EasyAiObjectmon() {
        this.random = new Random();
    }
    /**
     * can be used for extension.
     */
    @Override
    public int useSkill(final Objectmon objectmon) {
        int index;
        do {
            index = this.random.nextInt(MAX_INDEX);
        } while (objectmon.getSkill(index).getCurrentUses() <= 0);
        return index;
    }
}
