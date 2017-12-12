package com.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

import java.util.List;

public class RowFilter {

    public HSSFRow row;
    public List<ValueFilter> filters;

    public RowFilter(HSSFRow row, List<ValueFilter> filters) {
        this.row = row;
        this.filters = filters;
    }

    public boolean isPass() {
        for (int i = 0; i < filters.size(); i++) {
            ValueFilter nf = filters.get(i);
            HSSFCell cell = row.getCell(nf.getCell());
            if (!nf.isPass(cell)) {
                return false;
            }
        }
        return true;
    }


}
