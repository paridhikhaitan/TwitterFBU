package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements TweetAdapter.OnReplyClickedListener {

    private TwitterClient client;
    private TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    int POST_A_TWEET = 1;
    int REPLY_TO_A_TWEET = 2;
    private SwipeRefreshLayout swipeContainer;
    MenuItem miActionProgressItem;
    private EndlessRecyclerViewScrollListener scrollListener;
    long maxId=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        // Toast.makeText(this, "Timeline Activity has been created", Toast.LENGTH_LONG).show();

        client = TwitterApp.getRestClient(this);

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("Twitter");
        setSupportActionBar(toolbar);

        //find the RecyclerView
        //rvTweets
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);

        //init the arrayList (data source)
        tweets = new ArrayList<>();
        //construct the adapter from the data source
        tweetAdapter = new TweetAdapter(tweets);
        tweetAdapter.setTweetClickListener(this);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);

        //RecyclerView setup (layout manager, use adapter
        rvTweets.setLayoutManager(linearLayoutManager);

        //refresh the current timeline
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                tweets.clear();
                tweetAdapter.clear();
                populateTimeline(maxId);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        //set the adapter
        rvTweets.setAdapter(tweetAdapter);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimelineActivity.this, ComposeActivity.class);
                startActivityForResult(intent, POST_A_TWEET);
            }
        });

        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        rvTweets.addOnScrollListener(scrollListener);

        populateTimeline(maxId);
    }

    public void loadNextDataFromApi(int offset) {
        maxId= tweets.get(tweets.size()-1).uid;
        populateTimeline(maxId);
    }
/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);


        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }
*/

    @Override
    public void onReplyClicked(Tweet tweet) {
        Intent intent = new Intent(this, TweetItemActivity.class);
        intent.putExtra("tweet", tweet);
        startActivityForResult(intent, REPLY_TO_A_TWEET);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == POST_A_TWEET && resultCode == RESULT_OK) {
            Tweet tweet = (Tweet) data.getSerializableExtra("newTweet");
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }
        if (requestCode == REPLY_TO_A_TWEET && resultCode == RESULT_OK) {
            Tweet tweet = (Tweet) data.getSerializableExtra("replyTweet");
            tweets.add(0, tweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
        }

    }

    private void populateTimeline(long maxId) {
        //showProgressBar();
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                //iterate through the JSONArray
                //for each entry, deseraillize the JSON object

                for (int i = 0; i < response.length(); i++) {
                    //convert each object to a Tweet model
                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJSON(response.getJSONObject(i));
                        //add that Tweet model to our data source
                        tweets.add(tweet);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    tweetAdapter.notifyDataSetChanged();

                   // hideProgressBar();

                }
                //notify the adapter
                swipeContainer.setRefreshing(false);
                //Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                Log.d("TwitterClient", errorResponse.toString());TimelineActivity
                //hideProgressBar();
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }


}
