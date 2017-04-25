package com.bian.testweb;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {

    ViewGroup controlView;

    EditText editText;
    WebView webView;

    Button btnOpen;
    Button btnJs;

    TextView tvLoading;

    FloatingActionButton actionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edittext);
        webView = (WebView) findViewById(R.id.webview);

        btnOpen = (Button) findViewById(R.id.btnopen);
        btnOpen.setOnClickListener(this);
        btnJs = (Button) findViewById(R.id.btnjs);
        controlView = (ViewGroup) findViewById(R.id.contorlview);
        controlView.setVisibility(View.GONE);

        tvLoading = (TextView) findViewById(R.id.tv_loading);
        actionButton = (FloatingActionButton) findViewById(R.id.floatingbtn);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        initView();
    }

    public void initView() {
        locateEdit();

        btnJs.setTextColor(0xfff0f0f0);
        btnJs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.getSettings().getJavaScriptEnabled()) {
                    btnJs.setTextColor(0xfff0f0f0);
                    webView.getSettings().setJavaScriptEnabled(false);
                } else {
                    btnJs.setTextColor(0xffffbb33);
                    webView.getSettings().setJavaScriptEnabled(true);
                }
            }
        });


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        //支持缩放
        //webSettings.setBuiltInZoomControls(true);
        //webSettings.setSupportZoom(true);
        //webSettings.setUserAgentString("");
        //webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        //webSettings.setAppCacheEnabled(true);
        //webSettings.setDomStorageEnabled(true);
        //webSettings.setDatabaseEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                editText.setText(url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if (webView.getProgress() < 99) {
                    tvLoading.setVisibility(View.VISIBLE);
                    tvLoading.setText(url);
                } else {
                    tvLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                showorHide();
                tvLoading.setVisibility(View.GONE);
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {

        });
    }

    @Override
    public void onClick(View v) {
        String url = editText.getText().toString().trim();
        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onBack(View view) {
        if (webView.canGoBack()) {
            webView.goBack();
        }
        showorHide();
    }

    public void onForward(View view) {
        if (webView.canGoForward()) {
            webView.goForward();
        }
        showorHide();
    }

    public void locateEdit() {
        if (!TextUtils.isEmpty(editText.getText().toString())) {
            editText.setSelection(editText.getText().length());
        }
    }

    public void showorHide() {
        if (webView.canGoBack() || webView.canGoForward()) {
            controlView.setVisibility(View.VISIBLE);
        } else {
            controlView.setVisibility(View.GONE);
        }
    }

    public void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = new String[]{"百度", "清除历史", "无图模式"};
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    editText.setText("https://www.baidu.com");
                    btnOpen.performClick();
                }
                if(which == 1){
                    webView.clearHistory();
                    webView.clearCache(true);
                    webView.clearFormData();
                }
                if(which == 2){
                    if(webView.getSettings().getBlockNetworkImage()){
                        webView.getSettings().setBlockNetworkImage(false);
                    }else{
                        webView.getSettings().setBlockNetworkImage(true);
                    }
                }
            }
        });
        builder.create().show();
    }

}
