import javax.swing.*;
import java.awt.*;

public class EnvironmentPanel extends JPanel {

    private static final Color bg = new Color(0);
    private static final Color brushColor = new Color(50, 170, 37, 255);

    private double cameraDistance = 3.0;
    private double fov = 350.0;

    public EnvironmentPanel() {
        this.setBackground(bg);
        this.setLayout(null);
    }

    Vertex[] cubeVertices = {
            //quadrat nahe Betrachter
            new Vertex(-1, 1, -1), new Vertex(1, 1, -1),
            new Vertex(1, -1, -1), new Vertex(-1, -1, -1),
            //quadrat weg Betrachter
            new Vertex(-1, 1, 1), new Vertex(1, 1, 1),
            new Vertex(1, -1, 1), new Vertex(-1, -1, 1)
    };

    int[][] cubeEdges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0}, //quadrat nahe Betrachter
            {4, 5}, {5, 6}, {6, 7}, {7, 4}, //quadrat weg Betrachter
            {0, 4}, {1, 5}, {2, 6}, {3, 7} //verbindungen der quadrate an den jeweiligen 4 ecken
    };

    private Point project(Vertex p) {
       double z = p.z + cameraDistance;
       int x2D = (int) ((p.x / z) * fov + this.getWidth() / 2.0);
       int y2D = (int) ((p.y / z) * fov + this.getHeight() / 2.0);
       return new Point(x2D, y2D);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(brushColor);
        g2d.setStroke(new BasicStroke(5.0f));

        //würfel rigit zeichnen
        Point[] projectedPoints = new Point[cubeVertices.length];
        for (int i = 0; i < cubeVertices.length; i++) {
            projectedPoints[i] = this.project(cubeVertices[i]); //punkte projizieren
        }
        for (int[] edge : cubeEdges) {
            Point p1 = projectedPoints[edge[0]];
            Point p2 = projectedPoints[edge[1]];
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
}
