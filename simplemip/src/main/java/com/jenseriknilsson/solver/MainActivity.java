package com.jenseriknilsson.solver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity{

    private boolean loaded = false;
    final String PREFS_NAME = "MyPrefsFile";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.jenseriknilsson.solver.R.layout.main_layout);

        WebView webView = (WebView) findViewById(com.jenseriknilsson.solver.R.id.webview);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("version6", true)) {
            settings.edit().putBoolean("version6", false).commit();
            webView.clearCache(true);
        }

        webView.loadUrl("http://www.simplemip.com/");

        findViewById(com.jenseriknilsson.solver.R.id.loading_layout).setVisibility(View.VISIBLE);
        findViewById(com.jenseriknilsson.solver.R.id.webview).setVisibility(View.GONE);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url);
                return false;
            }
            public void onPageFinished(WebView view, String url) {
                if(!loaded){
                    findViewById(com.jenseriknilsson.solver.R.id.webview).setVisibility(View.VISIBLE);

                    int apiVersion = android.os.Build.VERSION.SDK_INT;

                    if (apiVersion >= android.os.Build.VERSION_CODES.FROYO){
                        findViewById(com.jenseriknilsson.solver.R.id.loading_layout).animate()
                                .alpha(0f)
                                .setDuration(500)
                                .setListener(new AnimatorListenerAdapter() {
                                    public void onAnimationEnd(Animator animation) {
                                        findViewById(com.jenseriknilsson.solver.R.id.loading_layout).setVisibility(View.GONE);
                                    }
                                });
                    }
                    else{
                        findViewById(com.jenseriknilsson.solver.R.id.loading_layout).setVisibility(View.GONE);
                    }

                    loaded = true;
                }
            }
        });

    }

}