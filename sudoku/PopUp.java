import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PopUp {
    
    private final String TITLE; // Title of pop-up
    private final String MSG;   // Message of pop-up
    private JFrame frame;
    private JPanel panel;

    public PopUp(String title, String message) {
        TITLE = title;
        MSG = message;
        frame = new JFrame(TITLE);
        panel = new JPanel();
    }

    public void openPopUp() {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            return;
        }

        panel.setLayout(new GridLayout(2, 1, 0, 20));
        panel.setBorder(new EmptyBorder(10, 20, 20, 20));

        JLabel title = new JLabel(TITLE);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel msg = new JLabel(MSG);
        msg.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(title);
        panel.add(msg);

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
