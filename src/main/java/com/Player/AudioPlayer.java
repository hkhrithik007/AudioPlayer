package com.Player;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class AudioPlayer {
  private AdvancedPlayer player;
  private Thread playerThread;
  private volatile boolean isPlaying = false;

  public void play(String filePath) {
    System.out.println("Attempting to play file: " + filePath);
    try {
      File file = new File(filePath);
      System.out.println("File size: " + file.length() + " bytes");

      FileInputStream fileInputStream = new FileInputStream(file);
      BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

      player = new AdvancedPlayer(bufferedInputStream);

      player.setPlayBackListener(
          new PlaybackListener() {
            @Override
            public void playbackStarted(PlaybackEvent evt) {
              System.out.println("Playback started at frame: " + evt.getFrame());
              isPlaying = true;
            }

            @Override
            public void playbackFinished(PlaybackEvent evt) {
              System.out.println("Playback finished at frame: " + evt.getFrame());
              isPlaying = false;
            }
          });

      playerThread =
          new Thread(
              () -> {
                try {
                  System.out.println("Starting playback");
                  player.play();
                  System.out.println("Play method finished");
                  // Add a small delay before closing
                  Thread.sleep(5000);
                } catch (JavaLayerException | InterruptedException e) {
                  System.out.println("Error during playback: " + e.getMessage());
                  e.printStackTrace();
                }
              });

      playerThread.start();
    } catch (Exception e) {
      System.out.println("Error initializing the audio player: " + e.getMessage());
      e.printStackTrace();
    }
  }

  public void stop() {
    System.out.println("Stopping playback");
    if (player != null) {
      player.close();
      isPlaying = false;
    }
    if (playerThread != null) {
      playerThread.interrupt();
    }
  }

  public static void main(String[] args) {
    System.out.println("AudioPlayer starting...");
    if (args.length == 0) {
      System.out.println("Please provide the path to an MP3 file as an argument.");
      return;
    }
    String filePath = args[0];
    File file = new File(filePath);
    if (!file.exists()) {
      System.out.println("File does not exist: " + filePath);
      return;
    }
    AudioPlayer audioPlayer = new AudioPlayer();
    audioPlayer.play(filePath);

    // Keep the main thread alive
    try {
      System.out.println("Main thread waiting for playback to finish");
      while (audioPlayer.isPlaying || !audioPlayer.playerThread.isAlive()) {
        Thread.sleep(1000);
        System.out.println("Playback in progress...");
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("AudioPlayer finished");
  }
}
