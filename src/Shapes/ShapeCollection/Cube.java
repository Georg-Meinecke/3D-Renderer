package Shapes.ShapeCollection;

import Shapes.DisplayedShapeTransferor;
import Shapes.Vertex;

public class Cube extends DisplayedShapeTransferor {

     public static Vertex[] vertices = {
            //quadrat nahe Betrachter
            new Vertex(-1, 1, -1), new Vertex(1, 1, -1),
            new Vertex(1, -1, -1), new Vertex(-1, -1, -1),
            //quadrat weg Betrachter
            new Vertex(-1, 1, 1), new Vertex(1, 1, 1),
            new Vertex(1, -1, 1), new Vertex(-1, -1, 1)
    };

    public static int[][] edges = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0}, //quadrat nahe Betrachter
            {4, 5}, {5, 6}, {6, 7}, {7, 4}, //quadrat weg Betrachter
            {0, 4}, {1, 5}, {2, 6}, {3, 7} //verbindungen der quadrate an den jeweiligen 4 ecken
    };

    public Cube() {
        super(vertices, edges);
    }
}
