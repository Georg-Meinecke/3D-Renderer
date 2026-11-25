package Shapes.ShapeCollection;

import Shapes.DisplayedShape;
import Shapes.Vertex;

public class Pyramide extends DisplayedShape {

    public static Vertex[] vertices = {
            //basis fläche
            new Vertex(-1, 1, -1), new Vertex(1, 1, -1), new Vertex(1, 1, 1), new Vertex(-1, 1, 1),
            //spitze
            new Vertex(0, -1, 0)
    };

    public static int[][] edges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0}, //basis fläche
            {0, 4}, {1, 4}, {2, 4}, {3, 4}
    };

    public Pyramide() {
        super(vertices, edges);
    }
}
