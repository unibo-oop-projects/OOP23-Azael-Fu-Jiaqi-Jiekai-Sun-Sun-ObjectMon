package it.unibo.objectmon.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JPanel;
import it.unibo.objectmon.controller.Controller;
import it.unibo.objectmon.model.entity.npc.AbstractNPC;
import it.unibo.objectmon.model.entity.npc.api.Healer;
import it.unibo.objectmon.model.entity.npc.api.Seller;
import it.unibo.objectmon.model.entity.npc.api.Trainer;
import it.unibo.objectmon.model.eventlog.EventLogger;
import it.unibo.objectmon.model.world.Coord;
import it.unibo.objectmon.view.controls.OverWorldControls;
import it.unibo.objectmon.view.utility.ImageLoader;
import it.unibo.objectmon.view.utility.ImageLoaderImpl;

/**
 * A JPanel responsible for rendering the overworld environment and entities during exploration mode.
 * This panel displays the game world, including the player character, NPCs, and terrain tiles,
 * providing a visual representation of the game state to the user.
 */
public final class OverWorldPanel extends JPanel {
    private static final int FONT_SIZE = 14;
    private static final long serialVersionUID = 1L;
    private static final int TILE_SIZE = 48;
    private final transient Controller controller;
    private final transient ImageLoader textureLoader;

    /**
     * Constructs a new OverWorld panel, initializing its properties and attaching it to the provided controller.
     * 
     * @param controller the controller responsible for managing inputs and retrieving game data.
     */
    public OverWorldPanel(final Controller controller) {
        this.setDoubleBuffered(true);
        this.setBackground(Color.BLACK);
        this.controller = controller;
        this.textureLoader = new ImageLoaderImpl();
        this.addKeyListener(new OverWorldControls(controller));
        this.setFocusable(true);
    }

    /**
     * This method is automatically invoked by Swing to redraw the panel when necessary.
     * 
     * @param g the Graphics context in which to paint.
     */
    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        if (g instanceof Graphics2D) {
            final Graphics2D graphics2d = (Graphics2D) g;
            final RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_RENDERING, 
            RenderingHints.VALUE_RENDER_SPEED);
            graphics2d.setRenderingHints(renderingHints);
            //Computes the offset needed to center the camera.
            final int playerX = controller.getPlayerController().getPosition().x() * TILE_SIZE;
            final int playerY = controller.getPlayerController().getPosition().y() * TILE_SIZE;
            final double cameraX = getWidth() / 2 - playerX;
            final double cameraY = getHeight() / 2 - playerY;
            //In this section the camera is always centered on the player.
            graphics2d.translate(cameraX, cameraY);
            drawWorld(graphics2d);
            drawNPCs(graphics2d);
            drawPlayer(graphics2d);
            //From this point on HUD elements are drawn.
            graphics2d.translate(-cameraX, -cameraY);
            drawEventLog(graphics2d);
            graphics2d.dispose();
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void drawNPCs(final Graphics2D g) {
        for (final AbstractNPC npc : controller.getNpcManager().getNpcs()) {
            final BufferedImage image = getNPCImage(npc);
            g.drawImage(image, npc.getPosition().x() * TILE_SIZE, npc.getPosition().y() * TILE_SIZE, null);
        }
    }

    private void drawPlayer(final Graphics2D g) {
        final BufferedImage image = getPlayerImage();
        final Coord playerPosition = controller.getPlayerController().getPosition();
        g.drawImage(image, playerPosition.x() * TILE_SIZE, playerPosition.y() * TILE_SIZE, null);
    }

    private void drawWorld(final Graphics2D g) {
        for (final var entry : controller.getWorld().getMap().entrySet()) {
            final BufferedImage image = textureLoader.getImage(entry.getValue().getImagePath());
            final int tileX = entry.getKey().x();
            final int tileY = entry.getKey().y();
            g.drawImage(image, tileX  * TILE_SIZE, tileY * TILE_SIZE, null);
        }
    }

    private void drawEventLog(final Graphics2D g) {
        final List<String> messages = EventLogger.getLogger().getMessages();
        final int lineHeight = 20;
        final int boxHeight = EventLogger.LIMIT * lineHeight;
        final int panelWidth = getWidth();
        final int panelHeight = getHeight();

        // Calculate the position of the black box at the bottom of the panel
        final int boxX = 0;
        final int boxY = panelHeight - boxHeight;

        g.setColor(Color.BLACK);
        g.fillRect(boxX, boxY, panelWidth, boxHeight);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, FONT_SIZE));

        // Adjusted to draw text a little higher
        final int startY = boxY + lineHeight - 5; 

        // Draw messages from the top to the bottom.
        for (int i = 0; i < messages.size(); i++) {
            final String message = messages.get(i);
            g.drawString(message, 10, startY + (i * lineHeight));
        }
    }

    private BufferedImage getNPCImage(final AbstractNPC npc) {
        if (npc instanceof Seller) {
            return textureLoader.getImage("/npc/vendor.png");
        } else if (npc instanceof Healer) {
            return textureLoader.getImage("/npc/doctor.png");
        } else if (npc instanceof Trainer) {
            return textureLoader.getImage("/npc/trainer.png");
        } else {
            return textureLoader.getImage("/npc/default.png");
        }
    }

    private BufferedImage getPlayerImage() {
        final String imagePath;
        switch (controller.getPlayerController().getDirection()) {
            case UP:
                imagePath = "/player/playerUp.png";
                break;
            case DOWN:
                imagePath = "/player/playerDown.png";
                break;
            case LEFT:
                imagePath = "/player/playerLeft.png";
                break;
            case RIGHT:
                imagePath = "/player/playerRight.png";
                break;
            default:
                throw new IllegalStateException();
        }
        return textureLoader.getImage(imagePath);
    }
}
