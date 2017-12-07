
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;

public class MainAction {

    public static final String path = "/Users/sierra/Downloads/";
    //public static final String name = "/Users/sierra/Downloads/2017年10月包材日报.xlsx";
    public static final String name = "C:\\Users\\Tim\\Desktop\\雪花表格\\2016年\\西安工厂2016年1月库存物资明细.xls";

    public static void main(String[] args) {
        File file = new File(name);
        try {
            handleXlsx(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void handleXls(File file) throws Exception {
        FileInputStream inputStream = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet("明细");
        HSSFFormulaEvaluator evaluator = new HSSFFormulaEvaluator(workbook);
        int rownum = Math.min(500, sheet.getPhysicalNumberOfRows());
        for (int i = 0; i < rownum; i++) {
            HSSFRow row = sheet.getRow(i);
            StringBuilder builder = new StringBuilder();
            int cellNum = row.getPhysicalNumberOfCells();
            builder.append("rowNum=" + i).append("  cellCount=" + row.getPhysicalNumberOfCells() + "  ");
            for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
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
