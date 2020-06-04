package editor.states;

import editor.DocumentModel;
import editor.Point;
import editor.Rectangle;
import editor.objects.CompositeShape;
import editor.objects.GraphicalObject;
import editor.renderers.Renderer;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class SelectShapeState extends IdleState
{
    private static final int HOTPOINT_SIZE = 10;
    private DocumentModel model;
    private GraphicalObject selectedObject;
    private int selectedHotPointIndex = -1;

    private Point moveAnchor;

    public SelectShapeState(DocumentModel model)
    {
        this.model = model;
    }

    @Override
    public void afterDraw(Renderer renderer, GraphicalObject object)
    {
        if(!object.isSelected()) return;
        drawRectangle(renderer, object.getBoundingBox());

        if (model.getSelectedObjects().size() == 1)
        {
            for (int i = 0; i < object.getNumberOfHotPoints(); i++)
            {
                Point hotpoint = object.getHotPoint(i);
                int x = hotpoint.getX() - HOTPOINT_SIZE / 2;
                int y = hotpoint.getY() - HOTPOINT_SIZE / 2;
                drawRectangle(renderer, new Rectangle(x, y, HOTPOINT_SIZE, HOTPOINT_SIZE));
            }
        }
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown)
    {
        if (selectedObject == null) return;
        selectedHotPointIndex = model.findSelectedHotPoint(selectedObject, mousePoint);
        moveAnchor = mousePoint;
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown)
    {
        if (!ctrlDown) deselectAll();

        selectedObject = model.findSelectedGraphicalObject(mousePoint);
        if(selectedObject == null) return;

        selectedObject.setSelected(true);
    }

    @Override
    public void mouseDragged(Point mousePoint)
    {
        if (selectedObject == null) return;

        if (selectedHotPointIndex == -1)
        {
            var offset = mousePoint.difference(moveAnchor);
            selectedObject.translate(offset);
            moveAnchor = mousePoint;
        }
        else
        {
            selectedObject.setHotPoint(selectedHotPointIndex, mousePoint);
        }
    }

    @Override
    public void keyPressed(int keyCode)
    {
        switch (keyCode)
        {
            case KeyEvent.VK_UP:    translateSelected(Point.UP); break;
            case KeyEvent.VK_RIGHT: translateSelected(Point.RIGHT); break;
            case KeyEvent.VK_DOWN:  translateSelected(Point.DOWN); break;
            case KeyEvent.VK_LEFT:  translateSelected(Point.LEFT); break;

            case KeyEvent.VK_PLUS: increaseZ(); break;
            case KeyEvent.VK_MINUS: decreaseZ(); break;

            case KeyEvent.VK_G: group(); break;
            case KeyEvent.VK_U: ungroup(); break;
        }
    }

    private void group()
    {
        if (model.getSelectedObjects().size() < 2) return;

        List<GraphicalObject> selectedObjects = new ArrayList<>(model.getSelectedObjects());
        selectedObjects.forEach(model::removeGraphicalObject);

        CompositeShape group = new CompositeShape();
        selectedObjects.forEach(group::addChild);

        model.addGraphicalObject(group);
        group.setSelected(true);
    }

    private void ungroup()
    {
        var selectedObjects = model.getSelectedObjects();

        if (selectedObjects.size() != 1) return;
        if (!(selectedObjects.get(0) instanceof CompositeShape)) return;

        CompositeShape group = (CompositeShape) selectedObjects.get(0);
        model.removeGraphicalObject(group);

        for (int i = 0; i < group.getNumberOfChildren(); i++)
        {
            group.getChild(i).setSelected(true);
            model.addGraphicalObject(group.getChild(i));
        }
    }

    private void translateSelected(Point direction)
    {
        if (direction == null) return;

        for (GraphicalObject object : model.getSelectedObjects())
        {
            object.translate(direction);
        }
    }

    private void increaseZ()
    {
        if (selectedObject == null) return;
        model.increaseZ(selectedObject);
    }

    private void decreaseZ()
    {
        if (selectedObject == null) return;
        model.decreaseZ(selectedObject);
    }

    @Override
    public void onStateLeave()
    {
        deselectAll();
    }

    private void deselectAll()
    {
        var copy = new ArrayList<>(model.getSelectedObjects());
        for (GraphicalObject object : copy)
        {
            object.setSelected(false);
        }
    }

    private void drawRectangle(Renderer renderer, Rectangle rectangle)
    {
        int x = rectangle.getX();
        int y = rectangle.getY();
        int w = rectangle.getWidth();
        int h = rectangle.getHeight();

        Point topLeft     = new Point(x, y);
        Point topRight    = new Point(x + w, y);
        Point bottomLeft  = new Point(x, y + h);
        Point bottomRight = new Point(x + w, y + h);

        renderer.drawLine(topLeft, topRight);
        renderer.drawLine(topRight, bottomRight);
        renderer.drawLine(bottomRight, bottomLeft);
        renderer.drawLine(bottomLeft, topLeft);
    }

    @Override
    public String toString()
    {
        return "Selection";
    }
}
