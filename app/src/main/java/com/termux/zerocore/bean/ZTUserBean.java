package com.termux.zerocore.bean;

import com.termux.shared.termux.settings.properties.TermuxPropertyConstants;

public class ZTUserBean {
    private boolean isOpenDownloadFileServices = false;
    private boolean inputMethodTriggerClose = false;
    private boolean styleTriggerOff = false;
    private boolean isToolShow = false;
    private boolean forceUseNumpad = false;
    private boolean isOutputLOG = false;
    private boolean isSnowflakeShow = false;
    private boolean isResetVolume = false;
    private boolean isShowCommand = false;
    private boolean isInternalPassage = false;
    private String mServerJsonString;

    public String getServerJsonString() {
        return mServerJsonString;
    }

    public void setServerJsonString(String serverJsonString) {
        this.mServerJsonString = serverJsonString;
    }

    public boolean isInternalPassage() {
        return isInternalPassage;
    }

    public void setInternalPassage(boolean internalPassage) {
        isInternalPassage = internalPassage;
    }

    public boolean isShowCommand() {
        return isShowCommand;
    }

    public void setShowCommand(boolean showCommand) {
        isShowCommand = showCommand;
    }

    public boolean isResetVolume() {
        return isResetVolume;
    }

    public void setResetVolume(boolean resetVolume) {
        isResetVolume = resetVolume;
    }

    public boolean isSnowflakeShow() {
        return isSnowflakeShow;
    }

    public void setSnowflakeShow(boolean snowflakeShow) {
        isSnowflakeShow = snowflakeShow;
    }

    public boolean isRainShow() {
        return isRainShow;
    }

    public void setRainShow(boolean rainShow) {
        isRainShow = rainShow;
    }

    private boolean isRainShow = false;


    public boolean isOutputLOG() {
        return isOutputLOG;
    }

    public void setOutputLOG(boolean outputLOG) {
        isOutputLOG = outputLOG;
    }

    private String numpad = TermuxPropertyConstants.DEFAULT_IVALUE_EXTRA_KEYS;

    public boolean isOpenDownloadFileServices() {
        return isOpenDownloadFileServices;
    }

    public void setOpenDownloadFileServices(boolean openDownloadFileServices) {
        isOpenDownloadFileServices = openDownloadFileServices;
    }

    public boolean isInputMethodTriggerClose() {
        return inputMethodTriggerClose;
    }

    public void setInputMethodTriggerClose(boolean inputMethodTriggerClose) {
        this.inputMethodTriggerClose = inputMethodTriggerClose;
    }

    public boolean isStyleTriggerOff() {
        return styleTriggerOff;
    }

    public void setStyleTriggerOff(boolean styleTriggerOff) {
        this.styleTriggerOff = styleTriggerOff;
    }

    public boolean isToolShow() {
        return isToolShow;
    }

    public void setToolShow(boolean toolShow) {
        isToolShow = toolShow;
    }

    public boolean isForceUseNumpad() {
        return forceUseNumpad;
    }

    public void setForceUseNumpad(boolean forceUseNumpad) {
        this.forceUseNumpad = forceUseNumpad;
    }

    public String getNumpad() {
        return numpad;
    }

    public void setNumpad(String numpad) {
        this.numpad = numpad;
    }
}
