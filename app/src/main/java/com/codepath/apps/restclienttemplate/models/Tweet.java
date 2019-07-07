package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Tweet implements Serializable {

    //list all the attributes I want to store
    public String body;
    public long uid;

    public User user;
   // public Entities entities;
    public String createAt;
    public int retweetCount;
    public int likeCount;
   // public boolean hasEntities;
    public Entity entity;
    public boolean hasEntities;

    public static Tweet fromJSON (JSONObject jsonObject) throws JSONException {
        Tweet tweet= new Tweet();

        //you're creating a tweet object with three fields, and setting each of
        //these fields using values that you're getting back from the JSONObject
        tweet.body= jsonObject.getString("text");
        tweet.uid= jsonObject.getLong("id");
        tweet.createAt= jsonObject.getString("created_at");
        tweet.retweetCount=jsonObject.getInt("retweet_count");
        tweet.likeCount= jsonObject.getInt("favorite_count");
       // tweet.hasEntities= false;

        JSONObject entityObject= jsonObject.getJSONObject("entities");
        if(entityObject.has("media")){
            JSONArray mediaEndpoint= entityObject.getJSONArray("media");
            if(mediaEndpoint!=null && mediaEndpoint.length()!=0){
                tweet.entity= Entity.fromJSON(jsonObject.getJSONObject("entities"));
                tweet.hasEntities=true;
            }else{
                tweet.hasEntities=false;
            }
        }

        //tweet.entity= Entity.fromJSON()

        //this actually connects the user with the tweet-> then when I call the tweet all the user
        //information will come directly
        tweet.user= User.fromJSON(jsonObject.getJSONObject("user"));
/*

        JSONObject entities = jsonObject.getJSONObject("entities");

        if(entities.has("media")) {
            JSONArray media = entities.getJSONArray("media");

            if(media != null && media.length() > 0){
                tweet.entities = Entities.fromJSON(jsonObject.getJSONObject("entities"));
                tweet.hasEntities=true;
            }
        }
*/



        //tweet.entities=jsonObject.getJSONArray("entities");
        //tweet.entities=Entities.fromJSON()

        return tweet;
    }
}
