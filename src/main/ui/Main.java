package ui;

import java.io.FileNotFoundException;

// Main class for running the data analysis application
public class Main {

    //EFFECTS: runs the data analysis application
    public static void main(String[] args) {
        try {
            new DataAnalysisApp();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot run application: file not found");
        }
    }
}
