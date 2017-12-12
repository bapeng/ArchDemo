package com.excel;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainAction {

    public static final String path = "/Users/sierra/Downloads/";
    //public static final String name = "/Users/sierra/Downloads/2017年10月包材日报.xlsx";
    public static final String name = "C:\\Users\\Tim\\Desktop\\雪花表格\\2016年\\西安工厂2016年1月库存物资明细.xls";

    public static void main(String[] args) {
        File file = new File(name);
        try {
            handleXls2(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleXls2(File file) throws Exception {
        FileInputStream inputStream = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet("明细");
        HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook);

        HSSFRow titleRow = sheet.getRow(1);

        String[] titles = {"物资名称", "单位"};
        List<ValueFilter> fs = ValueUtil.creatFilter(titles, titleRow);

        double allJine = 0;

        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            HSSFRow row = sheet.getRow(i);
            if (row == null) {
                System.out.println("本行为空");
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("第" + (i + 1) + "行 ");

            RowFilter rowFilter = new RowFilter(row, fs);
            if (!rowFilter.isPass()) {
                continue;
            }

//            if (jeCell.getCellTypeEnum() == CellType.NUMERIC) {
//                allJine += jeCell.getNumericCellValue();
//            }
//            if (jeCell.getCellTypeEnum() == CellType.FORMULA) {
//                CellValue value = evaluator.evaluate(jeCell);
//                if (value.getCellTypeEnum() == CellType.NUMERIC) {
//                    allJine += value.getNumberValue();
//                }
//            }


        }
        DecimalFormat format = new DecimalFormat("0.00");
        System.out.println("总金额是：" + format.format(allJine));
    }

    public static void handleXls(File file) throws Exception {
        FileInputStream inputStream = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet("明细");
        HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook);
        int rowCount = sheet.getPhysicalNumberOfRows();


        HSSFRow titleRow = sheet.getRow(1);
        int danwei, jine = -1;
        for (int j = 0; j < titleRow.getPhysicalNumberOfCells(); j++) {
            HSSFCell cell = titleRow.getCell(j);
            if (cell == null) {
                continue;
            }
            if (cell.getCellTypeEnum() == CellType.STRING) {
                String str = cell.getStringCellValue();
                if (str != null) {
                    if (str.trim().contains("单位")) {
                        danwei = j;
                    }
                    if (str.trim().contains("金额")) {
                        jine = j;
                    }
                }
            }
        }

        for (int i = 0; i < rowCount; i++) {
            HSSFRow row = sheet.getRow(i);
            if (row == null) {
                System.out.println("本行为空");
                continue;
            }
            StringBuilder builder = new StringBuilder();
            builder.append("第" + i + "行 ");

            int cellCount = row.getPhysicalNumberOfCells();
            for (int j = 0; j < cellCount; j++) {
                HSSFCell cell = row.getCell(j);
                if (cell == null) {
                    builder.append("NULL,");
                    continue;
                }
                if (cell.getCellTypeEnum() == CellType.STRING) {
                    builder.append(cell.getStringCellValue().trim());
                } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        String time = format.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                        builder.append(time);
                    } else {
                        builder.append(cell.getNumericCellValue());
                    }
                } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
                    builder.append(cell.getBooleanCellValue());
                } else if (cell.getCellTypeEnum() == CellType.ERROR) {
                    builder.append("error");
                } else if (cell.getCellTypeEnum() == CellType.FORMULA) {
                    CellValue value = evaluator.evaluate(cell);
                    if (value.getCellTypeEnum() == CellType.NUMERIC) {
                        builder.append(value.getNumberValue());
                    } else if (value.getCellTypeEnum() == CellType.STRING) {
                        builder.append(value.getStringValue());
                    }
                } else if (cell.getCellTypeEnum() == CellType.BLANK) {
                    builder.append("__");
                }
                builder.append(",");
            }
            System.out.println(builder.toString());
        }
    }


    public static void handleXlsx(File file) throws Exception {
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheet("明细");
        int rownum = Math.min(500, sheet.getPhysicalNumberOfRows());
        for (int i = 0; i < rownum; i++) {
            XSSFRow row = sheet.getRow(i);
            StringBuilder builder = new StringBuilder();
            int cellNum = row.getPhysicalNumberOfCells();
            builder.append("rowNum=" + i).append("  cellCount=" + row.getPhysicalNumberOfCells() + "  ");
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
                XSSFCell cell = row.getCell(j);
                if (cell == null) {
                    builder.append("NULL,");
                    continue;
                }
                if (cell.getCellTypeEnum() == CellType.STRING) {
                    builder.append(cell.getStringCellValue().trim());
                } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        String time = format.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                        builder.append(time);
                    } else {
                        builder.append(cell.getNumericCellValue());
                    }
                } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
                    builder.append(cell.getBooleanCellValue());
                } else if (cell.getCellTypeEnum() == CellType.ERROR) {
                    builder.append("error");
                } else if (cell.getCellTypeEnum() == CellType.FORMULA) {
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        String time = format.format(DateUtil.getJavaDate(cell.getNumericCellValue()));
                        builder.append("form:" + time);
                    } else {
                        builder.append("form:" + cell.getCTCell().getV());
                    }
                } else if (cell.getCellTypeEnum() == CellType.BLANK) {
                    builder.append("__");
                }
                builder.append(",");
            }
            System.out.println(builder.toString());
        }


    }


}
