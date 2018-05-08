package com.newgameplus.framework.draw;

import java.awt.Color;
import java.util.concurrent.TimeUnit;
import com.newgameplus.framework.misc.Misc;

/**
 * Unit test for simple App.
 */
public class TestDraw {

    public TestDraw() {
        super();
    }

    /**
     * Rigourous Test :-)
     */
    public void testDraw(final Drawer drawer) {
        drawer.clear();

        // System.out.println("testApp() : " + EventQueue.isDispatchThread());

        drawer.setColor(Color.GREEN);
        drawer.drawDashedLine(50.0, 75.0, 150.0, 150.0);

        drawer.setColor(Color.BLUE);
        drawer.drawLine(75.0, 150.0, 300.0, 150.0);

        drawer.setColor(Color.RED);
        final int[] xPoints = {50, 400, 150};
        final int[] yPoints = {200, 200, 400};
        final int pointCount = xPoints.length;
        drawer.drawPolygon(xPoints, yPoints, pointCount);

        drawer.setColor(Color.WHITE);
        drawer.drawRect(160.0, 160.0, 250, 250);

        drawer.drawString("Testing drawString() function", 50, 50);

        drawer.fillCircle(50.0, 400.0, 50);

        drawer.fillPolygon(xPoints, yPoints, pointCount);

        final double posX = 420.0;
        final double posY = 200.0;
        final int width = 40;
        final int height = 160;

        drawer.drawRect(posX - 100.0, posY, width * 2, height);
        // drawer.drawRect(posX, posY, width, height);

        drawer.setColor(Color.RED);
        drawer.drawArc(posX - width / 2, posY, width, height, 0, 90);

        drawer.setColor(Color.GREEN);
        drawer.drawArc(posX - width / 2, posY, width, height, 0, -90);

        // drawer.setColor(Color.CYAN);
        // drawer.drawArc(posX, posY, width, height, -90, 90);

        drawer.show();

        for (int i = 0; i < 256; i++) {
            final Color color = Misc.mix(Color.CYAN.getRGB(), Color.BLUE.getRGB(), i / 255.0);
            // System.out.println(String.format("CYAN : 0x%08X", Color.CYAN.getRGB()));
            // System.out.println(String.format("     : 0x%08X", color.getRGB()));
            // System.out.println(String.format("BLUE : 0x%08X\n", Color.BLUE.getRGB()));

            drawer.setColor(color);
            drawer.fillCircle(50.0 + i, 400.0 - i, 50);
            drawer.show();

            try {
                final long duree = 20;
                System.out.println("Waiting ... " + duree + " ms");
                TimeUnit.MILLISECONDS.sleep(duree);
            }
            catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}
