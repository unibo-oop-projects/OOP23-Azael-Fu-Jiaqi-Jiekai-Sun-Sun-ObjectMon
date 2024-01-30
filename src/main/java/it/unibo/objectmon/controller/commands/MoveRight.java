package it.unibo.objectmon.controller.commands;

import it.unibo.objectmon.model.Model;

/**
 * Moves the player right by one unit.
 */
public final class MoveRight implements Command {
    @Override
    public void execute(final Model model) {
        model.getGameManager().getPlayerController().moveRight();
    }
}
