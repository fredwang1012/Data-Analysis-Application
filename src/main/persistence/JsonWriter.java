package persistence;

import model.DataBase;
import org.json.JSONObject;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// a writer for DataBase that writes DataBase data to JSON file (implementation based on JSON serialization demo)
public class JsonWriter {
    private static final int TAB = 4;       // indent factor for JSON
    private PrintWriter writer;             // JSON writer for DataBase
    private final String destination;       // destination for the JSON file

    // EFFECTS: constructs a writer with the passed-in destination
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens the writer and throws FileNotFoundException if the destination file cannot be opened
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }

    // MODIFIES: this
    // EFFECTS: writes JSON version of the database to file
    public void write(DataBase dataBase) {
        JSONObject json = dataBase.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes the writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes String to file
    public void saveToFile(String json) {
        writer.print(json);
    }
}
