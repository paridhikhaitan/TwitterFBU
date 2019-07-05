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
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TweetItemActivity extends AppCompatActivity {

    Tweet tweet;
    ImageView profileImage;
    TextView userName;
    TextView userHandle;
    TextView tweetContent;
    TextView createAt;
    TextView numLikes;
    TextView numRetweets;
    EditText tweetReply;
    Button retweet;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_item);

        client = TwitterApp.getRestClient(this);

        tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        profileImage = (ImageView) findViewById(R.id.item_profile_image);
        userName = (TextView) findViewById(R.id.item_user_name);
        userHandle = (TextView) findViewById(R.id.item_user_handle);
        tweetContent = (TextView) findViewById(R.id.item_tweet_content);
        createAt = (TextView) findViewById(R.id.item_createdAt);
        numLikes = (TextView) findViewById(R.id.numOfLikes);
        numRetweets = (TextView) findViewById(R.id.numOfRetweets);
        tweetReply = (EditText) findViewById(R.id.userRetweet);
        retweet = (Button) findViewById(R.id.buttonSendReply);


        String url = tweet.user.profileImageUrl;

        Glide.with(this)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(profileImage);

        userName.setText(tweet.user.name);
        userHandle.setText("@" + (tweet.user.screenName));
        tweetContent.setText(tweet.body);
        createAt.setText((tweet.createAt).substring(0, 20));
        numLikes.setText((tweet.likeCount) + " Likes");
        numRetweets.setText((tweet.retweetCount) + " Retweets");

        retweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ("@" + tweet.user.screenName) + " " + tweetReply.getText().toString();
                long id = tweet.uid;
                replyTweet(message, id);
            }
        });

    }

    //add the likes feature

    //add the retweet feature

    //add the replies feature
    public void replyTweet(String message, long id) {
        client.sendTweet(message, id, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Tweet tweet = null;
                try {
                    tweet = Tweet.fromJSON(response);
                    Intent goBack = new Intent();
                    goBack.putExtra("replyTweet", tweet);
                    setResult(RESULT_OK, goBack);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
