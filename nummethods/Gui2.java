package nummethods;

import javax.swing.*;
import java.awt.*;

public class Gui2 extends JFrame {
    private Image backgroundImage;

    public Gui2() {
        setTitle("Image Background Example");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        backgroundImage = new ImageIcon(getClass().getResource("/nummethods/pixels_bg.png")).getImage();

        setContentPane(new BackgroundPanel());
        setVisible(true);
    }

    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Gui2::new);
    }
}
