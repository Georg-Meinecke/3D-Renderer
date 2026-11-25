package Shapes;

public class Shape3D {

    public Vertex[] vertices;
    public int[][] edges;

    public Shape3D(DisplayedShape shape) {
        this.vertices = shape.vertices;  this.edges = shape.edges;
    }
}
