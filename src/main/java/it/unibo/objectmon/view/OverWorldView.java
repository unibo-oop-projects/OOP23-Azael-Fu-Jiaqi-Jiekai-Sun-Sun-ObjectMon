package it.unibo.objectmon.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import it.unibo.objectmon.controller.Controller;
import it.unibo.objectmon.model.entity.api.Direction;
import it.unibo.objectmon.model.entity.api.Npc;
/**
 * The panel responsible for drawing the world in exploration mode.
 */
public final class OverWorldView extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int TILE_SIZE = 48;
    private final transient Controller controller;
    private final transient ImageLoader textureLoader;
    /**
     * Creates a new OverWorld view.
     * @param controller the controller to be attached.
     */
    public OverWorldView(final Controller controller) {
        this.setDoubleBuffered(true);
        this.setBackground(Color.BLACK);
        this.controller = controller;
        this.textureLoader = new ImageLoader();
        this.addKeyListener(new KeyboardControls(controller));
        this.setFocusable(true);
    }
    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (g instanceof Graphics2D) {
            final Graphics2D graphics2d = (Graphics2D) g;
            final RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, 
            RenderingHints.VALUE_RENDER_SPEED);
            graphics2d.setRenderingHints(renderingHints);

            final int playerX = controller.getGameManager().getPlayerManager().getPosition().x() * TILE_SIZE;
            final int playerY = controller.getGameManager().getPlayerManager().getPosition().y() * TILE_SIZE;
            final double cameraX = RenderingHelper.getScreenCenter().getWidth() - playerX;
            final double cameraY = RenderingHelper.getScreenCenter().getHeight() - playerY;
            graphics2d.translate(cameraX, cameraY);

            drawWorld(graphics2d);
            drawNPCs(graphics2d);
            drawPlayer(graphics2d);
            graphics2d.dispose();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void drawNPCs(final Graphics2D g) {
        for (final Npc npc : controller.getGameManager().getNpcs()) {
            BufferedImage image;
            switch (npc.getNpcType()) {
                case TRADER:
                    image = textureLoader.getImage("/npc/vendor.png");
                    break;
                case HEALER:
                    image = textureLoader.getImage("/npc/doctor.png");
                    break;
                case TRAINER:
                    image = textureLoader.getImage("/npc/trainer.png");
                break;
                default:
                    image = textureLoader.getImage("/npc/default.png");
                    break;
            }
            final int npcX = npc.getPosition().x();
            final int npcY = npc.getPosition().y();
            g.drawImage(image, npcX * TILE_SIZE, npcY * TILE_SIZE, null);
        }
    }

    private void drawPlayer(final Graphics2D g) {
        final BufferedImage image;
        final Direction playerDirection = controller.getGameManager().getPlayerManager().getDirection();
        switch (playerDirection) {
            case UP:
                image = textureLoader.getImage("/player/playerUp.png");
                break;
            case DOWN:
                image = textureLoader.getImage("/player/playerDown.png");
                break;
            case LEFT:
                image = textureLoader.getImage("/player/playerLeft.png");
                break;
            case RIGHT:
                image = textureLoader.getImage("/player/playerRight.png");
                break;
            default:
                throw new IllegalStateException();
        }
        final int x = controller.getGameManager().getPlayerManager().getPosition().x();
        final int y = controller.getGameManager().getPlayerManager().getPosition().y();
        g.drawImage(image, x * TILE_SIZE, y * TILE_SIZE, null);
    }

    private void drawWorld(final Graphics2D g) {
        for (final var entry : controller.getGameManager().getWorld().getMap().entrySet()) {
            final BufferedImage image = textureLoader.getImage(entry.getValue().getImagePath());
            final int tileX = entry.getKey().x();
            final int tileY = entry.getKey().y();
            g.drawImage(image, tileX  * TILE_SIZE, tileY * TILE_SIZE, null);
        }
    }
}
