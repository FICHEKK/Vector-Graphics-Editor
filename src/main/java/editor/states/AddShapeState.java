package editor.states;

import editor.DocumentModel;
import editor.Point;
import editor.objects.GraphicalObject;

public class AddShapeState extends IdleState
{
    private GraphicalObject prototype;
    private DocumentModel model;

    public AddShapeState(GraphicalObject prototype, DocumentModel model)
    {
        this.prototype = prototype;
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown)
    {
        var copy = prototype.duplicate();
        copy.translate(mousePoint);
        model.addGraphicalObject(copy);
    }

    @Override
    public String toString()
    {
        return "Add Shape - " + prototype.getShapeName();
    }
}
