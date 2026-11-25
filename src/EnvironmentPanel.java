import Shapes.*;
import Shapes.ShapeCollection.Pyramide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class EnvironmentPanel extends JPanel {

    private static final Color bg = new Color(0);
    private static final Color brushColor = new Color(50, 170, 37, 255);

    private double cameraDistance = 10.0;
    private double fov = 350.0;

    private double shapeX = 0.0;
    private double shapeY = 0.0;
    private double shapeZ = 0.0;

    private final double moveSpeed = 0.2;

    private static Shape3D shape3D;

    public EnvironmentPanel() {
        this.setBackground(bg);
        this.setLayout(null);

        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_D -> shapeX -= moveSpeed; // Links
                    case KeyEvent.VK_A -> shapeX += moveSpeed; // Rechts
                    case KeyEvent.VK_S -> shapeZ += moveSpeed; // Weiter weg (in den Raum hinein)
                    case KeyEvent.VK_W -> shapeZ -= moveSpeed; // Näher heran
                    case KeyEvent.VK_SPACE -> shapeY += moveSpeed; // Hoch (Y ist in Swing nach oben negativ)
                    case KeyEvent.VK_SHIFT -> shapeY -= moveSpeed; // Runter
                }
                repaint();
            }
        });

        this.addMouseWheelListener(e -> {
            cameraDistance += e.getWheelRotation() * 0.3;
            if (cameraDistance < 1.5) {
                cameraDistance = 1.5;
            }
            repaint();
        });

        //Form daten holen und in einem Objekt speichern
        shape3D = new Shape3D(new Pyramide());
    }

    //verschieben
    private Vertex translate(Vertex p) {
        return new Vertex(p.x + shapeX, p.y + shapeY, p.z + shapeZ);
    }



    private Point project(Vertex p) {
       double z = p.z + cameraDistance;
        if (z < 0.2) {
            z = 0.2;
        }
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
        g2d.setStroke(new BasicStroke(3.0f));

        //würfel rigid zeichnen
        Point[] projectedPoints = new Point[shape3D.vertices.length];
        for (int i = 0; i < shape3D.vertices.length; i++) {
            Vertex moved = this.translate(shape3D.vertices[i]);//verschieben
            projectedPoints[i] = this.project(moved); //punkte projizieren
        }
        for (int[] edge : shape3D.edges) {
            Point p1 = projectedPoints[edge[0]];
            Point p2 = projectedPoints[edge[1]];
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
}
