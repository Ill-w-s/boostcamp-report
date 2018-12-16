package com.example.ilwoo.moviesearch;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView listView_movie;
    EditText editText;

    String clientId = "YpeAYDSS3KVfqGFeOiP4";
    String clientSecret = "FQMO0zKJ46";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView_movie = (ListView) findViewById(R.id.listView_movie);
        editText = (EditText) findViewById(R.id.editText);

        Button button = (Button) findViewById(R.id.button_search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().equals("")) {
                    showCustomToast("검색어를 입력해주세요");
                } else {
                    String searchWord = editText.getText().toString();
                    sendRequest(searchWord);
                }

            }
        });

        if(AppHelper.requestQueue == null) {
            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            showCustomToast("휴대폰의 네트워크 연결상태를 확인해주세요\n통신사/와이파이 상태가 안정적이지 않아요..");
        }
    }


    public void sendRequest(final String searchWord) {
        String text = null;

        try {
            text = URLEncoder.encode( searchWord ,"UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        final String url = "https://openapi.naver.com/v1/search/movie.json?query=" + text+ "&display=100&start=1";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean result = processResponse(response);
                        if (!result) {
                            showCustomToast("'"+searchWord+"' 검색결과는 없습니다..");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Naver-Client-Id", clientId);
                params.put("X-Naver-Client-Secret", clientSecret);

                return params;
            }
        };

        AppHelper.requestQueue.add(request);
    }

    public boolean processResponse(String response) {
        Gson gson = new Gson();
        MovieSearchResult movieSearchResult = gson.fromJson(response, MovieSearchResult.class);

        if (movieSearchResult != null && movieSearchResult.items.size() != 0) {
            final MovieAdapter adapter = new MovieAdapter(movieSearchResult.items, getApplicationContext());
            listView_movie.setAdapter(adapter);

            listView_movie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MovieItem item = (MovieItem) adapter.getItem(position);
                    Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);

                    intent.putExtra("url", item.link);
                    intent.putExtra("title", item.title);
                    startActivity(intent);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    public void showCustomToast(String text) {
        Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        LayoutInflater inflater = getLayoutInflater();
        View view =
                inflater.inflate(R.layout.custom_toast,
                        (ViewGroup)findViewById(R.id.container_toast));
        TextView txtView = view.findViewById(R.id.textView_toast);
        txtView.setText(text);
        toast.setView(view);
        toast.show();
    }

}
