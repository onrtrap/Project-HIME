package org.avida.hime.hime.SQL;

import org.avida.hime.hime.sheets.SheetAPI;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.avida.hime.hime.Main.readConfig;

public class DatabaseUpdater {
    static JSONObject json;

    static {
        try {
            json = readConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static final String DB_URL = json.getString("DB");
    static final String USER = json.getString("SQLUsername");
    static final String PASS = json.getString("SQLPassword");
    static final String QUERY = "SELECT * FROM archetypes";
    public DatabaseUpdater(){

    }

    public void updateArchetypes() throws IOException {
       SheetAPI sheet = new SheetAPI("11rKUXUkjjMiaMxa-EGanQSwZW92W_39MIVkselmIl40");

    }
}
