package com.newgameplus.framework.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.newgameplus.framework.debug.Logger;
import com.newgameplus.framework.misc.Vector2D;

public final class Texture {
    private static final boolean DEBUG_ENABLED = false;
    private static final int IMAGE_TYPE = BufferedImage.TYPE_INT_ARGB;
    /* Draw/src/main/resources/images/car.png */
    /* At top of the jar of application. */
    private static final String IMAGE_PNG = "/car.png";

    private static Image image;
    private static int imageH = 0;
    private static int imageW = 0;
    private static boolean initDone = false;

    private Texture() {

    }

    public static void draw(final Drawer drawer, final double posX, final double posY,
                            final TextureModifier modifier) {
        final Vector2D position = new Vector2D(posX, posY);
        final double angle = modifier.getAngle();
        final double ratio = modifier.getRatio();
        final double deltaX = modifier.getW();
        final double deltaY = modifier.getH();

        if (DEBUG_ENABLED) {
            final Vector2D offset0 = new Vector2D(-deltaX, -deltaY);
            final Vector2D corner0 = Vector2D.add(position, Vector2D.rotate(offset0, angle).multiply(ratio));

            final Vector2D offset1 = new Vector2D(deltaX, -deltaY);
            final Vector2D corner1 = Vector2D.add(position, Vector2D.rotate(offset1, angle).multiply(ratio));

            final Vector2D offset2 = new Vector2D(deltaX, deltaY);
            final Vector2D corner2 = Vector2D.add(position, Vector2D.rotate(offset2, angle).multiply(ratio));

            final Vector2D offset3 = new Vector2D(-deltaX, deltaY);
            final Vector2D corner3 = Vector2D.add(position, Vector2D.rotate(offset3, angle).multiply(ratio));

            final int[] pointsX = { (int) corner0.getX(), (int) corner1.getX(), (int) corner2.getX(), (int) corner3.getX() };
            final int[] pointsY = { (int) corner0.getY(), (int) corner1.getY(), (int) corner2.getY(), (int) corner3.getY() };
            drawer.setColor(modifier.getColor());
            drawer.fillPolygon(pointsX, pointsY, pointsX.length);

            drawer.setColor(Color.BLACK);
            drawer.drawPolygon(pointsX, pointsY, pointsX.length);
        }
        else {
            /* Create buffer for rotate operation to come. */
            final BufferedImage imageBuff = new BufferedImage(imageW, imageH, IMAGE_TYPE);
            final Graphics2D graphicsCtx = imageBuff.createGraphics();
            graphicsCtx.drawImage(image, 0, 0, null);
            graphicsCtx.dispose();
            /* Set some color to the car image. */
            setcolor(modifier.getColor(), imageBuff);

            /* Construct sequence of transformation to come. */
            final AffineTransform transformer = new AffineTransform();
            /* Set center of car image at position.*/
            transformer.translate(posX, posY);
            /* Rotate. */
            transformer.rotate(Math.toRadians(angle));
            /* Adjust final position, taking into account top left corner. */
            transformer.translate(-deltaX * ratio, -deltaY * ratio);

            /* Instantiate and apply transformation filter */
            final BufferedImageOp doer = new AffineTransformOp(transformer, modifier.getAntialiasing());

            final BufferedImage imageBuffFinal = doer.filter(imageBuff, null);

            /* Display final image from top (0, 0) origin. */
            drawer.drawImage(imageBuffFinal, 0, 0);
        }
    }

    public static void init() {
        if (!initDone && !DEBUG_ENABLED) {
            try {
                image = ImageIO.read(Texture.class.getResourceAsStream(IMAGE_PNG));
            }
            catch (IOException e) {
                Logger.debug("Texture: " + e);
            }

            imageH = image.getHeight(null);
            imageW = image.getWidth(null);
            initDone = true;
        }
    }

    private static void setcolor(final Color color, final BufferedImage image) {
        final int width = image.getWidth() - 1;
        final int height = image.getHeight() - 1;

        for (int col = 1; col < width; col++) {
            for (int raw = 1; raw < height; raw++) {
                final int ori = image.getRGB(col, raw);

                /* Test against black. */
                if ((ori & 0xFFFFFF) != 0) {
                    image.setRGB(col, raw, color.getRGB());
                }
            }
        }
    }
}
