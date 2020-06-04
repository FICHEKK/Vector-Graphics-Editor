package editor;

public class Point
{
    private int x;
    private int y;

    public static final Point UP = new Point(0, -1);
    public static final Point RIGHT = new Point(1, 0);
    public static final Point DOWN = new Point(0, 1);
    public static final Point LEFT = new Point(-1, 0);

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Point translate(Point offset)
    {
        return new Point(x + offset.x, y + offset.y);
    }

    public Point difference(Point delta)
    {
        return new Point(x - delta.x, y - delta.y);
    }

    public Point scale(double scalar)
    {
        return new Point((int) (x * scalar), (int) (y * scalar));
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }

    @Override
    public String toString()
    {
        return "(" + x + "," + y + ")";
    }
}
