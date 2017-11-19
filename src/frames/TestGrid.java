package frames;

import javax.swing.*;
import java.awt.*;

public class TestGrid {
    private JPanel panel1;
    private JPanel panel2;
    private static FigureBuilder panel;


    public static void main(String[] args) {
        JFrame frame = new JFrame("TestGrid");
        TestGrid currentObject = new TestGrid();
        frame.setContentPane(currentObject.panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        currentObject.panel2.add(new FigureBuilder());
    }
}
