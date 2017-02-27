package com.peng.htmlview.tool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Tim on 2017/2/24.
 */
public class ReadUtil {

    private BufferedReader reader;
    private int line;
    private boolean isEnd;
    private boolean isReading;

    public synchronized void open(String path, String encode) throws FileNotFoundException, UnsupportedEncodingException {
        if (isEnd) {
            throw new IllegalStateException("已经读取结束");
        }
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), encode));
        isEnd = false;
    }

    public synchronized boolean isOpen() {
        return reader != null;
    }

    public synchronized boolean isEnd() {
        return isEnd;
    }

    public synchronized boolean isReading() {
        return isReading;
    }

    public synchronized void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
            } finally {
                reader = null;
            }
        }
        isEnd = true;
    }

    public synchronized void read(int lineNum, OnReadLine onRead) throws IOException {
        if (lineNum <= 0) return;
        isReading = true;
        this.line = lineNum;
        String str;
        while (true) {
            if (line <= 0) {
                line = 0;
                isReading = false;
                break;
            }
            str = reader.readLine();
            --line;
            if (str == null) {
                line = 0;
                isReading = false;
                close();
                break;
            } else {
                if (onRead != null) {
                    onRead.onRead(str);
                }
            }
        }
    }


    public interface OnReadLine {
        public void onRead(String line);
    }


}
