import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.sound.midi.Soundbank;
import java.io.*;

public class MainAction {

    public static final String path = "C:\\Users\\Tim\\Desktop\\雪花表格\\2016年";
    public static final String name = "西安工厂2016年1月库存物资明细";

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
        File file = files[0];
        try {
            readFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void readFile(File file) throws Exception {
        FileInputStream inputStream = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = workbook.getSheet("明细");
        for (int i = 0; i < 100; i++) {
            HSSFRow row = sheet.getRow(i);
            StringBuilder builder = new StringBuilder();
            builder.append("rowNum=" + i).append("  ");
            for (int j = 0; j < 13; j++) {
                HSSFCell cell = row.getCell(j);
                if (cell.getCellTypeEnum() == CellType.STRING) {
                    builder.append(cell.getStringCellValue());
                } else if (cell.getCellTypeEnum() == CellType.NUMERIC) {
                    builder.append(cell.getNumericCellValue());
                } else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
                    builder.append(cell.getBooleanCellValue());
                } else if (cell.getCellTypeEnum() == CellType.ERROR) {
                    builder.append(cell.toString());
                } else if (cell.getCellTypeEnum() == CellType.FORMULA) {
                    builder.append("formula:"+cell.toString());
                }
                builder.append("，   ");
            }
            System.out.println(builder.toString());
        }


    }


}
