package main.java.goxr3plus.sphinx5mavenexample.application;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.LiveSpeechRecognizer;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LetterRecognition {
    public interface LetterRecognitionCallback {
        void onLetterRecognized(String recognizedLetter);
    }

    private LetterRecognitionCallback callback;
    private LiveSpeechRecognizer recognizer;
    private Logger logger = Logger.getLogger(getClass().getName());
    private boolean ignoreSpeechRecognitionResults = false;
    private boolean speechRecognizerThreadRunning = false;
    private ExecutorService eventsExecutorService = Executors.newFixedThreadPool(2);
    private List<String> recognizedLetters = new ArrayList<>(); // List to store recognized letters

    public LetterRecognition() {
        logger.log(Level.INFO, "Loading Speech Recognizer...");

        Configuration configuration = new Configuration();
        configuration.setAcousticModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us");
        configuration.setDictionaryPath("resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict");
        configuration.setLanguageModelPath("resource:/edu/cmu/sphinx/models/en-us/en-us.lm.bin");
        configuration.setGrammarPath("resource:/grammars");
        configuration.setGrammarName("letters");
        configuration.setUseGrammar(true);

        try {
            recognizer = new LiveSpeechRecognizer(configuration);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        startSpeechRecognition(1000);
    }

    public void setLetterRecognitionCallback(LetterRecognitionCallback callback) {
        this.callback = callback;
    }

    public synchronized void startSpeechRecognition(int delayMilliseconds) {
        if (speechRecognizerThreadRunning) {
            logger.log(Level.INFO, "Speech Recognition Thread already running...");
        } else {
            eventsExecutorService.submit(() -> {
                speechRecognizerThreadRunning = true;
                ignoreSpeechRecognitionResults = false;

                recognizer.startRecognition(true);
                logger.log(Level.INFO, "You can start to speak...");

                try {
                    while (speechRecognizerThreadRunning) {
                        SpeechResult speechResult = recognizer.getResult();

                        if (!ignoreSpeechRecognitionResults && speechResult != null) {
                            String recognizedLetter = speechResult.getHypothesis();
                            recognizedLetters.add(recognizedLetter); // Add recognized letter to the list
                            logger.log(Level.INFO, "Recognized letter: " + recognizedLetter);
                            if (callback != null) {
                                callback.onLetterRecognized(recognizedLetter);
                            }
                        }

                        // Introduce delay here
                        Thread.sleep(delayMilliseconds);
                    }
                } catch (Exception ex) {
                    logger.log(Level.WARNING, null, ex);
                    speechRecognizerThreadRunning = false;
                }

                logger.log(Level.INFO, "SpeechThread has exited...");
            });
        }
    }


    public synchronized void stopSpeechRecognition() {
        speechRecognizerThreadRunning = false;
        recognizer.stopRecognition();
    }

    public void ignoreSpeechRecognitionResults() {
        ignoreSpeechRecognitionResults = true;
    }

    public void stopIgnoreSpeechRecognitionResults() {
        ignoreSpeechRecognitionResults = false;
    }
    public synchronized void stopRecognition() {
        if (speechRecognizerThreadRunning) {
            speechRecognizerThreadRunning = false;
            recognizer.stopRecognition();
            logger.log(Level.INFO, "Speech Recognition stopped.");
        } else {
            logger.log(Level.INFO, "Speech Recognition is not running.");
        }
    }

}