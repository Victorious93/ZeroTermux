package com.termux.zerocore.config.mainmenu.config;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public interface MainMenuClickConfig {
    int getType();
    Drawable getIcon(Context context);
    String getString(Context context);
    void onClick(View view, Context context);
    boolean onLongClick(View view, Context context);
    void initViewStatus(Context context);
    void release();
    void setTextView(TextView textView);
    void setImageView(ImageView imageView);
}
