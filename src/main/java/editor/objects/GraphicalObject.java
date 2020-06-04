package editor.objects;

import editor.Point;
import editor.Rectangle;
import editor.renderers.Renderer;
import editor.listeners.GraphicalObjectListener;

import java.util.List;
import java.util.Stack;

public interface GraphicalObject
{
    boolean isSelected();
    void setSelected(boolean selected);
    int getNumberOfHotPoints();
    Point getHotPoint(int index);
    void setHotPoint(int index, Point point);
    boolean isHotPointSelected(int index);
    void setHotPointSelected(int index, boolean selected);
    double getHotPointDistance(int index, Point mousePoint);

    void translate(Point delta);
    Rectangle getBoundingBox();
    double selectionDistance(Point mousePoint);

    String getShapeName();
    GraphicalObject duplicate();

    void render(Renderer renderer);
    String getShapeID();
    void load(Stack<GraphicalObject> stack, String data);
    void save(List<String> rows);

    void addGraphicalObjectListener(GraphicalObjectListener l);
    void removeGraphicalObjectListener(GraphicalObjectListener l);
}
