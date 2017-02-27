package com.peng.htmlview;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.peng.htmlview.tool.FileTool;
import com.peng.htmlview.tool.ReadUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HtmlActivity extends AppCompatActivity {

    private ReadUtil read = new ReadUtil();
    private SyncUtil sync = SyncUtil.newSync();

    private LinearLayout rootView;
    private NestedScrollView scrollView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        initView();
        loadData();
    }

    private void initView() {
        rootView = (LinearLayout) findViewById(R.id.activity_html);
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        textView = (TextView) findViewById(R.id.tv_html);

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY + scrollView.getHeight() >= scrollView.computeVerticalScrollRange()) {
                    readNextPage();
                }
            }
        });
    }

    public void readNextPage() {
        if (read.isEnd() || read.isReading()) {
            return;
        }
        Intent intent = getIntent();
        Uri uri = intent.getData();
        final String path = uri.getPath();
        sync.submit(new SyncUtil.RunBack() {
            @Override
            public void back() {
                try {
                    read.read(50, new ReadUtil.OnReadLine() {
                        @Override
                        public void onRead(String line) {
                            sync.sendResult(line + "\n");
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    ShowToast("文件读取错误");
                }
            }
        }, runUI);
    }

    private void loadData() {
        StringBuilder builder = new StringBuilder();
        Intent intent = getIntent();
        builder.append(intent.toString()).append("\n\n");
        Uri uri = intent.getData();
        String type = intent.getType();
        textView.append("URI："+uri.toString() + "\n");
        textView.append("MIME：" + type + "\n");

        final String path = uri.getPath();
        sync.submit(new SyncUtil.RunBack() {
            @Override
            public void back() {
                if (!read.isOpen()) {
                    String encode = FileTool.codeString(path);
                    sync.sendResult("编码：" + encode + "\n\n");
                    try {
                        read.open(path, encode);
                    } catch (FileNotFoundException e) {
                        sync.sendResult("文件不存在");
                    } catch (Exception e) {
                        sync.sendResult("文件读取错误");
                    }
                }
                try {
                    read.read(50, new ReadUtil.OnReadLine() {
                        @Override
                        public void onRead(String line) {
                            sync.sendResult(line + "\n");
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (IOException e) {
                    ShowToast("文件读取错误");
                }
            }
        }, runUI);
    }

    private SyncUtil.RunUI runUI = new SyncUtil.RunUI() {
        @Override
        public void ui(Object object) {
            if (object != null) {
                textView.append(object.toString());
            }
        }
    };

    public void decodeFile(String path, String encode) {
        try {
            FileInputStream fis = new FileInputStream(path);
            InputStreamReader isr = new InputStreamReader(fis, encode);
            BufferedReader reader = new BufferedReader(isr);
            String str;
            while (true) {
                str = reader.readLine();
                if (str == null) {
                    break;
                } else {
                    sync.sendUpdate(str + "\n");
                    Thread.sleep(20);
                }
            }
            reader.close();
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            ShowToast("文件不存在");
        } catch (Exception e) {
            ShowToast("文件读取错误");
        }
    }


    private String getFileString(String filepath) {
        File file = new File(filepath);
        try {
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            StringBuilder builder = new StringBuilder();
            String str;
            while (true) {
                str = reader.readLine();
                if (str == null) {
                    break;
                } else {
                    builder.append(str).append("\n");
                }
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            ShowToast("文件不存在");
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    private void ShowToast(String msg) {
        Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        read.close();

    }
}
