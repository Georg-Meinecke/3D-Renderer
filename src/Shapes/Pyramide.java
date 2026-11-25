package Shapes;

public class Pyramide {

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
}
