package editor.objects;

import editor.GeometryUtil;
import editor.Point;
import editor.Rectangle;
import editor.renderers.Renderer;

import java.util.List;
import java.util.Stack;

public class LineSegment extends AbstractGraphicalObject
{
    public LineSegment(Point start, Point end)
    {
        super(new Point[] {start, end});
    }

    public LineSegment()
    {
        this(new Point(0, 0), new Point(10, 0));
    }

    @Override
    public void translate(Point delta)
    {
        setHotPoint(0, getHotPoint(0).translate(delta));
        setHotPoint(1, getHotPoint(1).translate(delta));
        notifyListeners();
    }

    public Rectangle getBoundingBox()
    {
        var start = getHotPoint(0);
        var end = getHotPoint(1);

        int minX = Math.min(start.getX(), end.getX());
        int minY = Math.min(start.getY(), end.getY());
        int width = Math.abs(start.getX() - end.getX());
        int height = Math.abs(start.getY() - end.getY());
        return new Rectangle(minX, minY, width, height);
    }

    public double selectionDistance(Point mousePoint)
    {
        var start = getHotPoint(0);
        var end = getHotPoint(1);
        return GeometryUtil.distanceFromLineSegment(start, end, mousePoint);
    }

    public String getShapeName()
    {
        return "Line";
    }

    public GraphicalObject duplicate()
    {
        var start = getHotPoint(0);
        var end = getHotPoint(1);
        return new LineSegment(start, end);
    }

    public void render(Renderer renderer)
    {
        var start = getHotPoint(0);
        var end = getHotPoint(1);
        renderer.drawLine(start, end);
    }

    public String getShapeID()
    {
        return "@LINE";
    }

    public void load(Stack<GraphicalObject> stack, String data)
    {
        var line = new LineSegment();

        String[] parts = data.split(" ");

        int x1 = Integer.parseInt(parts[0]);
        int y1 = Integer.parseInt(parts[1]);
        int x2 = Integer.parseInt(parts[2]);
        int y2 = Integer.parseInt(parts[3]);

        line.setHotPoint(0, new Point(x1, y1));
        line.setHotPoint(1, new Point(x2, y2));

        stack.push(line);
    }

    public void save(List<String> rows)
    {
        var start = getHotPoint(0);
        var end = getHotPoint(1);
        rows.add(getShapeID() + " " + start.getX() + " " + start.getY() + " " + end.getX() + " " + end.getY());
    }
}
