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

    private int affineR0x = 0;
    private int affineR0y = 0;
    private int affineRXx = 1;
    private int affineRXy = 2;
    private int affineRYx = 0;
    private int affineRYy = 1;


    private int projectiveR0x = 0;
    private int projectiveR0y = 0;
    private int projectiveRXx = 200;
    private int projectiveRXy = 100;
    private int projectiveRYx = 0;
    private int projectiveRYy = 500;
    private int projectiveW0 = 600;
    private int projectiveWx = 1;
    private int projectiveWy = 1;

    private boolean isTransformed = false;

    public FigureBuilder() {
        transformFunction = this::translateTransform;
    }

    private String transformationType = "translate";

    public FigureBuilder(String function) {
        transformFunction = this::translateTransform;
        if (function.equals("affine")) {
            transformFunction = this::affineTransform;
            transformationType = function;
        }
        if (function.equals("projective")) {
            transformFunction = this::projectiveTransform;
            transformationType = function;
        }

    }

    private List<Side> sides = new ArrayList<>();

    private Function<List<Point>, List<Point>> transformFunction;

    public void paintComponent(Graphics g) {
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
        Graphics2D g2 = (Graphics2D) g;

        List<Point> outerShape = transformFunction.apply(translateTransform(getPointsOuter(sideA, sideB, sideC, radius1)));
        drawShape(g2, outerShape);

        List<Point> firstCircle = transformFunction.apply(translateTransform(getFirstCircle(radius2, g2)));
        drawShape(g2, firstCircle);

        List<Point> secondCircle = transformFunction.apply(translateTransform(getSecondCircle()));
        drawShape(g2, secondCircle);

        List<Point> squad = transformFunction.apply(translateTransform(getSquadPoints()));
        drawShape(g2, squad);

        List<Point> inner = transformFunction.apply(translateTransform(getInnerShape()));
        drawShape(g2, inner);

        if (!isTransformed) {
            drawSides(g2);
        }
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

        points.add(basePoint);
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

    private List<Point> getFirstCircle(int radius, Graphics2D g2) {
        int startDegree = 0;
        int endDegree = 360;
        int x;
        int y;
        LinkedList<Point> points = new LinkedList<>();
        Point center = new Point(-50, -90);
        for (double i = startDegree; i < endDegree; i = i + 4) {
            x = (int) (radius * Math.cos(2 * Math.PI * (i + 1) / 360) + center.getX());
            y = (int) (radius * Math.sin(2 * Math.PI * (i + 1) / 360) + center.getY());
            points.add(new Point(x, y));
        }
        g2.setStroke(new BasicStroke(3));

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
        points.add(new Point(-25, -25));
        return points;
    }

    private List<Point> getInnerShape() {
        LinkedList<Point> points = new LinkedList<>();
        int radius = 180;
        int startDegree = -15;
        int endDegree = 70;
        int x = 0;
        int y = 0;
        int startX = 0;
        int startY = 0;
        Point center = new Point(0, 0);

        for (double i = startDegree; i < endDegree; i++) {
            x = (int) (radius * Math.cos(2 * Math.PI * (i + 1) / 360) + center.getX());
            y = (int) (radius * Math.sin(2 * Math.PI * (i + 1) / 360) + center.getY());
            if (i == startDegree) {
                startX = x;
                startY = y;
            }
            points.add(new Point(x, y));
        }

        double angle = Math.toRadians(20);
        double endX = points.getLast().getX() - 50 * Math.sin(angle);
        double endY = points.getLast().getY() - 50 * Math.cos(angle);

        points.add(new Point(endX, endY));

        radius = 130;
        for (int i = x - 15; i < 130; i++) {
            points.add(new Point(i, Math.sqrt(Math.pow(radius, 2) - Math.pow(i, 2))));
            System.out.println(points.getLast());
        }
        for (int i = 130; i > 125; i--) {
            points.add(new Point(i, -Math.sqrt(Math.pow(radius, 2) - Math.pow(i, 2))));
        }
        angle = Math.toRadians(75);
        endX = points.getLast().getX() + 50 * Math.sin(angle);
        endY = points.getLast().getY() - 50 * Math.cos(angle);
        points.add(new Point(endX, endY));
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
        g2.setStroke(new BasicStroke(3));
        for (int i = 0; i < points.size() - 1; i++) {
            g2.drawLine((int) points.get(i).getX(), (int) points.get(i).getY(), (int) points.get(i + 1).getX(), (int) points.get(i + 1).getY());
        }
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
                List<Point> gridPoints = new LinkedList<>();
                gridPoints.add(new Point(-hBound, k));
                gridPoints.add(new Point(hBound, k));
                if (!transformationType.equals("translate")) {
                    gridPoints = transformFunction.apply(gridPoints);
                }
                g2.drawLine((int) gridPoints.get(0).getX(), (int) gridPoints.get(0).getY(), (int) gridPoints.get(1).getX(), (int) gridPoints.get(1).getY());
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2));
                g2.drawString(k.toString(), 10, k);
            }
        }
    }

    List<Point> affineTransform(List<Point> points) {
        return points
                .stream()
                .map((point) -> new Point(affineR0x + affineRXx * point.getX() + affineRXy * point.getY(), affineR0y + affineRYx * point.getX() + affineRYy * point.getY()))
                .collect(Collectors.toList());
    }

    List<Point> projectiveTransform(List<Point> points) {
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
        List<Point> gridPoints = new LinkedList<>();
        gridPoints.add(new Point(k, -bound));
        gridPoints.add(new Point(k, bound));
        if (!transformationType.equals("translate")) {
            gridPoints = transformFunction.apply(gridPoints);
        }
        g2.drawLine((int) gridPoints.get(0).getX(), (int) gridPoints.get(0).getY(), (int) gridPoints.get(1).getX(), (int) gridPoints.get(1).getY());
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

    public int getAffineR0x() {
        return affineR0x;
    }

    public void setAffineR0x(int affineR0x) {
        this.affineR0x = affineR0x;
    }

    public int getAffineR0y() {
        return affineR0y;
    }

    public void setAffineR0y(int affineR0y) {
        this.affineR0y = affineR0y;
    }

    public int getAffineRXx() {
        return affineRXx;
    }

    public void setAffineRXx(int affineRXx) {
        this.affineRXx = affineRXx;
    }

    public int getAffineRXy() {
        return affineRXy;
    }

    public void setAffineRXy(int affineRXy) {
        this.affineRXy = affineRXy;
    }

    public int getAffineRYx() {
        return affineRYx;
    }

    public void setAffineRYx(int affineRYx) {
        this.affineRYx = affineRYx;
    }

    public int getAffineRYy() {
        return affineRYy;
    }

    public void setAffineRYy(int affineRYy) {
        this.affineRYy = affineRYy;
    }

    public int getProjectiveR0x() {
        return projectiveR0x;
    }

    public void setProjectiveR0x(int projectiveR0x) {
        this.projectiveR0x = projectiveR0x;
    }

    public int getProjectiveR0y() {
        return projectiveR0y;
    }

    public void setProjectiveR0y(int projectiveR0y) {
        this.projectiveR0y = projectiveR0y;
    }

    public int getProjectiveRXx() {
        return projectiveRXx;
    }

    public void setProjectiveRXx(int projectiveRXx) {
        this.projectiveRXx = projectiveRXx;
    }

    public int getProjectiveRXy() {
        return projectiveRXy;
    }

    public void setProjectiveRXy(int projectiveRXy) {
        this.projectiveRXy = projectiveRXy;
    }

    public int getProjectiveRYx() {
        return projectiveRYx;
    }

    public void setProjectiveRYx(int projectiveRYx) {
        this.projectiveRYx = projectiveRYx;
    }

    public int getProjectiveRYy() {
        return projectiveRYy;
    }

    public void setProjectiveRYy(int projectiveRYy) {
        this.projectiveRYy = projectiveRYy;
    }

    public int getProjectiveW0() {
        return projectiveW0;
    }

    public void setProjectiveW0(int projectiveW0) {
        this.projectiveW0 = projectiveW0;
    }

    public int getProjectiveWx() {
        return projectiveWx;
    }

    public void setProjectiveWx(int projectiveWx) {
        this.projectiveWx = projectiveWx;
    }

    public int getProjectiveWy() {
        return projectiveWy;
    }

    public void setProjectiveWy(int projectiveWy) {
        this.projectiveWy = projectiveWy;
    }

    public boolean isTransformed() {
        return isTransformed;
    }

    public void setTransformed(boolean transformed) {
        isTransformed = transformed;
    }
}
