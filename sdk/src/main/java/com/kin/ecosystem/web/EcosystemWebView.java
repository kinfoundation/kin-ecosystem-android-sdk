package com.kin.ecosystem.web;


import static kin.ecosystem.core.BuildConfig.DEBUG;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.kin.ecosystem.common.KinTheme;
import com.kin.ecosystem.core.data.internal.ConfigurationImpl;

public class EcosystemWebView extends WebView {

    private static final String HTML_URL = ConfigurationImpl.getInstance().getEnvironment().getEcosystemWebFront();
    private static final String JS_INTERFACE_OBJECT_NAME = "KinNative";

    private final Handler mainThreadHandler;
    private final EcosystemNativeApi nativeApi;
    private final EcosystemWebViewClient webViewClient;
    private final EcosystemWebChromeClient webChromeClient;

    public EcosystemWebView(Context context) {
        this(context, null);
    }

    public EcosystemWebView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EcosystemWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mainThreadHandler = new Handler(Looper.getMainLooper());

        webViewClient = new EcosystemWebViewClient(context);
        setWebViewClient(this.webViewClient);

        webChromeClient = new EcosystemWebChromeClient(context);
        setWebChromeClient(this.webChromeClient);

        final WebSettings settings = this.getSettings();
        settings.setJavaScriptEnabled(true);

        nativeApi = new EcosystemNativeApi();
        addJavascriptInterface(nativeApi, JS_INTERFACE_OBJECT_NAME);
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            setWebContentsDebuggingEnabled(DEBUG);
        }
    }

    public void load() {
        loadUrl(HTML_URL);
    }

    public void setListener(final EcosystemWebPageListener listener) {
        nativeApi.setListener(listener);
    }

    public void renderJs(final String jsData) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mainThreadHandler.post(new Runnable() {
                @Override
                public void run() {
                    renderJs(jsData);
                }
            });

            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            evaluateJavascript(jsData, null);
        } else {
            loadUrl(jsData);
        }
    }

    public void renderPoll(final String pollJsonData) {
        renderJs("kin.renderPoll(" + pollJsonData + ")");
    }

    public void setTheme(final String kinTheme) {
        renderJs("kin.setTheme(\"" + kinTheme + "\")");
    }

    public void release() {
        mainThreadHandler.removeCallbacksAndMessages(null);
        onPause();
        destroy();
    }
}
