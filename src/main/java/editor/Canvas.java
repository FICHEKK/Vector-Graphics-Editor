package editor;

import editor.renderers.CanvasRenderer;
import editor.states.IdleState;
import editor.states.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Canvas extends JComponent
{
    private Application application;
    private DocumentModel model;

    public Canvas(DocumentModel model, Application application)
    {
        this.model = model;
        this.model.addDocumentModelListener(this::repaint);
        this.application = application;

        this.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mousePressed(MouseEvent e)
            {
                application.getCurrentState().mouseDown(new Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
            }

            @Override
            public void mouseReleased(MouseEvent e)
            {
                application.getCurrentState().mouseUp(new Point(e.getX(), e.getY()), e.isShiftDown(), e.isControlDown());
            }
        });

        this.addMouseMotionListener(new MouseAdapter()
        {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                application.getCurrentState().mouseDragged(new Point(e.getX(), e.getY()));
            }
        });

        this.addKeyListener(new KeyAdapter()
        {
            @Override
            public void keyPressed(KeyEvent e)
            {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
                {
                    application.changeCurrentState(new IdleState());
                }
                else
                {
                    application.getCurrentState().keyPressed(e.getKeyCode());
                }
            }
        });

        this.addFocusListener(new FocusAdapter()
        {
            @Override
            public void focusLost(FocusEvent e)
            {
                requestFocusInWindow();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        CanvasRenderer renderer = new CanvasRenderer(g2d);
        g2d.setColor(Color.BLACK);
        model.getObjects().forEach(o ->
        {
            o.render(renderer);
            application.getCurrentState().afterDraw(renderer, o);
        });

        application.getCurrentState().afterDraw(renderer);
    }
}
