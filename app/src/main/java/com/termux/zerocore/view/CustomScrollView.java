package com.termux.zerocore.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    private OnScrollChangeListener mOnScrollChangeListener;
    private boolean isScrollToStart = false;
    private boolean isScrollToEnd = false;
    private static final int CODE_TO_START = 0x001;
    private static final int CODE_TO_END = 0x002;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_TO_START:
                    isScrollToStart = false;
                    break;
                case CODE_TO_END:
                    isScrollToEnd = false;
                    break;
                default:
                    break;
            }
        }
    };

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangeListener != null) {
            Log.i("CustomScrollView", "scrollY:" + getScrollY());
            if (getScrollY() == 0) {
                if (!isScrollToStart) {
                    isScrollToStart = true;
                    mHandler.sendEmptyMessageDelayed(CODE_TO_START, 200);
                    Log.e("CustomScrollView", "toStart");
                    mOnScrollChangeListener.onScrollToStart();
                }
            } else {
                View contentView = getChildAt(0);
                if (contentView != null && contentView.getMeasuredHeight() == (getScrollY() + getHeight())) {
                    if (!isScrollToEnd) {
                        isScrollToEnd = true;
                        mHandler.sendEmptyMessageDelayed(CODE_TO_END, 200);
                        Log.e("CustomScrollView", "toEnd,scrollY:" + getScrollY());
                        mOnScrollChangeListener.onScrollToEnd();
                    }

                }
            }
        }

    }

    public interface OnScrollChangeListener {

        void onScrollToStart();

        void onScrollToEnd();
    }

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        mOnScrollChangeListener = onScrollChangeListener;
    }
}

