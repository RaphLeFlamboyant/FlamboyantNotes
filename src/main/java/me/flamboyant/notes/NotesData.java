package me.flamboyant.notes;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class NotesData implements Serializable {
    private static final String fileName = "flamboyant_notes_save.json";
    private static Gson gson = new Gson();
    public HashMap<UUID, ArrayList<String>> notes = new HashMap<>();

    public boolean save() {
        String json = gson.toJson(this);

        try {
            FileWriter writer = new FileWriter(fileName);
            writer.write(json);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static NotesData load() {
        String json = "";

        try {
            StringBuilder builder = new StringBuilder();
            FileReader protoReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(protoReader);
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            json = builder.toString();
            NotesData data = gson.fromJson(json, NotesData.class);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
