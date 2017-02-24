package com.peng.htmlview.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileTool {

    public static String charset(String path) {
        try {
            File file = new File(path);
            FileInputStream input = new FileInputStream(file);
            int pre = (input.read() << 8) + input.read();
            String code = "UTF-8";
            switch (pre) {
                case 0xefbb:
                    if (input.read() == 0xbf) {
                        code = "UTF-8";
                    }
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
                    break;
            }
            input.close();
            return code;
        } catch (IOException e) {
        }
        return "UTF-8";
    }

}
