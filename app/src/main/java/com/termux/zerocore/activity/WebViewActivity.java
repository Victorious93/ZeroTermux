package com.termux.zerocore.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.example.xh_lib.activity.BaseActivity;
import com.example.xh_lib.activity.BaseThemeActivity;
import com.example.xh_lib.statusBar.StatusBarCompat;
import com.example.xh_lib.utils.UUtils;
import com.just.agentweb.AgentWeb;
import com.termux.R;


import org.jetbrains.annotations.NotNull;


public class WebViewActivity extends BaseThemeActivity {

    private LinearLayout ll;
    private AgentWeb agentWeb;

    private ValueCallback mValueCallback;
    private ValueCallback mFilePathCallback;
    private final static int RESULT_CODE = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        StatusBarCompat.cancelLightStatusBar(this);
        setImgLiftAs();
        String title = getIntent().getExtras().getString("title");
        if(title!= null) {
            setBaseTitleString(title);
        }
        getRightTv().setVisibility(View.GONE);

        String title_visible = getIntent().getExtras().getString("title_visible");

        if(title_visible != null){
            getBaseTitle().setVisibility(View.GONE);
        }else{
            getBaseTitle().setVisibility(View.VISIBLE);
        }
        setting();

        UUtils.showLog("传入的连接:" + getIntent().getExtras().getString("content"));
    }

    private void setting() {
        ll = findViewById(R.id.ll);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(ll, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .createAgentWeb()
                .ready()


                .go(getIntent().getExtras().getString("content"));
        agentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface(agentWeb,this));
        agentWeb.getWebCreator().getWebView().getSettings().setUseWideViewPort(true);
        agentWeb.getWebCreator().getWebView().getSettings().setLoadWithOverviewMode(true);


    }

    @Override
    public void onBackPressed() {


        if (!agentWeb.back()){
            finish();
        }
       // super.onBackPressed();
    }

    public class AndroidInterface {

        private AgentWeb agent;
        private Context context;

        public AndroidInterface(AgentWeb agent, Context context) {
            this.agent = agent;
            this.context = context;

        }

        @JavascriptInterface //一定要加
        public void finishFromJS() {
            UUtils.showLog("执行销毁程序...");
           finish();

        }

        @JavascriptInterface //一定要加
        public void closWordOrder() {
         /*   startActivity(new Intent(WebViewActivity.this,HomeActivity.class));
            finish();*/

        }

        @JavascriptInterface //一定要加
        public void startWebUrl(String url) {
            UUtils.showLog("传入连接:" + url);
            Intent intent= new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            startActivity(intent);
        }

       // window.android.startWebUrl("https://www.baidu.com");
    }
    @Override
    public void refreshUI(@NotNull TypedValue mBackground, @NotNull TypedValue mTextColor) {

    }


    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
        }
        public void openFileChooser(ValueCallback valueCallback) {
            mValueCallback = valueCallback;
            openImageActivity();
        }
        public void openFileChooser(ValueCallback valueCallback, String acceptType) {
            mValueCallback = valueCallback;
            openImageActivity();
        }
        public void openFileChooser(ValueCallback valueCallback, String acceptType,  String capture) {
            mValueCallback = valueCallback;
            openImageActivity();
        }
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback filePathCallback, FileChooserParams fileChooserParams) {
            mFilePathCallback = filePathCallback;
            openImageActivity();
            return true;
        }

    };
    private void openImageActivity() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");
        startActivityForResult(Intent.createChooser(i, "Image Chooser"),RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CODE) {
            if (null == mValueCallback && null == mFilePathCallback) return;
            Uri result = data == null || resultCode != RESULT_OK ? null : data.getData();
            if (mFilePathCallback != null) {
                onActivityResultAboveL(requestCode, resultCode, data);
            } else if (mValueCallback != null) {
                mValueCallback.onReceiveValue(result);
                mValueCallback = null;
            }
        }
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void onActivityResultAboveL(int requestCode, int resultCode, Intent intent) {
        if (requestCode != RESULT_CODE || mFilePathCallback == null)
            return;
        Uri[] results = null;
        if (resultCode == Activity.RESULT_OK) {
            if (intent != null) {
                String dataString = intent.getDataString();
                ClipData clipData = intent.getClipData();
                if (clipData != null) {
                    results = new Uri[clipData.getItemCount()];
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results[i] = item.getUri();
                    }
                }
                if (dataString != null)
                    results = new Uri[]{Uri.parse(dataString)};
            }
        }
        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
    }
}
