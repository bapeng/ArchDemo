
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.text.SimpleDateFormat;

public class MainAction {

    public static final String path = "/Users/sierra/Downloads/";
    public static final String name = "/Users/sierra/Downloads/2017年10月包材日报.xlsx";

    public static void main(String[] args) {

        File rootDir = new File(path);
        File[] files = rootDir.listFiles((File dir, String name) ->
                {
                    if (name.contains("西安工厂"))
                        return true;
                    return false;
                }
        );
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String name = file.getName();
            }
        }
        File file = new File(name);
        try {
            readFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readFile(File file) throws Exception {
        FileInputStream inputStream = new FileInputStream(file);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheet("20日报");

        XSSFFormulaEvaluator evaluator = new XSSFFormulaEvaluator(workbook);

        int rownum = Math.min(50, sheet.getPhysicalNumberOfRows());
        for (int i = 0; i < 50; i++) {
            XSSFRow row = sheet.getRow(i);
            StringBuilder builder = new StringBuilder();
            //builder.append("rowNum=" + i).append("  ");
            int cellNum = row.getPhysicalNumberOfCells();
            for (int j = 0; j < 15; j++) {
                XSSFCell cell = row.getCell(j);
                //System.out.println("addr="+cell.getAddress().toString());

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

//                    try {
//                        CellType type = evaluator.evaluateFormulaCellEnum(cell);
//                        if(type == CellType.STRING){
//                            CellValue value =  evaluator.evaluate(cell);
//                            builder.append(value.getStringValue());
//                        }else if(type == CellType.NUMERIC){
//                            CellValue value =  evaluator.evaluate(cell);
//                            builder.append(value.getNumberValue());
//                        }else {
//                            builder.append("value");
//                        }
//                    } catch (Exception e) {
//                        builder.append("Error");
//                    }
                } else if (cell.getCellTypeEnum() == CellType.BLANK) {
                    builder.append("__");
                }
                builder.append(",");
            }
            System.out.println(builder.toString());
        }


    }


}
