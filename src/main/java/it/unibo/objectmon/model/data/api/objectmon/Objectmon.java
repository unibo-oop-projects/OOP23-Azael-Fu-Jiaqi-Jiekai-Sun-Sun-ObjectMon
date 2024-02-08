package it.unibo.objectmon.model.data.api.objectmon;

import it.unibo.objectmon.model.data.api.aspect.Aspect;
import it.unibo.objectmon.model.data.api.skill.Skill;
import it.unibo.objectmon.model.data.statistics.ActualStats;
import java.util.List;
/**
 * Class Objectmon.
 * An Objectmon can use Skills, and have changes to stats.
 */
    public interface Objectmon {
    /**
     *
     * @return Returns a copy of stats.
     */
    ActualStats getStats();
    /**
     *
     * @return Return the Objectmon's id.
     */
    int getId();
    /**
     *
     * @return Returns the Objectmon's name.
     */
    String getName();
    /**
     *
     * @return Returns the Objectmon's level.
     */
    int getLevel();
    /**
     *
     * @return Returns the Objectmon's Aspects.
     */
    List<Aspect> getAspect();
    /**
     *
     * @param skillId The id of the skill
     * @return Returns the skill with associated id.
     */
    Skill getSkill(int skillId);
    /**
     *
     * @return Returns the exp that the Objectmon has.
     */
    int getExp();
    /**
     *  Adds to exp. if >= 100, calls levelUp and
     *  removes 100 exp.
     *  @param gainedExp Exp gained after defeating an Objectmon.
     */
    void addExp(int gainedExp);
    /**
     *
     * @return Returns the currentHp of the Objectmon.
     */
    int getCurrentHp();
    /**
     *
     * @param quantity Quantity of the Hp to be added to currentHp.
     */
    void setCurrentHp(int quantity);
}