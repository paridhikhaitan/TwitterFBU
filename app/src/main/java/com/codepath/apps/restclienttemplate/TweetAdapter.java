package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{
    //pass it the tweets array in the constructor
    private List<Tweet> mTweets;
    Context context;
    private OnReplyClickedListener mListener = null;

    public TweetAdapter(List<Tweet> tweets){
        mTweets = tweets;

    }

    public void setTweetClickListener(OnReplyClickedListener mListener) {
        this.mListener = mListener;
    }

    //create the viewHolder class that will actually inflate the recycler view
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView ivProfileImage;
        public TextView tvUserName;
        public TextView tvBody;
        public ImageView tweet_entity;

        public ViewHolder(View itemView){

            //need to inherit it from the itemView, then you will populate it
            //with the data you have
            super(itemView);

            //inflate the variables from the itemView that they're retrieved from.
            //use the adapter to connect them to the API variables
            //perform findViewById lookups

            ivProfileImage= (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUserName= (TextView) itemView.findViewById(R.id.tvUserName);
            tvBody= (TextView) itemView.findViewById(R.id.tvBody);
            tweet_entity= (ImageView) itemView.findViewById(R.id.tweet_entity);

            itemView.setOnClickListener(this);


        }
        //want them to be able to click on a tweet and view the whole thing

        public void onClick(View v){
            int position= getAdapterPosition();
            if(position!=RecyclerView.NO_POSITION && mListener != null) {
                Tweet tweet= mTweets.get(position);
                mListener.onReplyClicked(tweet);
            }
        }

    }

    //for each row, inflate the layout and cache reference (using room)
    //for any kind of inflatable you first need to get the context of the ViewGroup that
    //you're trying to inflate
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        context= parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);

        View tweet= inflater.inflate(R.layout.item_tweet, parent, false);
        //you pass the tweet that has just been created from the inflater
        ViewHolder viewHolder= new ViewHolder(tweet);
        return viewHolder;
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.clear();
        mTweets.addAll(list);
        notifyDataSetChanged();
    }

    //bind the various elements in the view to the retrieved values from the
    //JSONObject using the adapter


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //get the data according to the position
        Tweet tweet= mTweets.get(i);

        //populate the view
        viewHolder.tvUserName.setText(tweet.user.name);
        String populate= (tweet.body);
        viewHolder.tvBody.setText(populate);

        String url= tweet.user.profileImageUrl;

        Glide.with(context).load(url).transform(new RoundedCornersTransformation(25, 0)).into(viewHolder.ivProfileImage);

        if(tweet.hasEntities==true) {
            String entityUrl = tweet.entities.loadURL;
            if (entityUrl != null) {
                viewHolder.tweet_entity.setVisibility(View.VISIBLE);
                Glide.with(context).load(entityUrl).into(viewHolder.tweet_entity);
            }
        }else {
            viewHolder.tweet_entity.setVisibility(View.GONE);
        }


        //then we'll know
    }

    public int getItemCount(){
        return mTweets.size();
    }

    public static interface OnReplyClickedListener {
        void onReplyClicked(Tweet tweet);
    }

}
