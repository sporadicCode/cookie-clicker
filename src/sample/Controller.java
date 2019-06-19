package sample;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class Controller {

    private final int width = 200;
    private final int height = 200;
    private Stage infoStage = new Stage();

    private int cookies = 0;
    private int cursors = 1;
    private int clickers = 0;
    private int bakers = 0;

    private int cursorPrice = 0;
    private int clickerPrice = 0;
    private int bakerPrice = 0;
    private int clickerDuration = 0;

    private final int clickerDurationDecrease = 100;
    private final int clickerInitialDuration = 5100;
    private final int clickerMinimumDuration = 1000;

    private final int cursorsPriceIncrease = 10;
    private final int clickersPriceIncrease = 50;
    private final int bakersPriceIncrease = 200;
    private final int bakersCookieIncrease = 15;
    private final int animatedCookieIncreaseDuration = 750;

    private Timeline clickerTimeline = new Timeline();
    private Timeline bakerTimeline = new Timeline();

    @FXML
    private Label cookiesLabel;

    private KeyFrame theOnlyBakerKeyFrame = new KeyFrame(
            Duration.millis(clickerMinimumDuration),
            event -> {
                this.cookies += bakersCookieIncrease * this.bakers;
                displayAnimatedAddedCookiesNumber(bakersCookieIncrease * this.bakers);
                cookiesLabel.setText(String.format("Cookies: %d", this.cookies));
                checkAndHideDisabledButtons();
            }
    );

    @FXML
    private Label cursorsLabel;

    @FXML
    private Label clickersLabel;

    @FXML
    private Label bakersLabel;

    @FXML
    private Label cursorPriceLabel;

    @FXML
    private Label clickerPriceLabel;

    @FXML
    private Label bakerPriceLabel;

    @FXML
    private Label priceTopicTag;

    @FXML
    private Button cursorButton;

    @FXML
    private Button clickerButton;

    @FXML
    private Button bakerButton;

    @FXML
    private Button infoButton;

    @FXML
    private Button exitButton;

    @FXML
    private Label addedCookiesLabel;

    @FXML
    private AnchorPane myAnchorPane;

    private void displayAnimatedAddedCookiesNumber(int addedCookies) {
        Label tempLabel = new Label();
        tempLabel.setLayoutX(115);
        tempLabel.setLayoutY(115);
        tempLabel.setText(String.format("+%d", addedCookies));
        tempLabel.setStyle("-fx-text-fill: #23ff23; -fx-font-family: System;"
                + "-fx-font-size: 24px; -fx-font-weight: bold;");
        myAnchorPane.getChildren().add(tempLabel);
        PathTransition transition = new PathTransition();
        transition.setNode(tempLabel);
        transition.setDuration(Duration.millis(animatedCookieIncreaseDuration));
        transition.setPath(new Line(0.0, 0.0, 0, -125));
        transition.setCycleCount(1);
        transition.play();
        transition.setOnFinished(event -> {
            myAnchorPane.getChildren().remove(tempLabel);
        });
    }

    @FXML
    private void newWindowInfo() {
        try {
            if (infoStage.isShowing()) {
                infoStage.show();
                return;
            }
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("info.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), width, 160);
            infoStage.setTitle("INFO");
            infoStage.setScene(scene);
            infoStage.setResizable(false);
            infoStage.centerOnScreen();
            infoStage.show();
        } catch (IOException e) {
            System.out.println("Something has gone terribly wrong!");
        }
    }

    @FXML
    private void handleImageClick() {
        this.cookies = this.cookies + cursors;
        cookiesLabel.setText(String.format("Cookies: %d", this.cookies));
        displayAnimatedAddedCookiesNumber(cursors);
        checkAndHideDisabledButtons();
    }

    @FXML
    private void buyCursor() {
        if (cursorPrice <= cookies) {
            this.cursors++;
            this.cookies -= cursorPrice;
            this.cursorPrice += cursorsPriceIncrease;
            cursorsLabel.setText(String.format("Cursors: %d", this.cursors));
            cursorPriceLabel.setText(String.valueOf(cursorPrice));
            cookiesLabel.setText(String.format("Cookies: %d", this.cookies));
            checkAndHideDisabledButtons();
        }

    }

    private void replaceKeyFrameNewDuration(int duration) {
        this.clickerTimeline.stop();
        this.clickerTimeline.getKeyFrames().removeAll();
        this.clickerTimeline.getKeyFrames().clear();
        KeyFrame newClickerFrame = new KeyFrame(
                Duration.millis(duration),
                event -> {
                    handleImageClick();
                }
        );
        clickerTimeline.getKeyFrames().add(newClickerFrame);
        clickerTimeline.setCycleCount(Timeline.INDEFINITE);
        clickerTimeline.play();
    }

    @FXML
    private void buyClicker() {
        if (clickerPrice <= cookies) {
            if (clickerDuration > clickerMinimumDuration) {
                this.clickers++;
                this.cookies -= clickerPrice;
                this.clickerPrice += clickersPriceIncrease;
                clickerDuration -= clickerDurationDecrease;

                clickersLabel.setText(String.format("Clickers: %d", this.clickers));
                clickerPriceLabel.setText(String.valueOf(clickerPrice));
                cookiesLabel.setText(String.format("Cookies: %d", this.cookies));

                replaceKeyFrameNewDuration(clickerDuration);
                checkAndHideDisabledButtons();
            }
        }

    }

    @FXML
    private void hireBaker() {
        if (bakerPrice <= cookies) {
            this.bakers++;
            this.cookies -= bakerPrice;
            this.bakerPrice += bakersPriceIncrease;

            bakersLabel.setText(String.format("Bakers: %d", this.bakers));
            bakerPriceLabel.setText(String.valueOf(bakerPrice));
            cookiesLabel.setText(String.format("Cookies: %d", this.cookies));

            bakerTimeline.stop();
            bakerTimeline.getKeyFrames().removeAll();
            bakerTimeline.getKeyFrames().clear();
            bakerTimeline.getKeyFrames().add(this.theOnlyBakerKeyFrame);
            bakerTimeline.setCycleCount(Timeline.INDEFINITE);
            bakerTimeline.play();
            checkAndHideDisabledButtons();
        }

    }

    @FXML
    private void closeApp() {
        Platform.exit();
    }

    private void setCommonButtonStyle(Button b) {
        b.setOnMouseEntered(e ->
                b.setStyle("-fx-background-color: white; -fx-text-fill: black; -fx-border-color: black;"));
        b.setOnMouseExited(e ->
                b.setStyle("-fx-background-color: black; -fx-border-color: white;"));
        b.setOnMousePressed(e ->
                b.setStyle("-fx-background-color: gray; -fx-border-color: white; -fx-opacity: 0.5;"));
        b.setOnMouseReleased(e ->
                b.setStyle("-fx-background-color: black; -fx-border-color: white;"));
    }

    private void checkAndHideDisabledButtons() {
        boolean tempBool = cursorPrice <= cookies;
        cursorButton.setVisible(tempBool);
        cursorPriceLabel.setVisible(tempBool);

        tempBool = clickerPrice <= cookies;
        clickerButton.setVisible(tempBool);
        clickerPriceLabel.setVisible(tempBool);

        tempBool = bakerPrice <= cookies;
        bakerButton.setVisible(tempBool);
        bakerPriceLabel.setVisible(tempBool);

        tempBool = (cursorPrice <= cookies) || (clickerPrice <= cookies) || (bakerPrice <= cookies);
        priceTopicTag.setVisible(tempBool);

    }

    public void initialize() {
        cursorPrice = cursorsPriceIncrease;
        clickerPrice = clickersPriceIncrease;
        bakerPrice = bakersPriceIncrease;
        clickerDuration = clickerInitialDuration;

        setCommonButtonStyle(infoButton);
        setCommonButtonStyle(exitButton);
        setCommonButtonStyle(cursorButton);
        setCommonButtonStyle(clickerButton);
        setCommonButtonStyle(bakerButton);

        checkAndHideDisabledButtons();
    }

}
