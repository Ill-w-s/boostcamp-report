package com.example.ilwoo.moviesearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class MovieItemView extends LinearLayout {
    TextView textView_title;
    TextView textView_pubDate;
    TextView textView_director;
    TextView textView_actor;
    RatingBar ratingBar;
    ImageView imageView;

    public MovieItemView(Context context) {
        super(context);

        init(context);
    }

    public MovieItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.movie_item, this, true);

        textView_title = (TextView) findViewById(R.id.textView_title);
        textView_pubDate = (TextView) findViewById(R.id.textView_pubDate);
        textView_director = (TextView) findViewById(R.id.textView_director);
        textView_actor = (TextView) findViewById(R.id.textView_actor);

        imageView = (ImageView) findViewById(R.id.imageView);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
    }

    public void setTitle(String title) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView_title.setText(Html.fromHtml(title, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView_title.setText(Html.fromHtml(title));
        }
        //textView_title.setText(title);
    }
    public void setPubDate(String pubDate) {
        textView_pubDate.setText(pubDate);
    }
    public void setDirector(String director) {
        textView_director.setText(director);
    }
    public void setActor(String actor) {
        textView_actor.setText(actor);
    }
    public void setRatingBar(float userRating) {
        ratingBar.setRating(userRating);
    }
    public void setImage(String url) {
        ImageLoadTask task = new ImageLoadTask(url, imageView);
        task.execute();
    }
}
