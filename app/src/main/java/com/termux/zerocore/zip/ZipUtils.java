package com.termux.zerocore.zip;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Environment;
import android.system.ErrnoException;
import android.system.Os;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.xh_lib.utils.UUtils;
import com.hzy.lib7z.IExtractCallback;
import com.hzy.lib7z.Z7Extractor;
import com.termux.app.TermuxApplication;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


public class ZipUtils {


    private static ArrayList<File> arrayList = new ArrayList<>();

    private static File mTempFile;

    /**
     * 2
     * zip解压
     * 3
     *
     * @param srcFile     zip源文件
     *                    4
     * @param destDirPath 解压后的目标文件夹
     *                    5
     *
     *
     */

    public static void unZip(File srcFile, String destDirPath, ZipNameListener zipNameListener) throws RuntimeException {

        long start = System.currentTimeMillis();


        if (!srcFile.exists()) {

            zipNameListener.zip("你没有存储卡权限，请设置权限为：允许[zip]", 0, 0);

            return;
        }


        ZipFile zipFile = null;

        try {
            try {
                String fileEncode = EncodeUtil.getEncode(srcFile.getAbsolutePath(), true);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    zipFile = new ZipFile(srcFile, Charset.forName(fileEncode));
                } else {
                    zipFile = new ZipFile(srcFile);
                }
            }catch (Exception e){
                zipFile = new ZipFile(srcFile);
            }

            Enumeration<?> entries = zipFile.entries();

            int size = zipFile.size();
            int sizeC = 0;

            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                try {

                    Log.e("XINHAO_HAN", "解压" + entry.getName());
                    sizeC++;
                    zipNameListener.progress(size, sizeC);
                    zipNameListener.zip(entry.getName(), size, sizeC);


                    if (entry.isDirectory()) {
                        String dirPath = destDirPath + "/" + entry.getName();
                        File dir = new File(dirPath);
                        dir.mkdirs();
                    }  else {
                        File targetFile = new File(destDirPath + "/" + entry.getName());
                        if (!targetFile.getParentFile().exists()) {
                            targetFile.getParentFile().mkdirs();
                        }
                        mTempFile = targetFile;
                        if (targetFile.isDirectory()) {
                            boolean mkdirs = targetFile.mkdirs();
                            if (!mkdirs) {
                                Log.e("XINHAO_HAN_ERROR", "创建文件夹失败! " + mTempFile.getAbsolutePath());
                            }
                        }
                        targetFile.createNewFile();
                        InputStream is = zipFile.getInputStream(entry);
                        FileOutputStream fos = new FileOutputStream(targetFile);
                        int len;
                        byte[] buf = new byte[2048];
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                        }
                        fos.close();
                        is.close();
                        String name = targetFile.getName();
                        if (!TextUtils.isEmpty(name) &&  name.contains("_zerotermux_link")) {
                            String fileString = UUtils.getFileStringNotNewFile(targetFile);
                            Log.d("TAG", "unZipxxxxx  fileString: " + fileString);
                            try {
                                String[] split = fileString.split(",");
                                if (split.length == 2) {
                                    targetFile.delete();
                                    termuxSymlink(split[1].trim(), targetFile.getAbsolutePath().replace("_zerotermux_link", ""));
                                } else {
                                    Log.d("TAG", "unZipxxxxx error path name: " + fileString);
                                }
                            }catch (Exception e) {
                                Log.d("TAG", "unZipxxxxx error path name: " + fileString);
                                Log.d("TAG", "unZipxxxxx error path name: " + e.toString());
                            }
                        } else {
                            Log.d("TAG", "unZipxxxxx error path name: " + name);
                        }
                    }
                } catch (Exception e) {


                    Log.e("XINHAO_HAN", "出错的文件[ " + entry.getName() + "]: " + e.toString());
                }

            }

            long end = System.currentTimeMillis();

            System.out.println("解压完成，耗时：" + (end - start) + " ms");

            zipNameListener.zip("解压完成，耗时：" + (end - start) + " ms,请不要离开,正在检查恢复包是否有自运行命令[[命令目录/home/BootCommand]命令之间用'&&'隔开]", size, sizeC);

            zipNameListener.complete();
        } catch (Exception e) {

            Log.e("XINHAO_HAN_ERROR", "unZip: " + e.toString());

        } finally {

            if (zipFile != null) {

                try {

                    zipFile.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }


    }

    private static void termuxSymlink(String oldStr, String newStr) {

        //Os.symlink(oldStr, newStr);
        try {
            Os.symlink("filerrr", "/data/data/com.termux/files.zip");
        } catch (ErrnoException e) {
            Log.e("TAG", "termuxSymlink: " + e.toString() );
        }
    }

    static int mSize = 0;
    static int mIndex = 0;

    public static void start7Z(Context context, File srcFile, String destDirPath, ZipNameListener zipNameListener) {


        Log.e("XINHAO_HAN", "srcFile: " + srcFile.getAbsolutePath());
        Log.e("XINHAO_HAN", "destDirPath: " + destDirPath);


        new Thread(new Runnable() {
            @Override
            public void run() {
                mSize = 0;
                mIndex = 0;
                Z7Extractor.extractAsset(context.getAssets(), srcFile.getAbsolutePath(), destDirPath, new IExtractCallback() {
                    @Override
                    public void onStart() {

                        zipNameListener.zip("备份开始!", 0, 0);
                    }

                    @Override
                    public void onGetFileNum(int fileNum) {
                        mSize = fileNum;
                    }

                    @Override
                    public void onProgress(String name, long size) {
                        mIndex++;
                        zipNameListener.zip(name, 0, 0);
                        zipNameListener.progress(mSize, mIndex);
                    }

                    @Override
                    public void onError(int errorCode, String message) {
                        zipNameListener.zip("备份出现错误:" + message, 0, 0);
                    }

                    @Override
                    public void onSucceed() {
                        zipNameListener.zip("备份完成!", 0, 0);
                        zipNameListener.complete();
                    }
                });
            }
        }).start();


    }


    public static void un7Z(File srcFile, String destDirPath, ZipNameListener zipNameListener) {


        final int[] i = {0};
        final int[] size = {0};
        Z7Extractor.extractFile(srcFile.getAbsolutePath(), destDirPath, new IExtractCallback() {
            @Override
            public void onStart() {
                zipNameListener.zip("开始解压", 0, 0);
            }

            @Override
            public void onGetFileNum(int fileNum) {
                size[0] = fileNum;
            }

            @Override
            public void onProgress(String name, long size1) {
                i[0]++;
                zipNameListener.zip(name, 0, 0);
                zipNameListener.progress(size[0], i[0]);

                Runtime runtime = Runtime.getRuntime();

                try {

                    runtime.exec("chmod 0700 /data/data/com.termux/files/" + name);


                    if ("mysqld".equals(name)) {
                        Log.e("XINHAO_HAN", "onProgress: " + name);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int errorCode, String message) {
                zipNameListener.zip("解压失败!" + message, 0, 0);
            }

            @Override
            public void onSucceed() {
                zipNameListener.zip("解压完成!", 0, 0);
                zipNameListener.complete();
            }
        });

    }


    private static long size = 0;
    private static long index = 0;

    private static long fileSize = 0;

    private static long fileThisSize = 0;


    public static long getFileSize() {
        return fileSize;
    }

    public static long getFileThisSize() {
        return fileThisSize;
    }


    public static void recursiveFilesDelete(String path, ZipNameListener zipNameListener) {

        File file = new File(path);

        File files[] = file.listFiles();

        if (files == null) {

            return;
        }

        if (files.length == 0) {
            System.out.println(path + "该文件夹下没有文件");
        }

        for (File f : files) {

            if (f.isDirectory()) {
                recursiveFilesDelete(f.getAbsolutePath(), zipNameListener);
            } else if (f.isFile()) {

                Log.e("XINHAO_HAN", "recursiveFiles: " + f.getAbsolutePath());
                size++;
                fileSize += f.length();
                zipNameListener.zip("获取文件[" + size + "]：" + f.getAbsolutePath(), 0, 0);
                f.delete();
                // arrayList.add(f);
            } else {

                zipNameListener.zip("忽略该目录", 0, 0);
            }

        }

    }


    //----------------------------------------------------------------------



    public static void delFolder(String folderPath, ZipNameListener zipNameListener) {
        try {
            delAllFile(folderPath, zipNameListener); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹


        } catch (Exception e) {
            e.printStackTrace();
            zipNameListener.zip("删除失败!" + e.toString(), 0, 0);

            throw new RuntimeException(e);
        }


    }

    public static boolean delAllFile(String path, ZipNameListener zipNameListener) {

        File file1 = new File(path);

        if (file1.isDirectory()) {

            if ("sdcard".equals(file1.getName())) {

                UUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UUtils.getContext(), "为了数据安全,已强制忽略[sdcard]目录", Toast.LENGTH_SHORT).show();
                    }
                });

                return true;
            }

            if ("storage".equals(file1.getName())) {

                UUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UUtils.getContext(), "为了数据安全,已强制忽略[storage]目录", Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }

        }


        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        if (tempList == null) {
            return true;
        }
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            zipNameListener.zip(tempList[i], 0, 0);
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i], zipNameListener);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i], zipNameListener);//再删除空文件夹
                flag = true;
            }
        }

        zipNameListener.complete();
        return flag;
    }


    //----------------------------------------------------------------------


    public static void recursiveFiles(String path, ZipNameListener zipNameListener) {
        File file = new File(path);
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }

        if (files.length == 0) {
            System.out.println(path + "该文件夹下没有文件");
        }

        for (File f : files) {
            if (f.isDirectory() &&
                (isSymbolicLink(f) != SYMBOLIC_LINK_FILE && isSymbolicLink(f) != SYMBOLIC_LINK_FILE_ERROR)) {
                recursiveFiles(f.getAbsolutePath(), zipNameListener);
            } else if (f.isFile()) {
                Log.e("XINHAO_HAN", "recursiveFiles: " + f.getAbsolutePath());
                size++;
                fileSize += f.length();
                zipNameListener.zip("获取文件[" + size + "]：" + f.getAbsolutePath(), 0, 0);
                // arrayList.add(f);
            } else {
                zipNameListener.zip("快捷方式", 0, 0);
            }
        }
    }

    private final static int NO_SYMBOLIC_LINK_FILE = 0;
    private final static int SYMBOLIC_LINK_FILE = 1;
    private final static int SYMBOLIC_LINK_FILE_ERROR = -1;

    private static int isSymbolicLink(File f){
        try {
            return !f.getAbsolutePath().equals(f.getCanonicalPath())? SYMBOLIC_LINK_FILE : NO_SYMBOLIC_LINK_FILE;
        } catch (IOException e) {
            return SYMBOLIC_LINK_FILE_ERROR;
        }
    }


    public static void zipFileThis(String srcPath, String dstPath, ZipNameListener zipNameListener) {


        /**
         23
         * 压缩成ZIP 方法1
         24
         * @param srcDir 压缩文件夹路径
        25
         * @param out    压缩文件输出流
        26
         * @param KeepDirStructure  是否保留原来的目录结构,true:保留目录结构;
        27
         *                          false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
        28
         * @throws RuntimeException 压缩失败会抛出运行时异常
        29
         */


    }

    private static final int BUFFER_SIZE = 2 * 1024;

    public static void toZip(String srcDir, String d, ZipNameListener zipNameListener) throws RuntimeException, FileNotFoundException {
        size = 0;
        index = 0;
        fileSize = 0;
        fileThisSize = 0;
        FileOutputStream out = new FileOutputStream(new File(d));

        recursiveFiles(srcDir, zipNameListener);

        zipNameListener.zip("开始压缩", 0, 0);

        long start = System.currentTimeMillis();

        ZipOutputStream zos = null;

        try {

            zos = new ZipOutputStream(out);

            File sourceFile = new File(srcDir);

            compress(sourceFile, zos, sourceFile.getName(), true, zipNameListener);

            long end = System.currentTimeMillis();

            System.out.println("压缩完成，耗时：" + (end - start) + " ms");

            zipNameListener.complete();
        } catch (Exception e) {

        } finally {

            if (zos != null) {

                try {

                    zos.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }


    }


    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure, ZipNameListener zipNameListener) throws Exception {

        try {
            index++;
            zipNameListener.progress(fileSize, fileThisSize);

            byte[] buf = new byte[BUFFER_SIZE];

            if (!sourceFile.exists()) {
                return;
            }

            if (sourceFile.isFile() && isSymbolicLink(sourceFile) != SYMBOLIC_LINK_FILE) {
                fileThisSize += sourceFile.length();
                zipNameListener.zip(sourceFile.getAbsolutePath(), 0, 0);
                zos.putNextEntry(new ZipEntry(name));
                int len;
                FileInputStream in = new FileInputStream(sourceFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                // Complete the entry
                zos.closeEntry();
                in.close();
            } else if (sourceFile.isDirectory() && isSymbolicLink(sourceFile)
                != SYMBOLIC_LINK_FILE && isSymbolicLink(sourceFile) != SYMBOLIC_LINK_FILE_ERROR){
                File[] listFiles = sourceFile.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    if (KeepDirStructure) {
                        zos.putNextEntry(new ZipEntry(name + "/"));
                        zos.closeEntry();
                    }
                } else {
                    for (File file : listFiles) {
                        if (KeepDirStructure) {
                            compress(file, zos, name + "/" + file.getName(), KeepDirStructure, zipNameListener);
                        } else {
                            compress(file, zos, file.getName(), KeepDirStructure, zipNameListener);
                        }
                    }
                }
            } else if (isSymbolicLink(sourceFile) == SYMBOLIC_LINK_FILE) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    File file = workSymbolicLinkFile(sourceFile);
                    fileThisSize += file.length();
                    zipNameListener.zip(file.getAbsolutePath(), 0, 0);
                    zos.putNextEntry(new ZipEntry(name + "_zerotermux_link"));
                    int len;
                    FileInputStream in = new FileInputStream(file);
                    while ((len = in.read(buf)) != -1) {
                        zos.write(buf, 0, len);
                    }
                    // Complete the entry
                    try {
                        file.delete();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    zos.closeEntry();
                    in.close();
                } else {
                    Log.d("TAG", "workSymbolicLinkFile Android API < Android O");
                }
            } else {
                Log.d("TAG", "workSymbolicLinkFile file on error! :" + sourceFile.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)

    private static File workSymbolicLinkFile(File symbolicLinkFile) {
        try {
            Log.d("TAG", "workSymbolicLinkFile AbsolutePath: " + symbolicLinkFile.getAbsolutePath());
            Log.d("TAG", "workSymbolicLinkFile CanonicalPath: " + symbolicLinkFile.getCanonicalPath());
            File file = new File(symbolicLinkFile.getParentFile(), "zerotermux_link_" + symbolicLinkFile.getName());
            String absolutePath = symbolicLinkFile.getAbsolutePath();
            String canonicalPath = symbolicLinkFile.getCanonicalPath();
            UUtils.setFileString(file, absolutePath + "," + canonicalPath);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG", "workSymbolicLinkFile error: " + e.toString());
        }
        return null;
    }

    public static interface ZipNameListener {


        void zip(String FileName, int size, int position);


        void complete();

        void progress(long size, long position);

    }
}
