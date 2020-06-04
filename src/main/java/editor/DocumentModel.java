package editor;

import editor.listeners.DocumentModelListener;
import editor.listeners.GraphicalObjectListener;
import editor.objects.GraphicalObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DocumentModel implements GraphicalObjectListener
{
    private final static double SELECTION_PROXIMITY = 10;

    private List<GraphicalObject> objects = new ArrayList<>();
    private List<GraphicalObject> objectsUnmodifiable = Collections.unmodifiableList(objects);
    private List<GraphicalObject> selectedObjects = new ArrayList<>();
    private List<GraphicalObject> selectedObjectsUnmodifiable = Collections.unmodifiableList(selectedObjects);

    private List<DocumentModelListener> listeners = new ArrayList<>();

    @Override
    public void graphicalObjectChanged(GraphicalObject object)
    {
        notifyListeners();
    }

    @Override
    public void graphicalObjectSelectionChanged(GraphicalObject object)
    {
        if (object.isSelected())
        {
            selectedObjects.add(object);
        }
        else
        {
            selectedObjects.remove(object);
        }

        notifyListeners();
    }

    // ======================================================================
    //                          Graphical objects
    // ======================================================================

    public void addGraphicalObject(GraphicalObject object)
    {
        object.addGraphicalObjectListener(this);
        if(object.isSelected()) selectedObjects.add(object);
        objects.add(object);
        notifyListeners();
    }

    public void removeGraphicalObject(GraphicalObject object)
    {
        object.removeGraphicalObjectListener(this);
        if(object.isSelected()) selectedObjects.remove(object);
        objects.remove(object);
        notifyListeners();
    }

    public void clear()
    {
        for (GraphicalObject object : objects)
        {
            object.removeGraphicalObjectListener(this);
        }

        objects.clear();
        selectedObjects.clear();
        notifyListeners();
    }

    public void increaseZ(GraphicalObject object)
    {
        int index = objects.indexOf(object);
        if(index == objects.size() - 1) return;

        var next = objects.get(index + 1);
        objects.set(index, next);
        objects.set(index + 1, object);
        notifyListeners();
    }

    public void decreaseZ(GraphicalObject object)
    {
        int index = objects.indexOf(object);
        if(index == 0) return;

        var previous = objects.get(index - 1);
        objects.set(index, previous);
        objects.set(index - 1, object);
        notifyListeners();
    }

    // ======================================================================
    //                          Finding selection
    // ======================================================================

    public GraphicalObject findSelectedGraphicalObject(Point mousePoint)
    {
        double minSelectionDistance = Double.MAX_VALUE;
        GraphicalObject closest = null;

        for (GraphicalObject object : objects)
        {
            double distance = object.selectionDistance(mousePoint);

            if (distance < minSelectionDistance && distance <= SELECTION_PROXIMITY)
            {
                minSelectionDistance = distance;
                closest = object;
            }
        }

        return closest;
    }

    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint)
    {
        double minHotPointDistance = Double.MAX_VALUE;
        int closestHotPointIndex = -1;

        for(int i = 0; i < object.getNumberOfHotPoints(); i++)
        {
            Point hotPoint = object.getHotPoint(i);
            double distance = GeometryUtil.distanceBetweenPoints(hotPoint, mousePoint);

            if (distance < minHotPointDistance && distance <= SELECTION_PROXIMITY)
            {
                minHotPointDistance = distance;
                closestHotPointIndex = i;
            }
        }

        return closestHotPointIndex;
    }

    // ======================================================================
    //                              Getters
    // ======================================================================

    public List<GraphicalObject> getObjects()
    {
        return objectsUnmodifiable;
    }

    public List<GraphicalObject> getSelectedObjects()
    {
        return selectedObjectsUnmodifiable;
    }

    // ======================================================================
    //                             Listeners
    // ======================================================================

    public void addDocumentModelListener(DocumentModelListener l)
    {
        listeners.add(l);
    }

    public void removeDocumentModelListener(DocumentModelListener l)
    {
        listeners.remove(l);
    }

    private void notifyListeners()
    {
        for (DocumentModelListener l : listeners)
        {
            l.documentChanged();
        }
    }
}
