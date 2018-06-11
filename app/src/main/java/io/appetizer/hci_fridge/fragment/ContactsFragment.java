package io.appetizer.hci_fridge.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import io.appetizer.hci_fridge.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {


    public ContactsFragment() {
        // Required empty public constructor
    }
    private WebView webview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_contacts, container, false);
        WebView goodsWebView = (WebView)inflate.findViewById(R.id.webview);
        goodsWebView.setWebViewClient(new WebViewClient());
//        Util.printLog("硬件加速器  "+Build.VERSION.SDK_INT, 2);
//        if (Build.VERSION.SDK_INT >= 19) {//硬件加速器的使用
//            goodsWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }
        WebSettings webSettings = goodsWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        goodsWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return true;
            }
        });
        goodsWebView.loadUrl("www.baidu.com");
        return inflate;
    }


}
