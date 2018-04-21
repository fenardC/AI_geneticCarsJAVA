package com.newgameplus.framework.draw;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

public final class Drawer {
    public Drawer(Canvas canvas) {
        super();
        this.canvas = canvas;
    }

    public void clear() {
        final BufferStrategy strategy = canvas.getBufferStrategy();

        if (strategy != null) {
            final Graphics graphics = strategy.getDrawGraphics();
            graphics.clearRect(canvas.getX(), canvas.getY(), canvas.getWidth(), canvas.getHeight());
            graphics.dispose();
        }
    }

    public void drawArc(final double posX, final double posY, final int width, final int height,
                        final int startAngle, final int arcAngle) {
        final BufferStrategy strategy = canvas.getBufferStrategy();

        if (strategy != null) {
            final Graphics graphics = strategy.getDrawGraphics();
            graphics.drawArc((int) posX, (int) posY, width, height, startAngle, arcAngle);
            graphics.dispose();
        }
    }

    public void drawDashedLine(final double posX1, final double posY1,
                               final double posX2, final double posY2) {
        final BufferStrategy strategy = canvas.getBufferStrategy();

        if (strategy != null) {
            final Graphics graphics = strategy.getDrawGraphics();
            /* Creates a copy of the Graphics instance */
            final Graphics2D g2d = (Graphics2D) graphics.create();
            /* Set the stroke of the copy, not the original */
            final Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0,
                                                  new float[] { 9 },
                                                  0);
            g2d.setStroke(dashed);
            g2d.drawLine((int) posX1, (int) posY1, (int) posX2, (int) posY2);
            /* Gets rid of the copy */
            g2d.dispose();
            graphics.dispose();
        }
    }

    public void drawImage(final Image img, final double posX, final double posY) {
        final BufferStrategy strategy = canvas.getBufferStrategy();

        if (strategy != null) {
            final Graphics graphics = strategy.getDrawGraphics();
            graphics.drawImage(img, (int) posX, (int) posY, null);
            graphics.dispose();
        }
    }

    public void drawLine(final double posX1, final double posY1, final double posX2, final double posY2) {
        final BufferStrategy strategy = canvas.getBufferStrategy();

        if (strategy != null) {
            final Graphics graphics = strategy.getDrawGraphics();
            graphics.drawLine((int) posX1, (int) posY1, (int) posX2, (int) posY2);
            graphics.dispose();
        }
    }

    public void drawPolygon(final int[] xPoints, final int[] yPoints, final int nPoints) {
        final BufferStrategy strategy = canvas.getBufferStrategy();

        if (strategy != null) {
            final Graphics graphics = strategy.getDrawGraphics();
            graphics.drawPolygon(xPoints, yPoints, nPoints);
            graphics.dispose();
        }
    }

    public void drawRect(final double posX, final double posY, final int width, final int height) {
        final BufferStrategy strategy = canvas.getBufferStrategy();

        if (strategy != null) {
            final Graphics graphics = strategy.getDrawGraphics();
            graphics.drawRect((int) posX, (int) posY, width, height);
            graphics.dispose();
        }
    }

    public void drawString(final String string, final int posX, final int posY) {
        final BufferStrategy strategy = canvas.getBufferStrategy();

        if (strategy != null) {
            final Graphics graphics = strategy.getDrawGraphics();
            graphics.drawString(string, posX, posY);
            graphics.dispose();
        }
    }

    public void fillCircle(final double posX, final double posY, final int radius) {
        final BufferStrategy strategy = canvas.getBufferStrategy();

        if (strategy != null) {
            final Graphics graphics = strategy.getDrawGraphics();
            graphics.fillOval((int)(posX - radius / 2), (int)(posY - radius / 2), radius, radius);
            graphics.dispose();
        }
    }

    public void fillPolygon(final int[] xPoints, final int[] yPoints, final int nPoints) {
        final BufferStrategy strategy = canvas.getBufferStrategy();

        if (strategy != null) {
            final Graphics graphics = strategy.getDrawGraphics();
            graphics.fillPolygon(xPoints, yPoints, nPoints);
            graphics.dispose();
        }
    }

    public void setColor(final Color color) {
        canvas.setForeground(color);
    }

    public void show() {
        final BufferStrategy strategy = canvas.getBufferStrategy();

        if (strategy != null) {
            strategy.show();
            /* synchronized the blitter page shown */
            Toolkit.getDefaultToolkit().sync();
        }
    }

    private final Canvas canvas;
}
