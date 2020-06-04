package editor.renderers;

import editor.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SVGRenderer implements Renderer
{
    private List<String> lines = new ArrayList<>();
    private Path filePath;

    public SVGRenderer(Path filePath)
    {
        this.filePath = filePath;
        lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\">");
    }

    @Override
    public void drawLine(Point start, Point end)
    {
        int x1 = start.getX();
        int y1 = start.getY();
        int x2 = end.getX();
        int y2 = end.getY();

        StringBuilder sb = new StringBuilder("<line ");
        appendAttribute(sb, "x1", x1).append(" ");
        appendAttribute(sb, "y1", y1).append(" ");
        appendAttribute(sb, "x2", x2).append(" ");
        appendAttribute(sb, "y2", y2).append(" ");
        appendAttribute(sb, "style", "stroke:#000000;").append("/>");

        lines.add(sb.toString());
    }

    @Override
    public void fillPolygon(Point[] points)
    {
        StringBuilder sb = new StringBuilder("<polygon points=\"");

        for (Point point : points)
        {
            sb.append(point.getX()).append(",").append(point.getY()).append(" ");
        }

        sb.append("\" style=\"stroke:#ff0000; fill:#0000ff;\"");
        sb.append("/>");

        lines.add(sb.toString());
    }

    private StringBuilder appendAttribute(StringBuilder builder, String attribute, Object value)
    {
        return builder.append(attribute).append("=\"").append(value).append("\"");
    }

    public void close() throws IOException
    {
        lines.add("</svg>");
        Files.write(filePath, lines);
    }
}
