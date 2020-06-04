package editor.renderers;

import editor.Point;

import java.awt.*;

public class CanvasRenderer implements Renderer
{
    private Graphics2D g2d;

    public CanvasRenderer(Graphics2D g2d)
    {
        this.g2d = g2d;
    }

    @Override
    public void drawLine(editor.Point start, editor.Point end)
    {
        g2d.drawLine(start.getX(), start.getY(), end.getX(), end.getY());
    }

    @Override
    public void fillPolygon(Point[] points)
    {
        int nPoints = points.length;
        int[] xPoints = new int[nPoints];
        int[] yPoints = new int[nPoints];

        for (int i = 0; i < points.length; i++)
        {
            xPoints[i] = points[i].getX();
            yPoints[i] = points[i].getY();
        }

        Color savedColor = g2d.getColor();

        g2d.setColor(Color.BLUE);
        g2d.fillPolygon(xPoints, yPoints, nPoints);

        g2d.setColor(Color.RED);
        g2d.drawPolygon(xPoints, yPoints, nPoints);

        g2d.setColor(savedColor);
    }
}
