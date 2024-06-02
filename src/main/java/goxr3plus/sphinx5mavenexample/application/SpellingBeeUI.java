package main.java.goxr3plus.sphinx5mavenexample.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// SpellingBeeUI.java

public class SpellingBeeUI extends JFrame {
    private JButton startButton;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private TextToSpeech tts;

    public SpellingBeeUI() {
        tts = new TextToSpeech();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Create title label
        titleLabel = new JLabel("SPELLING BEE", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        add(titleLabel);

        // Create subtitle label
        subtitleLabel = new JLabel("WITH VOICE RECOGNITION", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        add(subtitleLabel);

        // Create instructions label
        JLabel instructionsLabel = new JLabel("\nINSTRUCTIONS:", SwingConstants.CENTER);
        instructionsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(instructionsLabel);

        JLabel listenLabel = new JLabel("\nLISTEN FOR THE WORD");
        listenLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(listenLabel);

        JLabel spellLabel = new JLabel("\nSPELL IT OUT EACH LETTER AT A TIME");
        spellLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(spellLabel);

        JLabel clearLabel = new JLabel("\nTRY TO BE AS CLEAR AS POSSIBLE");
        clearLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(clearLabel);

        JLabel goodLuckLabel = new JLabel("GOOD LUCK!");
        goodLuckLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        add(goodLuckLabel);

        // Create start button
        startButton = new JButton("NEXT");
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        add(startButton);

        setVisible(true);
    }

    private void startGame() {
        // Close the current SpellingBeeUI window
        dispose();

        // Create the MainGameUI window
        MainGame mainGameUI = new MainGame();
        mainGameUI.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SpellingBeeUI();
            }
        });
    }
}
