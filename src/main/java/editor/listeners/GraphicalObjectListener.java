package editor.listeners;

import editor.objects.GraphicalObject;

public interface GraphicalObjectListener
{
    void graphicalObjectChanged(GraphicalObject object);
    void graphicalObjectSelectionChanged(GraphicalObject object);
}
