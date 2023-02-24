package persistence;

import model.DataBase;
import model.DataSet;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// a reader for DataBase that reads DataBase data from JSON file (implementation based on JSON serialization demo)
public class JsonReader {
    private final String source;    // source for the JSON file

    // EFFECTS: constructs a reader with the passed-in source
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads a database from json file; will throw exception if error occurs when being read
    public DataBase read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseDataBase(jsonObject);
    }

    // EFFECTS: reads source file as a String and returns String
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(contentBuilder::append);
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses DataBase from the JSONObject and returns DataBase
    private DataBase parseDataBase(JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        DataBase dataBase = new DataBase(name);
        addDataSets(dataBase, jsonObject);
        return dataBase;
    }

    // MODIFIES: dataBase
    // EFFECTS: parses DataSets from JSONObject and adds them to the DataBase
    private void addDataSets(DataBase dataBase, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("listOfDatasets");
        for (Object json : jsonArray) {
            JSONObject nextDataSet = (JSONObject) json;
            addDataSet(dataBase, nextDataSet);
        }
    }

    // MODIFIES: dataBase
    // EFFECTS: parses the fields for DataSet from JSONObject and adds them to the DataSet
    private void addDataSet(DataBase dataBase, JSONObject jsonObject) {
        String listName = jsonObject.getString("listName");
        int listLength = jsonObject.getInt("listLength");
        double listMean = jsonObject.getDouble("listMean");
        double listMedian = jsonObject.getDouble("listMedian");
        double listSD = jsonObject.getDouble("listSD");
        double listVar = jsonObject.getDouble("listVar");
        ArrayList<Double> numList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("numList");
        for (Object number : jsonArray) {
            double nextNum = new Double(number.toString());
            numList.add(nextNum);
        }
        dataBase.addList(listName);
        DataSet dataSet = dataBase.getData(dataBase.getListLength() - 1);
        dataSet.setListLength(listLength);
        dataSet.setListMean(listMean);
        dataSet.setListMedian(listMedian);
        dataSet.setListSD(listSD);
        dataSet.setListVar(listVar);
        dataSet.setNumList(numList);
    }
}
