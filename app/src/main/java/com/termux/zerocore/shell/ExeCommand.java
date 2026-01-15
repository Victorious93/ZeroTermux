package com.termux.zerocore.shell;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ExeCommand {


    private Process process;
    private BufferedReader successResult;
    private BufferedReader errorResult;
    private DataOutputStream os;
    private boolean bSynchronous;
    private boolean bRunning = false;
    ReadWriteLock lock = new ReentrantReadWriteLock();

    private StringBuffer result = new StringBuffer();

    /**
     * 构造函数
     *
     * @param synchronous true：同步，false：异步
     */
    public ExeCommand(boolean synchronous) {
        bSynchronous = synchronous;
    }

    /**
     * 默认构造函数，默认是同步执行
     */
    public ExeCommand() {
        bSynchronous = true;
    }

    /**
     * 还没开始执行，和已经执行完成 这两种情况都返回false
     *
     * @return 是否正在执行
     */
    public boolean isRunning() {
        return bRunning;
    }

    /**
     * @return 返回执行结果
     */
    public String getResult() {
        Lock readLock = lock.readLock();
        readLock.lock();
        try {
            Log.i("auto", "getResult");
            String s = new String(result);
            Log.i("日志:", "getResult" + s);

            result.setLength(0);
            return s;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * 执行命令
     *
     * @param command eg: cat /sdcard/test.txt
     * 路径最好不要是自己拼写的路径，最好是通过方法获取的路径
     * example：Environment.getExternalStorageDirectory()
     * @param maxTime 最大等待时间 (ms)
     * @return this
     */
    public ExeCommand run(String command, final int maxTime,boolean isRoot) {
        Log.i("auto", "run command:" + command + ",maxtime:" + maxTime);
        if (command == null || command.length() == 0) {
            return this;
        }

        try {
            if(isRoot){
                process = Runtime.getRuntime().exec("su");//看情况可能是su
            }else{
                process = Runtime.getRuntime().exec("sh");//看情况可能是su
            }

        } catch (Exception e) {
            return this;
        }
        bRunning = true;
        successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
        errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        os = new DataOutputStream(process.getOutputStream());

        try {
            os.write(command.getBytes());
            os.writeBytes("\n");
            os.flush();
            os.close();
            if (maxTime > 0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(maxTime);
                        } catch (Exception e) {
                        }
                        try {
                            int ret = process.exitValue();
                            Log.i("auto", "exitValue Stream over"+ret);
                        } catch (IllegalThreadStateException e) {
                            Log.i("auto", "take maxTime,forced to destroy process");
                            process.destroy();
                        }
                    }
                }).start();
            }

            final Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    String line;
                    Lock writeLock = lock.writeLock();
                    try {
                        while ((line = successResult.readLine()) != null) {
                            if(line.equals("")){
                                line += "";
                            }else{
                                line += "\n";
                            }

                           // Thread.sleep(200);

                            writeLock.lock();
                            result.append(line);
                            Log.e("XINHAO_HANERROR", "run: " + line );
                            writeLock.unlock();
                        }
                        Log.e("XINHAO_HANERROR", line );
                    } catch (Exception e) {
                        Log.e("XINHAO_HANERROR", "read InputStream exception:" + e.toString());
//                        throw new RuntimeException(e);
                    } finally {
                        try {
                            successResult.close();
                            Log.e("XINHAO_HANERROR", "read InputStream over");

                        } catch (Exception e) {
                            Log.e("XINHAO_HANERROR", "close InputStream exception:" + e.toString());
  //                          throw new RuntimeException(e);
                        }
                    }
                }
            });
            t1.start();

            final Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    String line;
                    Lock writeLock = lock.writeLock();
                    try {
                        while ((line = errorResult.readLine()) != null) {
                            line += "\n";
                            writeLock.lock();
                            result.append(line);
                            writeLock.unlock();
                        }
                    } catch (Exception e) {
                        Log.i("auto", "read ErrorStream exception:" + e.toString());
                    } finally {
                        try {
                            errorResult.close();
                            Log.i("auto", "read ErrorStream over");
                        } catch (Exception e) {
                            Log.i("auto", "read ErrorStream exception:" + e.toString());
                        }
                    }
                }
            });
            t2.start();

            Thread t3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        t1.join();
                        t2.join();
                        process.waitFor();
                    } catch (Exception e) {

                    } finally {
                        bRunning = false;
                        Log.i("auto", "run command process end");
                    }
                }
            });
            t3.start();

            if (bSynchronous) {
                Log.i("auto", "run is go to end");
                t3.join();
                Log.i("auto", "run is end");
            }
        } catch (Exception e) {
            Log.i("auto", "run command process exception:" + e.toString());
        }
        return this;
    }

}
