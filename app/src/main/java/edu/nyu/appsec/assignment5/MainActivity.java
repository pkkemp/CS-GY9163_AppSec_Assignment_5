package edu.nyu.appsec.assignment5;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.SerializablePermission;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String SPELL_CHECK_URL = "http://appsecclass.report:8080/";
    private static final String KNOWN_HOST = "appsecclass.report";

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = String.valueOf(request.getUrl());
            String host = Uri.parse(url).getHost();

            if (KNOWN_HOST.equals(host)) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    /* Get location data to provide language localization
    *  Supported languages ar-DZ zh-CN en-US en-IN en-AU fr-FR
    */

    /* I'm removing the onLocationChanged method, since it isn't necessary for the application to function
     * and violates the user's privacy in a pretty severe way.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView view = new WebView(this);
        view.setWebViewClient(new MyWebViewClient());

        WebSettings settings = view.getSettings();
        settings.setJavaScriptEnabled(true);


        /* The below settings have been modified, it's worth noting that even if the web application
           was built to need this, we still shouldn't allow it as it could compromise user data
         */


        /* since the web application does not access any Javascript from the device's file system
           we can safely disable setAllowFileAccessFromFileURLs() */
        settings.setAllowFileAccessFromFileURLs(false);
        /* this allows for Javascript that runs from a file URL context to be able to arbitrarially
         access content from any origin, once again, since the web application does not access any
         Javascript from the device's file system we can safely disable setAllowFileAccessFromFileURLs() */
        settings.setAllowUniversalAccessFromFileURLs(false);


        setContentView(view);
        view.loadUrl(SPELL_CHECK_URL + "register");
    }
}
