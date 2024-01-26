package it.unibo.objectmon.controller;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.Map;
import it.unibo.objectmon.controller.commands.Command;
import it.unibo.objectmon.controller.engine.GameLoop;
import it.unibo.objectmon.model.Model;
import it.unibo.objectmon.model.entity.api.Npc;
import it.unibo.objectmon.model.entity.api.Player;
import it.unibo.objectmon.model.world.Coord;
import it.unibo.objectmon.model.world.Tile;
import it.unibo.objectmon.view.View;
/**
 * Models the controller of the application.
 */
public final class Controller {

    private static final int COMMAND_LIMIT = 16;
    private final Queue<Command> commandQueue;
    private final Model model;
    private final View view;
    /**
     * 
     * @param model the model from which information is retrieved.
     * @param view  the view on which to render and from which inputs are received.
     */
    public Controller(final Model model, final View view) {
        this.model = model;
        this.view = view;
        this.commandQueue = new ArrayBlockingQueue<>(COMMAND_LIMIT);
    }
    /**
     * Notifies the controller of a new Command.
     * @param command the command to be added to the command queue.
     */
    public void notifyCommand(final Command command) {
        this.commandQueue.add(command);
    }
    /**
     * @return the a set of npcs in the game.
     */
    public Set<Npc> getNpcs() {
        return model.getNpcs();
    }
    /**
     * Returns the {@Player}.
     * @return the player.
     */
    public Player getPlayer() {
        return model.getPlayer();
    }
    /**
     * Returns the world map.
     * @return the world map.
     */
    public Map<Coord, Tile> getMap() {
        return model.getWorld().getMap();
    }
    /**
     * Polls a command.
     * @return a command from the queue.
     */
    public Command pollCommand() {
        return this.commandQueue.poll();
    }
    /**
     * Starts the game.
     */
    public void startGame() {
        final GameLoop gameLoop = new GameLoop(model, view, this);
        gameLoop.startLoop();
    }
}