package com.termux.zerocore.bean;

public class ZeroRunCommandBean {


    private int type;
    private String title;
    private String msg;
    private String fileName;
    private String address;
    private String assetsName;
    private String name;
    private String runCommand;
    private boolean isShow = true;
    private boolean isHttpCommand = false;
    private RunCommit mRunCommit;

    public RunCommit getRunCommit() {
        return mRunCommit;
    }

    public void setRunCommit(RunCommit mRunCommit) {
        this.mRunCommit = mRunCommit;
    }

    public interface RunCommit{

        public void run();

    }

    public boolean isHttpCommand() {
        return isHttpCommand;
    }

    public void setHttpCommand(boolean httpCommand) {
        isHttpCommand = httpCommand;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public String getRunCommand() {
        return runCommand;
    }

    public void setRunCommand(String runCommand) {
        this.runCommand = runCommand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
