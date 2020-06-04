package editor.states;

import editor.objects.GraphicalObject;
import editor.Point;
import editor.renderers.Renderer;

public class IdleState implements State
{
    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) { }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) { }

    @Override
    public void mouseDragged(Point mousePoint) { }

    @Override
    public void keyPressed(int keyCode) { }

    @Override
    public void afterDraw(Renderer renderer, GraphicalObject object) { }

    @Override
    public void afterDraw(Renderer renderer) { }

    @Override
    public void onStateLeave() { }

    @Override
    public String toString()
    {
        return "Idle";
    }
}
