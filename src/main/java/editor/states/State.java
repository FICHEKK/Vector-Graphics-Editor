package editor.states;

import editor.objects.GraphicalObject;
import editor.Point;
import editor.renderers.Renderer;

public interface State
{
    void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown);
    void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown);
    void mouseDragged(Point mousePoint);
    void keyPressed(int keyCode);

    void afterDraw(Renderer renderer, GraphicalObject object);
    void afterDraw(Renderer renderer);

    void onStateLeave();
}
