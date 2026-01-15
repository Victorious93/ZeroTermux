package com.termux.zerocore.config.ztcommand.config;

import android.content.Context;

import com.termux.zerocore.config.ztcommand.ZTSocketService;

public interface ZTConfig {
    String getCommand(Context context, String command);
    int getId();
    boolean isForWard();
    String getCommandForWard(Context context, String command);

    void sendSocketMessage(ZTSocketService.ClientHandler clientHandler, Context context);
}
