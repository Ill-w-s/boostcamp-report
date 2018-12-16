package com.example.ilwoo.moviesearch;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.provider.Browser;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {
    TextView textView;
    ImageView imageView;
    WebView webView;

    String urlStr;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent passedIntent = getIntent();

        urlStr = passedIntent.getStringExtra("url");
        title = passedIntent.getStringExtra("title");

        textView = (TextView) findViewById(R.id.textView_web_toolbar);
        imageView = (ImageView) findViewById(R.id.imageView_web_toolbar);

        Toolbar wedToolbar = (Toolbar) findViewById(R.id.toolbar_web);
        setSupportActionBar(wedToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorStasus));
        }


        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                textView.setText("http:/ ...");

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                textView.setText("https:/ ...");
                imageView.setVisibility(View.VISIBLE);

                super.onPageFinished(view, url);
            }
        });

        webView.loadUrl(urlStr);

        Button button = (Button) findViewById(R.id.btn_web_share);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int curId = item.getItemId();
        switch (curId) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_refresh:
                webView.reload();
                return true;
            case R.id.menu_another:
                Intent anotherIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl()));
                startActivity(anotherIntent);
                return true;
            case R.id.menu_homeAdd:
                addShortcut();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void share () {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, webView.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, webView.getUrl());
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Intent chooser = Intent.createChooser(intent,"공유");

        startActivity(chooser);
    }

    public void addShortcut() {
        String tmpTitle = title.replace("<b>", "");
        tmpTitle = tmpTitle.replace("</b>", "");
        String name = tmpTitle + " : 네이버 통합검색";

        Intent action= new Intent(Intent.ACTION_VIEW, Uri.parse(webView.getUrl()));

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Intent intent = new Intent();
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                    Intent.ShortcutIconResource.fromContext(getApplicationContext(), R.drawable.naver_icon));
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, action);
            getApplicationContext().sendBroadcast(intent);
        } else {
            ShortcutManager shortcutManager = getApplicationContext().getSystemService(ShortcutManager.class);
            if (shortcutManager.isRequestPinShortcutSupported()) {
                ShortcutInfo pinShortcutInfo = new ShortcutInfo.Builder(getApplicationContext(), name)
                        .setIntent(action)
                        .setShortLabel(name)
                        .setIcon(Icon.createWithResource(getApplicationContext(), R.drawable.naver_icon)).build();
                Intent pinnedShortcutCallbackIntent = shortcutManager.createShortcutResultIntent(pinShortcutInfo);
                PendingIntent successCallback = PendingIntent.getBroadcast(getApplicationContext(), 0, pinnedShortcutCallbackIntent, 0);

                shortcutManager.requestPinShortcut(pinShortcutInfo, successCallback.getIntentSender());
            }
        }
    }


}
