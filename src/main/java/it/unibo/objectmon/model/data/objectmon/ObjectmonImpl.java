package it.unibo.objectmon.model.data.objectmon;

import java.util.List;

import it.unibo.objectmon.api.data.aspect.Aspect;
import it.unibo.objectmon.api.data.objectmon.Objectmon;
import it.unibo.objectmon.api.data.skill.Skill;
import it.unibo.objectmon.model.data.statistics.ActualStats;

/**
 * Implementation of Objectmon.
 */
public class ObjectmonImpl implements Objectmon {

    private final int id;
    private final String name;
    private final List<Aspect> aspects;
    private final List<Skill> skills;
    private ActualStats stats;
    private int level;
    private int exp;
    private static final int MAXEXP = 100;

    /**
     * Constructor of the class ObjectmonImpl.java.
     * @param id The id of the Objectmon.
     * @param name The name of the Objectmon.
     * @param aspects The aspects of the Objectmon.
     * @param skills The skills of the Objectmon.
     * @param stats The stats of the Objectmon.
     * @param level The level of the Objectmon.
     */
    public ObjectmonImpl(
        final int id,
        final String name,
        final List<Aspect> aspects,
        final List<Skill> skills,
        final ActualStats stats,
        final int level
        ) {
        this.id = id;
        this.name = name;
        this.aspects = List.copyOf(aspects);
        this.skills = List.copyOf(skills);
        this.stats = stats;
        this.level = level;
        this.exp = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ActualStats getStats() {
        return this.stats;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLevel() {
        return this.level;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Aspect> getAspect() {
        return List.copyOf(this.aspects);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Skill getSkill(final int skillId) {
        return this.skills.get(skillId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getExp() {
        return this.exp;
    }

    /**
     * Adds a level to Objectmon.
     * Max 100
     */
    private void addLevel() {
        if (getLevel() < 100) {
            this.level++;
        }
    }

    /**
     *
     * @param exp
     */
    private void setExp(final int exp) {
        this.exp = exp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addExp(final int gainedExp) {
        setExp(gainedExp);
        if (getExp() >= MAXEXP) {
            setExp(getExp() - MAXEXP);
            levelUp();
        }
    }

    /**
     *  Levels up an Objectmon and grows its stats.
     */
    public void levelUp() {
        addLevel();
        this.stats = getStats().growAllStats();
    }

}