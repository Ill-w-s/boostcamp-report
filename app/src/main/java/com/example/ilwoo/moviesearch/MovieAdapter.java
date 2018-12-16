package com.example.ilwoo.moviesearch;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.net.URL;
import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter {
    ArrayList<MovieItem> items;

    Context context;

    public MovieAdapter(ArrayList<MovieItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MovieItemView view = new MovieItemView(context);
        MovieItem item = items.get(position);

        float userRating = Float.parseFloat(item.userRating) / 2;

        view.setTitle(item.title);
        view.setPubDate(item.pubDate);
        view.setDirector(item.director);
        view.setActor(item.actor);
        view.setRatingBar(userRating);
        view.setImage(item.image);

        return view;
    }


}
