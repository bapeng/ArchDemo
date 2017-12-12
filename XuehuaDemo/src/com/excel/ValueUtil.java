package com.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ValueUtil {

    public static List<ValueFilter> creatFilter(String[] ns, HSSFRow row) {
        List<String> names = Arrays.asList(ns);
        List<ValueFilter> fs = new ArrayList<>();
        for (int i = 0; i < row.getLastCellNum(); i++) {
            HSSFCell cell = row.getCell(i);
            if (cell == null) {
                continue;
            }
            if (cell.getCellTypeEnum() == CellType.STRING) {
                String str = cell.getStringCellValue();
                if (str != null) {
                    if (names.contains(str.trim())) {
                        ValueFilter filter = new ValueFilter(i, str);
                        fs.add(filter);
                    }
                }
            }
        }
        if (names.size() != fs.size()) {
            throw new IllegalStateException("没有正确找到标题栏");
        }
        return fs;
    }


}
