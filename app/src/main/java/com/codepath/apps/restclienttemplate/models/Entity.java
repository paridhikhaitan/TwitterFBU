package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;


public class Entity implements Serializable {

    public String loadURL;

    public static Entity fromJSON(JSONObject jsonObject) throws JSONException {
        Entity entity= new Entity();
        entity.loadURL=jsonObject.getJSONArray("media").getJSONObject(0).getString("media_url_https");

        return entity;
    }
}
