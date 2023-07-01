package src;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    public static void WriteToLog(String log) {
        String filePath = "Game.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(log);
            writer.newLine(); // Add a new line after the text if desired
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}