package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.util.Duration;


import java.io.File;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public File file;
    @FXML
    public List<File> files;
    @FXML
    public ArrayList<File> filesList;
    @FXML
    public Media media;
    @FXML
    public MediaPlayer mediaPlayer;
    @FXML
    public FileChooser chooser;
    @FXML
    public FileChooser.ExtensionFilter filter;
    @FXML
    public BorderPane borderPane;
    @FXML
    public Slider musicProgress;
    @FXML
    public Button play,pause,pre,next;
    @FXML
    public ImageView imageView;
    @FXML
    public Duration playLimit;
    @FXML
    public Label time,textLimit,errorText;
    @FXML
    public Label titleName;
    @FXML
    public CheckBox checkBox;
    @FXML
    public Slider volume;

    private int count;


    @FXML
    public void SelectMusic() {
        chooser.getExtensionFilters().addAll(
                filter = new FileChooser.ExtensionFilter("Select (*.Mp3)", "*.mp3")
        );
        files = chooser.showOpenMultipleDialog(borderPane.getScene().getWindow());

        if (files != null) {
            for (File file : files) {
                filesList.add(file);
                titleName.setText(filesList.get(count).getName().replace(".mp3",""));
            }

            media = new Media(filesList.get(count).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
            TimeDuration();

        }

    }

    public  void TimeDuration(){

        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                oldValue = mediaPlayer.getTotalDuration();
                newValue = mediaPlayer.getCurrentTime();
                musicProgress.setValue((newValue.toSeconds() * 100) / oldValue.toSeconds());

                double minutes = newValue.toMinutes();
                double seconds = newValue.toSeconds() % 60;

                int m = (int)minutes;
                int s = (int)seconds;

                time.setText("Playing : " + m + " : " + s);

                double limit = oldValue.toMinutes();
                double oldSeconds = oldValue.toSeconds();

                int l = (int) limit;
                short ol = (short) oldSeconds;
                textLimit.setText("" + l + " : " + ol);

                volume.setValue(mediaPlayer.getVolume() * 100);
            }
        });
    }

    @FXML
    public void handleVolume(){
        mediaPlayer.setVolume(volume.getValue() / 100);
    }
    @FXML
    public void PlayBtn(ActionEvent event) {

            try {
                mediaPlayer.play();
            }catch (Exception e){
                errorText.setVisible(false);
            }

    }

    @FXML
    public void PauseBtn() {
        try{
            mediaPlayer.pause();
        }catch (Exception e){
            errorText.setVisible(false);
        }
    }

    @FXML
    public void NextBtn() {

        try {
            if (count < filesList.size() - 1) {
                count++;
                mediaPlayer.stop();

                media = new Media(filesList.get(count).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                titleName.setText(filesList.get(count).getName().replace(".mp3",""));

                PlayBtn(new ActionEvent());
                TimeDuration();
                handleVolume();

            } else {
                count = 0;
                mediaPlayer.stop();

                media = new Media(filesList.get(count).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                PlayBtn(new ActionEvent());
                TimeDuration();
                handleVolume();
            }
        }catch (Exception e){
            errorText.setVisible(false);
        }

    }

    @FXML
    public void previousBtn() {
        if (count > 0) {
            count--;
            mediaPlayer.stop();

            media = new Media(filesList.get(count).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            PlayBtn(new ActionEvent());
            TimeDuration();
            handleVolume();
            titleName.setText(filesList.get(count).getName().replace(".mp3",""));
        }
    }


    @FXML
    public void SliderClick(){
        try {
            double d = musicProgress.getValue();
            mediaPlayer.seek(new Duration(mediaPlayer.getCurrentTime().toSeconds() / d));
        }catch (Exception e){

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chooser = new FileChooser();
        filesList = new ArrayList<>();
        time.setText("000:00");
        textLimit.setText("000:00");
        play.setEffect(new DropShadow());


        play.setShape(new Circle(1.5));
        pre.setShape(new Circle(1.5));
        pause.setShape(new Circle(1.5));
        next.setShape(new Circle(1.5));
    }

    @FXML
    public void handleAbout(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("About");
        dialog.initOwner(borderPane.getScene().getWindow());

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("AboutProgrammer.fxml"));

        try {
            dialog.getDialogPane().setContent(loader.load());
        }catch (Exception e){
            e.getStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() & result.get() == ButtonType.CANCEL){
            dialog.close();
        }
    }

}
