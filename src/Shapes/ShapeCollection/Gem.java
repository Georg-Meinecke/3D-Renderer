package Shapes.ShapeCollection;

import Shapes.DisplayedShapeTransferor;
import Shapes.Vertex;

public class Gem extends DisplayedShapeTransferor {

    public static Vertex[] vertices = {
            //basis fläche
            new Vertex(-1, 1, -1), new Vertex(1, 1, -1), new Vertex(1, 1, 1), new Vertex(-1, 1, 1),
            //spitze oben
            new Vertex(0, -1, 0),
            //spize unten
            new Vertex(0, 3, 0)
    };

    public static int[][] edges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0}, //basis fläche
            {0, 4}, {1, 4}, {2, 4}, {3, 4},
            {0, 5}, {1, 5}, {2, 5}, {3, 5}
    };

    public Gem() {
        super(vertices, edges);
    }
}
