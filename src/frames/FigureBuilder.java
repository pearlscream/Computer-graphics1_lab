package frames;

import utils.Point;
import utils.Side;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Dima on 22.10.2017.
 */
public class FigureBuilder extends JPanel {
    private int rotate = 0;
    private double scale = 1;
    private int translateX = 0;
    private int translateY = 0;

    private int sideA = 40;
    private int sideB = 150;
    private int sideC = 430;
    private int radius1 = 40;
    private int radius2 = 30;


    private List<Side> sides = new ArrayList<>();

    private Function<List<Point>, List<Point>> transformFunction;

    public void paintComponent(Graphics g) {
        transformFunction = this::translateTransform;
        draw(g);
    }

    private void draw(Graphics g) {
        g.setColor(Color.WHITE);
        super.paintComponent(g);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK);

        // Set up a Cartesian coordinate system
        // get the size of the drawing area
        Dimension size = this.getSize();

        // place the origin at the middle
        g.translate(size.width / 2, size.height / 2);


        // draw the x and y axes
        drawGridAndAxes(g);
//        drawFigure(g);
        Graphics2D g2 = (Graphics2D) g;

        List<Point> outerShape = transformFunction.apply(translateTransform(getPointsOuter(sideA, sideB, sideC, radius1)));
        drawShape(g2, outerShape);

        List<Point> firstCircle = transformFunction.apply(translateTransform(getFirstCircle(radius2)));
        drawShape(g2, firstCircle);

        List<Point> secondCircle = transformFunction.apply(translateTransform(getSecondCircle()));
        drawShape(g2, secondCircle);

        List<Point> squad = transformFunction.apply(translateTransform(getSquadPoints()));
        drawShape(g2, squad);

        List<Point> inner = transformFunction.apply(translateTransform(getInnerShape()));
        drawShape(g2, inner);

        drawSides(g2);
    }


    private List<Point> getPointsOuter(int sideA, int sideB, int sideC, int radius1) {
        Point basePoint = new Point(0, -150);
        LinkedList<Point> points = new LinkedList<>();
        points.add(basePoint);
        points.add(new Point(basePoint.getX() + sideA, basePoint.getY()));
        Side dimesionA = new Side(
                "A",
                basePoint,
                points.getLast());
        dimesionA.setHorisontal(false);
        sides.add(dimesionA);
        points.add(new Point(points.getLast().getX() + 20, points.getLast().getY() - 60));
        Side dimesionB = new Side(
                "B",
                points.getLast(),
                new Point(points.getLast().getX() + sideB, points.getLast().getY()));
        dimesionB.setHorisontal(false);
        sides.add(dimesionB);
        points.add(new Point(points.getLast().getX() + sideB, points.getLast().getY()));
        Side dimesionC = new Side(
                "C",
                points.getLast(),
                new Point(points.getLast().getX(), points.getLast().getY() + sideC));
        sides.add(dimesionC);
        points.add(new Point(points.getLast().getX(), points.getLast().getY() + sideC));
        points.add(new Point(points.getLast().getX() - 200, points.getLast().getY()));

        Point center = new Point(-90, sideC - 210 - radius1);

        points.addAll(getCirclePoints(100, 210, center, radius1));

        points.add(new Point(points.getLast().getX() + 25, points.getLast().getY() - 100));

        points.add(new Point(points.getLast().getX(), points.getLast().getY() - 170));

        int radius2 = 65;
        center = new Point(points.getLast().getX() + radius2 - 5, points.getLast().getY() + 25);

        points.addAll(getCirclePoints(200, 270, center, radius2));

        return points;
    }

    private LinkedList<Point> getCirclePoints(int startDegree, int endDegree, Point center, int radius) {
        LinkedList<Point> points = new LinkedList<>();
        int x;
        int y;
        for (double i = startDegree; i < endDegree; i++) {
            x = (int) (radius * Math.cos(2 * Math.PI * (i + 1) / 360) + center.getX());
            y = (int) (radius * Math.sin(2 * Math.PI * (i + 1) / 360) + center.getY());
            points.add(new Point(x, y));
        }
        return points;
    }

    private List<Point> getFirstCircle(int radius) {
        int startDegree = 0;
        int endDegree = 360;
        int x;
        int y;
        LinkedList<Point> points = new LinkedList<>();
        Point center = new Point(-50, -90);
        for (double i = startDegree; i < endDegree; i++) {
            x = (int) (radius * Math.cos(2 * Math.PI * (i + 1) / 360) + center.getX());
            y = (int) (radius * Math.sin(2 * Math.PI * (i + 1) / 360) + center.getY());
            points.add(new Point(x, y));
        }
        return points;
    }

    private List<Point> getSecondCircle() {
        int radius = 30;
        int startDegree = 0;
        int endDegree = 360;
        int x;
        int y;
        LinkedList<Point> points = new LinkedList<>();
        Point center = new Point(-75, 175);
        for (double i = startDegree; i < endDegree; i++) {
            x = (int) (radius * Math.cos(2 * Math.PI * (i + 1) / 360) + center.getX());
            y = (int) (radius * Math.sin(2 * Math.PI * (i + 1) / 360) + center.getY());
            points.add(new Point(x, y));
        }
        return points;
    }

    private List<Point> getSquadPoints() {
        LinkedList<Point> points = new LinkedList<>();
        points.add(new Point(-25, -25));
        points.add(new Point(75, -25));
        points.add(new Point(75, 75));
        points.add(new Point(-25, 75));
        return points;
    }

    private List<Point> getInnerShape() {
        LinkedList<Point> points = new LinkedList<>();
        int radius = 180;
        int startDegree = -10;
        int endDegree = 70;
        int x = 0;
        int y = 0;
        Point center = new Point(0, 0);

        for (double i = startDegree; i < endDegree; i++) {
            x = (int) (radius * Math.cos(2 * Math.PI * (i + 1) / 360) + center.getX());
            y = (int) (radius * Math.sin(2 * Math.PI * (i + 1) / 360) + center.getY());
            points.add(new Point(x, y));
        }
        points.add(new Point(x - 30, y - 35));
        radius = 135;
        for (int i = x - 30; i < 150; i++) {
            points.add(new Point(i, Math.sqrt(Math.pow(radius, 2) - Math.pow(i, 2))));
        }
        for (int i = 150; i > 133; i--) {
            points.add(new Point(i, -Math.sqrt(Math.pow(radius, 2) - Math.pow(i, 2))));
        }
        return points;
    }

    private void drawSides(Graphics2D g2) {
        g2.setStroke(new BasicStroke(2));
        g2.setFont(new Font("TimesRoman", Font.BOLD, 20));
        for (Side side : sides) {
            GeneralPath path = new GeneralPath();
            path.moveTo(side.getStart().getX(), side.getStart().getY());
            if (side.isHorisontal()) {
                path.lineTo(side.getStart().getX() + 20, side.getStart().getY());
                path.lineTo(side.getStart().getX() + 20, side.getEnd().getY());
                path.lineTo(side.getStart().getX(), side.getEnd().getY());
                g2.drawString(side.getName(), ((int) (side.getStart().getX() + 30)), (int) (side.getEnd().getY() - side.getStart().getY()) / 2);
            } else {
                path.lineTo(side.getStart().getX(), side.getStart().getY() - 20);
                path.lineTo(side.getEnd().getX(), side.getStart().getY() - 20);
                path.lineTo(side.getEnd().getX(), side.getEnd().getY());
                g2.drawString(side.getName(), ((int) (side.getEnd().getX() - side.getStart().getX()) / 2), (int) (side.getStart().getY() - 30));
            }
            g2.draw(path);
        }
    }

    private void drawShape(Graphics2D g2, List<Point> points) {
        GeneralPath ctx = new GeneralPath();
        ctx.moveTo(points.get(0).getX(), points.get(0).getY());
        points.forEach((point) -> ctx.lineTo(point.getX(), point.getY()));
        ctx.closePath();
        g2.setStroke(new BasicStroke(3));
        g2.draw(ctx);
    }

    private List<Point> translateTransform(List<Point> points) {
        double rotateDegree = this.rotate / 180.0 * Math.PI;
        double scale = this.scale;
        double translateX = +this.translateX;
        double translateY = -this.translateY;
        return points
                .stream()
                .map(point -> {
                    double x = point.getX() * scale * Math.cos(rotateDegree) - point.getY() * scale * Math.sin(rotateDegree) + translateX;
                    double y = point.getX() * scale * Math.sin(rotateDegree) + point.getY() * scale * Math.cos(rotateDegree) + translateY;
                    return new Point(x, y);
                })
                .collect(Collectors.toList());
    }


    private void drawGridAndAxes(Graphics g) {
        Dimension size = this.getSize();
        int hBound = ((size.width / 2) / 50) * 50;
        int vBound = ((size.height / 2) / 50) * 50;
        int tic = size.width / 200;
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));

        // draw the x-axis
        g.drawLine(-hBound, 0, hBound, 0);

        // draw the tic marks along the x axis
        g.setFont(new Font("Times New Roman", Font.PLAIN, 10));
        for (Integer k = -hBound; k <= hBound; k += 25) {
            g.drawLine(k, tic, k, -tic);
            drawGridLine(g2, vBound, k);
            g2.drawString(k.toString(), k - 10, 13);
        }

        // draw the y-axis
        g.drawLine(0, vBound, 0, -vBound);

        // draw the tic marks along the y axis
        for (Integer k = -vBound; k <= vBound; k += 25) {
            if (!k.equals(0)) {
                g.drawLine(-tic, k, +tic, k);
                g2.setColor(Color.GRAY);
                g2.setStroke(new BasicStroke(1));
                g2.drawLine(-hBound, k, hBound, k);
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawString(k.toString(), 10, k);
            }
        }
    }

    List<Point> affineTransform(List<Point> points) {
        int affineR0x = 0;
        int affineR0y = 0;
        int affineRXx = 1;
        int affineRXy = 2;
        int affineRYx = 0;
        int affineRYy = 1;
        return points
                .stream()
                .map((point) -> new Point(affineR0x + affineRXx * point.getX() + affineRXy * point.getY(), affineR0y + affineRYx * point.getX() + affineRYy * point.getY()))
                .collect(Collectors.toList());
    }

    List<Point> projectiveTransform(List<Point> points) {
        int projectiveR0x = 0;
        int projectiveR0y = 0;
        int projectiveRXx = 200;
        int projectiveRXy = 100;
        int projectiveRYx = 0;
        int projectiveRYy = 500;
        int projectiveW0 = 600;
        int projectiveWx = 1;
        int projectiveWy = 1;

        return points
                .stream()
                .map((point) -> new Point((projectiveR0x * projectiveW0 + projectiveRXx * projectiveWx * point.getX() + projectiveRXy * projectiveWy * point.getY())
                        / (projectiveW0 + projectiveWx * point.getX() + projectiveWy * point.getY()), (projectiveR0y * projectiveW0 + projectiveRYx * projectiveWx * point.getX() + projectiveRYy * projectiveWy * point.getY())
                        / (projectiveW0 + projectiveWx * point.getX() + projectiveWy * point.getY())))
                .collect(Collectors.toList());
    }

    private void drawGridLine(Graphics2D g2, int bound, int k) {
        g2.setColor(Color.GRAY);
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(k, -bound, k, bound);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
    }

    public int getRotate() {
        return rotate;
    }

    public void setRotate(int rotate) {
        this.rotate = rotate;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public int getTranslateX() {
        return translateX;
    }

    public void setTranslateX(int translateX) {
        this.translateX = translateX;
    }

    public int getTranslateY() {
        return translateY;
    }

    public void setTranslateY(int translateY) {
        this.translateY = translateY;
    }

    public int getSideA() {
        return sideA;
    }

    public void setSideA(int sideA) {
        this.sideA = sideA;
    }

    public int getSideB() {
        return sideB;
    }

    public void setSideB(int sideB) {
        this.sideB = sideB;
    }

    public int getSideC() {
        return sideC;
    }

    public void setSideC(int sideC) {
        this.sideC = sideC;
    }

    public int getRadius1() {
        return radius1;
    }

    public void setRadius1(int radius1) {
        this.radius1 = radius1;
    }

    public int getRadius2() {
        return radius2;
    }

    public void setRadius2(int radius2) {
        this.radius2 = radius2;
    }
}