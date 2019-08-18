package ir.farzinnasiri.Client.audio;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class Sound {

    private MediaPlayer mediaPlayer;


    public Sound(Media media){
        mediaPlayer = new MediaPlayer(media);



    }




    public void play(){
            mediaPlayer.play();

    }

    public void setLoop(boolean looping){
        if(looping){
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

        }else{
            mediaPlayer.setCycleCount(0);
        }

    }

    public void pause(){
        mediaPlayer.pause();
    }

    public void stop(){
        mediaPlayer.stop();
    }
    public void reset(){
        mediaPlayer.seek(Duration.ZERO);
    }

    public void setVolume(int volume){
        mediaPlayer.setVolume(volume/10.0);

    }

    public double getVolume(){
        return mediaPlayer.getVolume();
    }

    public boolean isPlaying(){
        return mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING);
    }
}
