import javax.swing.JFrame;

// Frame class to intialize JPanel
public class Frame extends JFrame{
    public Frame() {
        Main screen = new Main(); // New JPanel

        this.setVisible(true);
        this.setTitle("Chess");

        this.add(screen);
        this.pack();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }
}
