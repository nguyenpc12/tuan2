
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
public class Clock extends JFrame {
    private JLabel timeLabel;
    private JTextField timeZoneTextField;
    private JButton resetButton;
    private SimpleDateFormat timeFormat;
    public Clock() {
        setTitle("World Clock");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        timeLabel = new JLabel();
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeZoneTextField = new JTextField(5);
        resetButton = new JButton("Create");
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+7")); // Initial timezone
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("Timezone Offset:"));
        inputPanel.add(timeZoneTextField);
        inputPanel.add(resetButton);
        add(timeLabel, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.NORTH);
        updateTime();
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int offset = Integer.parseInt(timeZoneTextField.getText());
                    String customTimeZoneID = "GMT" + (offset >= 0 ? "+" : "") + offset;
                    new Clock(customTimeZoneID).setVisible(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Clock.this, "Invalid timezone offset", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        Thread updateTimeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    updateTime();
                    try {
                        Thread.sleep(1000); // Update every second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        updateTimeThread.start();
    }

    public Clock(String timeZoneID) {
        this();
        timeFormat.setTimeZone(TimeZone.getTimeZone(timeZoneID));
    }
    private void updateTime() {
        Date currentTime = new Date();
        String formattedTime = timeFormat.format(currentTime);
        timeLabel.setText(formattedTime);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Clock().setVisible(true);
            }
        });
    }
}