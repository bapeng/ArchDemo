package com.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;

public class ValueFilter {

    public int column;
    public String title;
    public String value;

    public ValueFilter(int column, String title) {
        this.column = column;
        this.title = title;
    }

    public int getCell(){
        return 0;
    }

    public boolean isPass(HSSFCell cell) {

        return true;
    }


}
