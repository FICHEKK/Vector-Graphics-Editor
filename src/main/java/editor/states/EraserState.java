package editor.states;

import editor.Canvas;
import editor.DocumentModel;
import editor.Point;
import editor.objects.GraphicalObject;
import editor.renderers.Renderer;

import java.util.ArrayList;
import java.util.List;

public class EraserState extends IdleState
{
    private Canvas canvas;
    private DocumentModel model;
    private List<Point> curvePoints = new ArrayList<>();

    public EraserState(Canvas canvas, DocumentModel model)
    {
        this.canvas = canvas;
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown)
    {
        curvePoints = new ArrayList<>();
        curvePoints.add(mousePoint);
    }

    @Override
    public void mouseDragged(Point mousePoint)
    {
        canvas.repaint();
        curvePoints.add(mousePoint);
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown)
    {
        removeAllSelectedObjects();
        curvePoints.clear();
        canvas.repaint();
    }

    @Override
    public void afterDraw(Renderer renderer)
    {
        for (int i = 1; i < curvePoints.size(); i++)
        {
            Point point = curvePoints.get(i);
            renderer.drawLine(curvePoints.get(i-1), point);
        }
    }

    private void removeAllSelectedObjects()
    {
        for (Point point : curvePoints)
        {
            GraphicalObject object = model.findSelectedGraphicalObject(point);
            if (object != null)
            {
                model.removeGraphicalObject(object);
            }
        }
    }

    @Override
    public String toString()
    {
        return "Eraser";
    }
}
