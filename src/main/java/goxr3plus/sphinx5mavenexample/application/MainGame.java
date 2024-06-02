package main.java.goxr3plus.sphinx5mavenexample.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainGame extends JFrame implements ActionListener, LetterRecognition.LetterRecognitionCallback {

    private TextToSpeech tts;
    private LetterRecognition letterRecognition;
    private JLabel wordLabel;
    private JTextField inputField;
    private JButton startButton;
    private JButton answerButton;
    private JButton replayButton;
    private String currentWord;
    private List<String> wordList;

    public MainGame() {
        super("Spelling Bee Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        tts = new TextToSpeech();

        // Create game components
        wordLabel = new JLabel("Waiting for start...");
        wordLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(wordLabel);

        startButton = new JButton("Start Game");
        startButton.addActionListener(this);
        add(startButton);

        inputField = new JTextField(20);
        inputField.setFont(new Font("Arial", Font.PLAIN, 18));
        inputField.setEnabled(false);
        add(inputField);

        answerButton = new JButton("Answer");
        answerButton.addActionListener(this);
        answerButton.setEnabled(false);
        add(answerButton);

        replayButton = new JButton("Replay");
        replayButton.addActionListener(this);
        replayButton.setEnabled(false);
        add(replayButton);

        // Initialize word list
        wordList = new ArrayList<>();
        wordList.add("STINKY");
        wordList.add("FROG");
        wordList.add("CAT");
        wordList.add("DOG");
        wordList.add("HOUSE");

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startGame();
        } else if (e.getSource() == answerButton) {
            // Get the user's input
            String userInput = inputField.getText();

            // Check if the input is empty
            if (userInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a word before answering.");
                return; // Exit the method if input is empty
            }

            // Check if the input is correct
            if (userInput.equalsIgnoreCase(currentWord)) {
                tts.speak("Correct, Game Over", 1.0f, true, false);
                dispose();
                new MessageScreen("Correct!"); // Display correct message in a new screen
            } else {
                tts.speak("Incorrect, Game Over", 1.0f, true, false);
                dispose();
                new MessageScreen("Incorrect. The correct answer is " + currentWord); // Display incorrect message in a new screen
            }
        } else if (e.getSource() == replayButton) {
            // Replay the word using TTS
            tts.speak(currentWord, 1.0f, true, true);
        }
    }

    private void startGame() {
        // Get a random word from the list
        currentWord = wordList.get((int) (Math.random() * wordList.size()));

        // Speak the word using TTS
        tts.speak(currentWord, 1.0f, true, true);

        // Update the word label
        wordLabel.setText("Spell the word: " + currentWord);

        // Enable input field and buttons
        inputField.setEnabled(true);
        answerButton.setEnabled(true);
        replayButton.setEnabled(true);

        // Initialize letter recognition if not already initialized
        if (letterRecognition == null) {
            try {
                letterRecognition = new LetterRecognition();
                letterRecognition.setLetterRecognitionCallback(this);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error initializing speech recognition: " + ex.getMessage());
                // Handle error: log, display message, or take appropriate action
            }
        } else {
            // Start the recognizer if it's already initialized
            letterRecognition.setLetterRecognitionCallback(this);
        }
    }
    @Override
    public void onLetterRecognized(String recognizedLetter) {
        // Check if the recognized letter is not <unk>
        if (!recognizedLetter.equals("<unk>")) {
            // Append recognized letter to input field
            inputField.setText(inputField.getText() + recognizedLetter);
        }
    }

    public class MessageScreen extends JFrame {
        private JLabel messageLabel;
        private JButton quitButton;

        public interface MessageScreenCallback {
            void updateMessage(String message);
        }

        public MessageScreen(String message) {
            super("Message");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 200);
            setLocationRelativeTo(null);
            setLayout(new BorderLayout());

            messageLabel = new JLabel(message);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 18));
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            add(messageLabel, BorderLayout.CENTER);

            quitButton = new JButton("Quit");
            quitButton.addActionListener(e -> {
                System.exit(0);
            });
            add(quitButton, BorderLayout.SOUTH);

            setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGame());
    }
}
