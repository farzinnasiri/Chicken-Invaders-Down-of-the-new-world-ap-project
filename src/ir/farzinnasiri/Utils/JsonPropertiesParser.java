package ir.farzinnasiri.Utils;

import com.google.gson.Gson;

public class JsonPropertiesParser {
    private Gson gson;

    public  JsonPropertiesParser(){
        gson = new Gson();
    }

    public String serializeToJson(String name, PlayerProperties playerProperties) {


        return gson.toJson(playerProperties);



    }

    public PlayerProperties deserializeFromJson(String json) {

        PlayerProperties playerProperties = gson.fromJson(json, PlayerProperties.class);
        return playerProperties;

    }






}
