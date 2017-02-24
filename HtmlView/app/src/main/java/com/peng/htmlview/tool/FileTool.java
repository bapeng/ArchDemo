package com.peng.htmlview.tool;

import org.mozilla.universalchardet.UniversalDetector;

import java.io.FileInputStream;
import java.io.IOException;

public class FileTool {

    public static String codeString(String path) {
        String charset;
        try {
            byte[] buf = new byte[1024];
            FileInputStream fis = new FileInputStream(path);
            UniversalDetector detector = new UniversalDetector(null);
            int read;
            while ((read = fis.read(buf)) != -1 && !detector.isDone()) {
                detector.handleData(buf, 0, read);
            }
            fis.close();
            detector.dataEnd();
            charset = detector.getDetectedCharset();
        } catch (IOException e) {
            charset = "UTF-8";
        }
        if(charset == null){
            charset = "UTF-8";
        }
        return charset;
    }


}
