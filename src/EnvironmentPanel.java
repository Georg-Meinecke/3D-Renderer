import Shapes.*;
import Shapes.ShapeCollection.Cube;
import Shapes.ShapeCollection.Gem;
import Shapes.ShapeCollection.Pyramide;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EnvironmentPanel extends JPanel {

    private static final Color bg = new Color(0);
    private static final Color brushColor = new Color(50, 170, 37, 255);

    private double cameraDistance = 10.0;
    private double fov = 350.0;

    private final double initialShapeX = 0.0;
    private final double initialShapeY = 0.0;
    private final double initialShapeZ = 0.0;
    private final double initialCamYaw = 0.0;
    private final double initialCamPitch = 0.0;
    private final double initialCameraDistance = 10.0;

    private double shapeX = 0.0;
    private double shapeY = 0.0;
    private double shapeZ = 0.0;

    // orbit cam fields
    private double camYaw = 0.0;
    private double camPitch = 0.0;
    private int lastMouseX;
    private int lastMouseY;
    private boolean isOrbiting = false;
    private final double mouseSensitivity = 0.016;

    private final double moveSpeed = 0.2;

    private Shape3D shape3D;
    private static ArrayList<DisplayedShapeTransferor> shapePortfolio = new ArrayList<>();

    private Set<Integer> pressedKeys = new HashSet<>();

    public EnvironmentPanel() {
        this.setBackground(bg);
        this.setLayout(null);

        this.setFocusable(true);
        this.requestFocusInWindow();

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }
        });

        Timer gameLoop = new Timer(16, e -> {
            update();
            repaint();
        });
        gameLoop.start();

        this.addMouseWheelListener(e -> {
            cameraDistance += e.getWheelRotation() * 0.3;
            if (cameraDistance < 1.5) {
                cameraDistance = 1.5;
            }
            repaint();
        });

        //orbit cam
        MouseAdapter orbitListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    isOrbiting = true;
                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    isOrbiting = false;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (isOrbiting) {
                    int deltaX = e.getX() - lastMouseX;
                    int deltaY = e.getY() - lastMouseY;

                    camYaw -= deltaX * mouseSensitivity;
                    camPitch -= deltaY * mouseSensitivity;

                    double maxPitch = Math.PI / 2.0 - 0.05;
                    camPitch = Math.max(-maxPitch, Math.min(maxPitch, camPitch));

                    lastMouseX = e.getX();
                    lastMouseY = e.getY();
                }
            }
        };
        this.addMouseListener(orbitListener);
        this.addMouseMotionListener(orbitListener);

        //Form daten holen und in einem Objekt speichern
        shape3D = randomShape3D();
    }

    private Shape3D randomShape3D() {
        shapePortfolio.add(new Cube());
        shapePortfolio.add(new Pyramide());
        shapePortfolio.add(new Gem());
        Random random = new Random();
        int choosen = random.nextInt(0, shapePortfolio.size());
        DisplayedShapeTransferor randomShape = shapePortfolio.get(choosen);
        return new Shape3D(randomShape);
    }

    private void update() {
        if (pressedKeys.contains(KeyEvent.VK_D)) shapeX += moveSpeed;
        if (pressedKeys.contains(KeyEvent.VK_A)) shapeX -= moveSpeed;
        if (pressedKeys.contains(KeyEvent.VK_S)) shapeZ += moveSpeed;
        if (pressedKeys.contains(KeyEvent.VK_W)) shapeZ -= moveSpeed;
        if (pressedKeys.contains(KeyEvent.VK_SPACE)) shapeY -= moveSpeed;
        if (pressedKeys.contains(KeyEvent.VK_SHIFT)) shapeY += moveSpeed;
        if (pressedKeys.contains(KeyEvent.VK_R)) {resetPositions();}
    }

    private Vertex applyCameraOrbit(Vertex p) {
        //horizontale Drehung um y-Achse (Yaw)
        double cosY = Math.cos(camYaw);
        double sinY = Math.sin(camYaw);
        double x1 = p.x * cosY - p.z * sinY;
        double z1 = p.x * sinY + p.z * cosY;

        //vertikale Drehung x-Achse (Pitch)
        double cosP = Math.cos(camPitch);
        double sinP = Math.sin(camPitch);
        double y2 = p.y * cosP - z1 * sinP;
        double z2 = p.y * sinP - z1 * cosP;

        return new Vertex(x1, y2, z2);
    }

    //verschieben
    private Vertex translate(Vertex p) {
        return new Vertex(p.x + shapeX, p.y + shapeY, p.z + shapeZ);
    }

    private void resetPositions() {
        shapeX = initialShapeX;
        shapeY = initialShapeY;
        shapeZ = initialShapeZ;
        camYaw = initialCamYaw;
        camPitch = initialCamPitch;
        cameraDistance = initialCameraDistance;
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

    private void drawLine3D(Graphics2D g2d, Vertex worldA, Vertex worldB) {
        Vertex viewA = this.applyCameraOrbit(worldA);
        Vertex viewB = this.applyCameraOrbit(worldB);
        Point p1 = this.project(viewA);
        Point p2 = this.project(viewB);
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
    }

    private void drawGrid(Graphics2D g2d, int halfSize, double step, double floorY) {
        g2d.setStroke(new BasicStroke(1.0f));

        for (double i = -halfSize; i <= halfSize; i += step) {
            // Mittellinien durch den Ursprung (X=0 und Z=0) etwas heller hervorheben
            if (Math.abs(i) < 0.001) {
                g2d.setColor(new Color(90, 90, 90));
            } else {
                g2d.setColor(new Color(40, 40, 40));
            }

            // Linie parallel zur Z-Achse (läuft bei festem X = i von -halfSize nach +halfSize)
            drawLine3D(g2d, new Vertex(i, floorY, -halfSize), new Vertex(i, floorY, halfSize));

            // Linie parallel zur X-Achse (läuft bei festem Z = i von -halfSize nach +halfSize)
            drawLine3D(g2d, new Vertex(-halfSize, floorY, i), new Vertex(halfSize, floorY, i));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        this.drawGrid(g2d, 6, 1.0, 1.0);

        g2d.setStroke(new BasicStroke(2.0f));
        g2d.setColor(Color.RED);
        drawLine3D(g2d, new Vertex(0, 1.0, 0), new Vertex(1.5, 1.0, 0));   // X-Achse
        g2d.setColor(Color.BLUE);
        drawLine3D(g2d, new Vertex(0, 1.0, 0), new Vertex(0, 1.0, 1.5));   // Z-Achse
        g2d.setColor(Color.YELLOW);
        drawLine3D(g2d, new Vertex(0, 1.0, 0), new Vertex(0, -0.5, 0));

        g2d.setStroke(new BasicStroke(3.0f));
        g.setColor(new Color(130, 130, 130));
        Point origin3D = this.project(new Vertex(0, 0, 0));
        g2d.fillOval(origin3D.x -3, origin3D.y - 3, 6, 6);
        g2d.setColor(brushColor);

        Point[] projectedPoints = new Point[shape3D.vertices.length];
        for (int i = 0; i < shape3D.vertices.length; i++) {
            Vertex worldPos = this.translate(shape3D.vertices[i]);//Position im Raum
            Vertex viewPos = this.applyCameraOrbit(worldPos);//Kamerawinkel um Ursprung
            projectedPoints[i] = this.project(viewPos); //Abstand und 3D Projektion
        }
        for (int[] edge : shape3D.edges) {
            Point p1 = projectedPoints[edge[0]];
            Point p2 = projectedPoints[edge[1]];
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
}
