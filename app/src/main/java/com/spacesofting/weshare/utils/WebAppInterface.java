package com.spacesofting.weshare.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.spacesofting.weshare.yandex.MapListener;


public class WebAppInterface {
    public static final String NEXI = "Nexi";
    private Context mContext;
    private MapListener listener;

    /** Instantiate the interface and set the context */
    public WebAppInterface(Context c) {
        mContext = c;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void Log(String msg) {
        LogUtil.Log(msg);
    }

    @JavascriptInterface
    public void Loge(String msg) {

        LogUtil.Loge(msg);
    }

    @JavascriptInterface
    public void onMapInitialized(boolean isMapInit) {
        LogUtil.Log(""+isMapInit);

        if (listener != null) {
            listener.onMapInitialized(isMapInit);
        }
    }

    public void setOnInitListener(MapListener listener) {
        this.listener = listener;
    }
}
