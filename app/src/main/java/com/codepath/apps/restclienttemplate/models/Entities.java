package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Entities implements Serializable {

    public String loadURL=null;

    public static Entities fromJSON(JSONObject object) throws JSONException {
        Entities entity= new Entities();
            entity.loadURL = object.getJSONArray("media").getJSONObject(0).getString("media_url_https");

        return entity;

    }

}
