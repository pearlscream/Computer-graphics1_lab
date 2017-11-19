package utils;

public class Side {
    private String name;
    private Point start;
    private Point end;
    private boolean isHorisontal = true;

    public Side() {
    }

    public Side(String name, Point start, Point end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public boolean isHorisontal() {
        return isHorisontal;
    }

    public void setHorisontal(boolean horisontal) {
        isHorisontal = horisontal;
    }
}
