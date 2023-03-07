package persistence;

import org.json.JSONObject;

// interface for JSON reader and writer (based on JSON serialization demo):
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writable {
    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
