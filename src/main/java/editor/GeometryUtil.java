package editor;

public class GeometryUtil
{
    public static double distanceBetweenPointsSquared(Point point1, Point point2)
    {
        int dx = point1.getX() - point2.getX();
        int dy = point1.getY() - point2.getY();
        return dx * dx + dy * dy;
    }

    public static double distanceBetweenPoints(Point point1, Point point2)
    {
        return Math.sqrt(distanceBetweenPointsSquared(point1, point2));
    }

    public static double distanceFromLineSegment(Point start, Point end, Point point)
    {
        return distanceBetweenPoints(point, getLineSegmentProjection(start, end, point));
    }

    public static Point getLineSegmentProjection(Point start, Point end, Point point)
    {
        double segmentLength = distanceBetweenPoints(start, end);
        if(segmentLength == 0.0) return start;

        Point a = end.difference(start);
        Point b = point.difference(start);
        double projectionLength = dot(a, b) / segmentLength;
        double t = projectionLength / segmentLength;

        if(t > 1.0) t = 1.0;
        if(t < 0.0) t = 0.0;

        Point offset = end.difference(start).scale(t);
        return start.translate(offset);
    }

    public static double distanceFromRectangle(Rectangle rectangle, Point point)
    {
        int width = rectangle.getWidth();
        int height = rectangle.getHeight();
        int centerX = rectangle.getX() + width / 2;
        int centerY = rectangle.getY() + height / 2;

        int dx = Math.max(Math.abs(point.getX() - centerX) - width / 2, 0);
        int dy = Math.max(Math.abs(point.getY() - centerY) - height / 2, 0);

        return Math.sqrt(dx * dx + dy * dy);
    }

    private static double dot(Point p1, Point p2)
    {
        return p1.getX() * p2.getX() + p1.getY() * p2.getY();
    }
}