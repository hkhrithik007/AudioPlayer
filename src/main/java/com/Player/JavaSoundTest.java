package com.Player;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

public class JavaSoundTest {
  public static void main(String[] args) {
    System.out.println("JavaSoundTest starting...");
    if (args.length == 0) {
      System.out.println("Please provide the path to a WAV file as an argument.");
      return;
    }
    String filePath = args[0];
    File file = new File(filePath);
    if (!file.exists()) {
      System.out.println("File does not exist: " + filePath);
      return;
    }

    try {
      System.out.println("Attempting to play file: " + filePath);
      AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
      Clip clip = AudioSystem.getClip();
      clip.open(audioInputStream);
      clip.start();

      System.out.println("Playback started");
      // Sleep for the duration of the audio file
      Thread.sleep(clip.getMicrosecondLength() / 1000);

      clip.stop();
      clip.close();
      System.out.println("Playback finished");
    } catch (UnsupportedAudioFileException
        | IOException
        | LineUnavailableException
        | InterruptedException e) {
      System.out.println("Error playing the audio file: " + e.getMessage());
      e.printStackTrace();
    }
    System.out.println("JavaSoundTest finished");
  }
}
