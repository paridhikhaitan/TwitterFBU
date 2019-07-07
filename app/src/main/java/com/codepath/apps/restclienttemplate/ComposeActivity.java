package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    ImageView profilePicture;
    EditText tweetContent;
    Button tweetButton;
    TextView composeUsername;
    TextView composeHandle;
    String userTweet;
    TextView charCount;
    private TwitterClient client;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        //finding all the views
        profilePicture= (ImageView) findViewById(R.id.profile_picture);
        tweetContent= (EditText) findViewById(R.id.tweet_content);
        tweetButton= (Button) findViewById(R.id.tweet_button);
        composeUsername= (TextView) findViewById(R.id.compose_username);
        composeHandle= (TextView) findViewById(R.id.compose_handle);
        //get the profile image of the current user and set it

        //get the text entered by the user

//        getSupportActionBar().setTitle("compose");

        client= TwitterApp.getRestClient(this);
        getCurrentUser();


        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userTweet= tweetContent.getText().toString();
                //Toast.makeText(ComposeActivity.this, "This is user tweet"+userTweet, Toast.LENGTH_SHORT).show();
                postTweet(userTweet);
            }
        });

    }

    public void postTweet(String userTweet){
        client.sendTweet(userTweet,0, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet tweet= null;
                try {
                    tweet= Tweet.fromJSON(response);
                    Intent resultIntent= new Intent();
                    resultIntent.putExtra("newTweet", tweet);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void getCurrentUser(){
        client.getCurrentUser(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    User user= User.fromJSON(response);
                    composeUsername.setText(user.name);
                    composeHandle.setText("@"+user.screenName);
                    String url= user.profileImageUrl;
                    Glide.with(ComposeActivity.this).load(url)
                            .apply(RequestOptions.circleCropTransform()).into(profilePicture);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
