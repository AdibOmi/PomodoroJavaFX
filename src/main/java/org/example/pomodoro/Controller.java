package org.example.pomodoro;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.layout.AnchorPane;

public class Controller {

    @FXML
    private Label timerLabel;

    @FXML
    private Label activityLabel;

    @FXML
    private Button startButton;

    @FXML
    private Button resetButton;

    @FXML
    private AnchorPane rootPane;


    private int pomodoroDuration = 15; // set to 1 minute to for convenience
    private int shortBreakDuration = 1 * 60; // set to 1 minute to for convenience
    private int longBreakDuration = 2 * 60; // set to 2 minute to for convenience

    private int timeSeconds = pomodoroDuration;
    private Timeline timeline;

    private int shortBreakCounter = 0;

    public void initialize() {
        updateActivityLabel("Pomodoro");
        timerLabel.setText(formatTime(timeSeconds));
        setRootPaneColor("#DC143C");
    }

    @FXML
    private void handleStartButton(ActionEvent event) {
        if (timeline == null) {
            startButton.setText("Pause");
            timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(1),
                            event1 -> {
                                timeSeconds--;
                                timerLabel.setText(formatTime(timeSeconds));
                                if (timeSeconds <= 0) {
                                    stopTimer();
                                    if (shortBreakCounter < 1) {        //set to 1 for convenience
                                        shortBreakCounter++;
                                        goToShortBreak();
                                    } else {
                                        shortBreakCounter = 0;
                                        goToLongBreak();
                                    }
                                }
                            }));
            timeline.play();
        } else {

            if (timeline.getStatus() == Timeline.Status.RUNNING) {
                timeline.pause();
                startButton.setText("Resume");
            } else {
                timeline.play();
                startButton.setText("Pause");
            }
        }
    }

    @FXML
    private void handleResetButton(ActionEvent event) {
        stopTimer();
        shortBreakCounter = 0;
        timeSeconds = pomodoroDuration;
        timerLabel.setText(formatTime(timeSeconds));
        startButton.setText("Start");
        updateActivityLabel("Pomodoro");
    }

    private String formatTime(int timeSeconds) {
        int minutes = timeSeconds / 60;
        int seconds = timeSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
            timeline = null;
        }
    }

    private void goToShortBreak() {
        timeSeconds = shortBreakDuration;
        timerLabel.setText(formatTime(timeSeconds));
        updateActivityLabel("Short Break");
        setRootPaneColor("#87CEEB");
        startButton.setText("Start");
    }

    private void goToLongBreak() {
        timeSeconds = longBreakDuration;
        timerLabel.setText(formatTime(timeSeconds));
        updateActivityLabel("Long Break");
        setRootPaneColor("blue");
        startButton.setText("Start");
    }


    private void updateActivityLabel(String activity) {
        activityLabel.setText(activity);
    }

    private void setRootPaneColor(String color) {
        rootPane.setStyle("-fx-background-color: " + color + ";");
    }
}
