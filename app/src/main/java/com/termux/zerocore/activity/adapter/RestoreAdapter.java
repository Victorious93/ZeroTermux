package com.termux.zerocore.activity.adapter;

import android.view.View;

import com.example.xh_lib.utils.UUtils;
import com.termux.R;
import com.termux.zerocore.activity.view_holder.RestoreViewHolder;
import com.termux.zerocore.activity.view_holder.ViewHolder;

import java.io.File;
import java.util.List;


public class RestoreAdapter extends ListBaseAdapter<File> {

    public RestoreAdapter(List<File> list) {
        super(list);
    }

    @Override
    public ViewHolder getViewHolder() {
        return new RestoreViewHolder(View.inflate(UUtils.getContext(), R.layout.list_file_list, null));
    }

    @Override
    public void initView(int position, File file, ViewHolder viewHolder) {

        RestoreViewHolder restoreViewHolder = (RestoreViewHolder) viewHolder;

        restoreViewHolder.msg_title.setText(file.getName() + "    [" + getPrintSize(file.length()) + "]");

        restoreViewHolder.msg_message.setText(file.getAbsolutePath());

    }

    public String getPrintSize(long size) {
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                + String.valueOf((size % 100)) + "MB";
        } else {
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                + String.valueOf((size % 100)) + "GB";
        }
    }

}
