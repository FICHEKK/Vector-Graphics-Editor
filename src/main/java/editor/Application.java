package editor;

import editor.objects.CompositeShape;
import editor.objects.GraphicalObject;
import editor.objects.LineSegment;
import editor.objects.Oval;
import editor.renderers.SVGRenderer;
import editor.states.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class Application extends JFrame
{
    private static final Map<String, GraphicalObject> ID_TO_OBJECT = new HashMap<>();
    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    private Canvas canvas;
    private DocumentModel model;

    private State currentState = new IdleState();
    private JLabel currentStateLabel;

    private Application(List<GraphicalObject> objects)
    {
        setTitle("Vector Graphics Editor");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        setLocationRelativeTo(null);
        setVisible(true);

        initGUI(objects);
    }

    public void changeCurrentState(State newState)
    {
        currentState.onStateLeave();
        currentState = newState;
        currentStateLabel.setText("State: " + currentState);
    }

    public State getCurrentState()
    {
        return currentState;
    }

    private void initGUI(List<GraphicalObject> objects)
    {
        add(createToolbar(objects), BorderLayout.PAGE_START);
        model = new DocumentModel();
        canvas = new Canvas(model, this);
        add(canvas);
        canvas.requestFocusInWindow();
    }

    private JToolBar createToolbar(List<GraphicalObject> objects)
    {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        toolbar.setVisible(true);

        for (GraphicalObject object : objects)
        {
            var button = new JButton(object.getShapeName());
            button.addActionListener(new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    changeCurrentState(new AddShapeState(object, model));
                }
            });
            toolbar.add(button);
        }

        toolbar.add(createSelectionButton());
        toolbar.add(createEraserButton());
        toolbar.add(createExportButton());
        toolbar.add(createSaveButton());
        toolbar.add(createLoadButton());

        var panel = new JPanel();
        panel.add(currentStateLabel = new JLabel("State: " + currentState));
        toolbar.add(panel);

        return toolbar;
    }

    private JButton createSelectionButton()
    {
        var button = new JButton("Selection");
        button.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeCurrentState(new SelectShapeState(model));
            }
        });
        return button;
    }

    private JButton createEraserButton()
    {
        var button = new JButton("Eraser");
        button.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                changeCurrentState(new EraserState(canvas, model));
            }
        });
        return button;
    }

    private JButton createExportButton()
    {
        var button = new JButton("SVG Export");
        button.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser jfc = new JFileChooser();

                if(jfc.showSaveDialog(Application.this) != JFileChooser.APPROVE_OPTION) return;
                Path filePath = jfc.getSelectedFile().toPath();

                SVGRenderer renderer = new SVGRenderer(filePath);
                model.getObjects().forEach(object -> object.render(renderer));

                try
                {
                    renderer.close();
                    JOptionPane.showMessageDialog(Application.this, "File exported!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(Application.this, "Could not export.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return button;
    }

    private JButton createSaveButton()
    {
        var button = new JButton("Save");
        button.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser jfc = new JFileChooser();

                if(jfc.showSaveDialog(Application.this) != JFileChooser.APPROVE_OPTION) return;
                Path filePath = jfc.getSelectedFile().toPath();

                List<String> rows = new ArrayList<>();
                model.getObjects().forEach(object -> object.save(rows));

                try
                {
                    Files.write(filePath, rows);
                    JOptionPane.showMessageDialog(Application.this, "File saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(Application.this, "Could not save.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return button;
    }

    private JButton createLoadButton()
    {
        var button = new JButton("Load");
        button.addActionListener(new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                JFileChooser jfc = new JFileChooser();

                if(jfc.showSaveDialog(Application.this) != JFileChooser.APPROVE_OPTION) return;
                Path filePath = jfc.getSelectedFile().toPath();

                try
                {
                    var rows = Files.readAllLines(filePath);
                    var stack = new Stack<GraphicalObject>();

                    for (String row : rows)
                    {
                        String[] parts = row.split(" ", 2);
                        String id = parts[0];
                        String data = parts[1];

                        var object = ID_TO_OBJECT.get(id);
                        object.load(stack, data);
                    }

                    model.clear();
                    stack.forEach(object -> model.addGraphicalObject(object));

                    JOptionPane.showMessageDialog(Application.this, "File loaded!", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
                catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(Application.this, "Could not load.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return button;
    }

    public static void main(String[] args)
    {
        List<GraphicalObject> objects = new ArrayList<>();

        objects.add(new LineSegment());
        objects.add(new Oval());
        objects.forEach(object -> ID_TO_OBJECT.put(object.getShapeID(), object));

        // Composite is a special type, add it separately.
        var composite = new CompositeShape();
        ID_TO_OBJECT.put(composite.getShapeID(), composite);

        SwingUtilities.invokeLater(() -> new Application(objects));
    }
}
