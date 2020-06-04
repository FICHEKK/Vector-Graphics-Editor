package editor.objects;

import editor.GeometryUtil;
import editor.Point;
import editor.Rectangle;
import editor.renderers.Renderer;

import java.util.List;
import java.util.Stack;

public class Oval extends AbstractGraphicalObject
{
    public Oval(Point center, int radiusX, int radiusY)
    {
        super(new Point[]
        {
                new Point(center.getX() + radiusX, center.getX()),
                new Point(center.getX(), center.getY() + radiusY)
        });
    }

    public Oval()
    {
        this(new Point(0, 0), 10, 10);
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
        int radiusX = getRadiusX();
        int radiusY = getRadiusY();
        var center = getCenter();

        int x = center.getX() - radiusX;
        int y = center.getY() - radiusY;
        int width = radiusX * 2;
        int height = radiusY * 2;
        return new Rectangle(x, y, width, height);
    }

    public double selectionDistance(Point mousePoint)
    {
        return GeometryUtil.distanceFromRectangle(getBoundingBox(), mousePoint);
    }

    public String getShapeName()
    {
        return "Oval";
    }

    public GraphicalObject duplicate()
    {
        return new Oval(getCenter(), getRadiusX(), getRadiusY());
    }

    public void render(Renderer renderer)
    {
        var center = getCenter();
        int a = getRadiusX();
        int b = getRadiusY();

        final int N = 90;
        final int step = 360 / N;
        Point[] points = new Point[N];

        for(int i = 0; i < N; i++)
        {
            int degree = i * step;
            int x = (int) (a * Math.cos(Math.toRadians(degree)));
            int y = (int) (b * Math.sin(Math.toRadians(degree)));

            points[i] = new Point(x, y).translate(center);
        }

        renderer.fillPolygon(points);
    }

    public String getShapeID()
    {
        return "@OVAL";
    }

    public void load(Stack<GraphicalObject> stack, String data)
    {
        var oval = new Oval();

        String[] parts = data.split(" ");

        int hx = Integer.parseInt(parts[0]);
        int hy = Integer.parseInt(parts[1]);
        int vx = Integer.parseInt(parts[2]);
        int vy = Integer.parseInt(parts[3]);

        oval.setHotPoint(0, new Point(hx, hy));
        oval.setHotPoint(1, new Point(vx, vy));

        stack.push(oval);
    }

    public void save(List<String> rows)
    {
        // horizontal hot-point
        var h = getHotPoint(0);

        // vertical hot-point
        var v = getHotPoint(1);

        rows.add(getShapeID() + " " + h.getX() + " " + h.getY() + " " + v.getX() + " " + v.getY());
    }

    private Point getCenter()
    {
        var horizontal = getHotPoint(0);
        var vertical = getHotPoint(1);
        return new Point(vertical.getX(), horizontal.getY());
    }

    private int getRadiusX()
    {
        var horizontal = getHotPoint(0);
        var vertical = getHotPoint(1);
        return Math.abs(horizontal.getX() - vertical.getX());
    }

    private int getRadiusY()
    {
        var horizontal = getHotPoint(0);
        var vertical = getHotPoint(1);
        return Math.abs(vertical.getY() - horizontal.getY());
    }
}
