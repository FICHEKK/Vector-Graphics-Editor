package editor.objects;

import editor.Point;
import editor.Rectangle;
import editor.renderers.Renderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CompositeShape extends AbstractGraphicalObject
{
    private List<GraphicalObject> children = new ArrayList<>();

    public CompositeShape()
    {
        super(new Point[0]);
    }

    public GraphicalObject getChild(int index)
    {
        return children.get(index);
    }

    public void addChild(GraphicalObject child)
    {
        children.add(child);
    }

    public void removeChild(GraphicalObject child)
    {
        children.remove(child);
    }

    public int getNumberOfChildren()
    {
        return children.size();
    }

    @Override
    public void translate(Point delta)
    {
        children.forEach(child -> child.translate(delta));
        notifyListeners();
    }

    @Override
    public Rectangle getBoundingBox()
    {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (GraphicalObject child : children)
        {
            var box = child.getBoundingBox();

            minX = Math.min(minX, box.getX());
            minY = Math.min(minY, box.getY());
            maxX = Math.max(maxX, box.getX() + box.getWidth());
            maxY = Math.max(maxY, box.getY() + box.getHeight());
        }

        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public double selectionDistance(Point mousePoint)
    {
        double minDistance = Double.MAX_VALUE;

        for (GraphicalObject child : children)
        {
            double distance = child.selectionDistance(mousePoint);

            if (distance < minDistance)
            {
                minDistance = distance;
            }
        }

        return minDistance;
    }

    @Override
    public String getShapeName()
    {
        return "Composite";
    }

    @Override
    public GraphicalObject duplicate()
    {
        CompositeShape compositeCopy = new CompositeShape();
        children.forEach(child -> compositeCopy.addChild(child.duplicate()));
        return compositeCopy;
    }

    @Override
    public void render(Renderer renderer)
    {
        children.forEach(child -> child.render(renderer));
    }

    @Override
    public String getShapeID()
    {
        return "@COMP";
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data)
    {
        var composite = new CompositeShape();

        int childCount = Integer.parseInt(data);
        for (int i = 0; i < childCount; i++)
        {
            composite.addChild(stack.pop());
        }

        stack.push(composite);
    }

    @Override
    public void save(List<String> rows)
    {
        for (int i = children.size() - 1; i >= 0; i--)
            children.get(i).save(rows);

        rows.add(getShapeID() + " " + getNumberOfChildren());
    }
}
