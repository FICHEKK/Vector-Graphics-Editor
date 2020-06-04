package editor.objects;

import editor.GeometryUtil;
import editor.Point;
import editor.listeners.GraphicalObjectListener;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGraphicalObject implements GraphicalObject
{
    private Point[] hotPoints;
    private boolean[] hotPointSelected;
    private boolean selected;
    private List<GraphicalObjectListener> listeners = new ArrayList<>();

    protected AbstractGraphicalObject(Point[] hotPoints)
    {
        this.hotPoints = hotPoints;
    }

    // ======================================================================
    //                             Selection
    // ======================================================================

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        if(this.selected == selected) return;
        this.selected = selected;
        notifySelectionListeners();
    }

    public int getNumberOfHotPoints()
    {
        return hotPoints.length;
    }

    public Point getHotPoint(int index)
    {
        return hotPoints[index];
    }

    public void setHotPoint(int index, Point point)
    {
        hotPoints[index] = point;
        notifyListeners();
    }

    public boolean isHotPointSelected(int index)
    {
        return hotPointSelected[index];
    }

    public void setHotPointSelected(int index, boolean selected)
    {
        hotPointSelected[index] = selected;
    }

    public double getHotPointDistance(int index, Point mousePoint)
    {
        return GeometryUtil.distanceBetweenPoints(hotPoints[index], mousePoint);
    }

    // ======================================================================
    //                             Listeners
    // ======================================================================

    public void addGraphicalObjectListener(GraphicalObjectListener l)
    {
        listeners.add(l);
    }

    public void removeGraphicalObjectListener(GraphicalObjectListener l)
    {
        listeners.remove(l);
    }

    protected void notifyListeners()
    {
        for(GraphicalObjectListener l : listeners)
        {
            l.graphicalObjectChanged(this);
        }
    }

    protected void notifySelectionListeners()
    {
        for(GraphicalObjectListener l : listeners)
        {
            l.graphicalObjectSelectionChanged(this);
        }
    }
}
