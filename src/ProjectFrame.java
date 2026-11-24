import javax.swing.*;

public class ProjectFrame extends JFrame {

    public ProjectFrame() {
        this.setTitle("3D-Renderer");
        this.setSize(512, 512);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(new EnvironmentPanel());
        this.setVisible(true);
    }
}
