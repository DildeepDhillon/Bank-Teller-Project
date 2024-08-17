package persistence;

import org.json.JSONObject;

// ** Writable class is modeled from the classes provided in GitHub below
// ** https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo
public interface Writable {
    //EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
