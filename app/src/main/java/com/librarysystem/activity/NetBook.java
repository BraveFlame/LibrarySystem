package com.librarysystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.librarysystem.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 检查不到书籍时联网学习图书馆，以建议管理员加入某书
 */

public class NetBook extends Activity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.net_book);
        webView=(WebView)findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        try {
            String addresses = (URLEncoder.encode(getIntent().getStringExtra("search"), "UTF-8"));
            webView.loadUrl("http://202.116.174.108:8080/opac/openlink.php?strSearchType=title&historyCount=1&strText="+addresses+"&doctype=ALL");
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setInitialScale(50);

        } catch (UnsupportedEncodingException e) {

        }


        /*
        webView.getSettings().setUseWideViewPort(true);设置此属性，可恣意比例缩放。
注：1、初始缩放值可这样设置：webView.setInitialScale(initalValue);
         */
    }
}
 /*
    借书
    http://202.116.174.108:8080
    /opac/openlink.php?strSearchType=title&historyCount=1&strText=English&doctype=ALL

    http://202.116.174.108:8080
    /opac/openlink.php?strSearchType=title&historyCount=1&strText=java&doctype=ALL

    http://202.116.174.108:8080
    /opac/openlink.php?strSearchType=title&historyCount=1&strText=%E8%A5%BF%E6%B8%B8%E8%AE%B0&doctype=ALL

    http://202.116.174.108:8080
    /opac/openlink.php?strSearchType=title&historyCount=1&strText=%E4%B8%89%E5%9B%BD%E6%BC%94%E4%B9%89&doctype=ALL
     */