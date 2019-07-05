package com.codepath.apps.restclienttemplate.models;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

//this is a class that we're making for the user
public class User implements Serializable {

    //list all attributes
    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;

    public static User fromJSON(JSONObject jsonObject) throws JSONException {
        User user= new User();
        user.uid= jsonObject.getLong("id");
        user.name= jsonObject.getString("name");
        user.profileImageUrl= jsonObject.getString("profile_image_url");
        user.screenName= jsonObject.getString("screen_name");

        return user;

    }
}
