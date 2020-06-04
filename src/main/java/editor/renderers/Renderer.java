package editor.renderers;

import editor.Point;

public interface Renderer
{
    void drawLine(Point start, Point end);
    void fillPolygon(Point[] points);
}