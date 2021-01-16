package com.aquilaflycloud.mdc.util;
/**
 * Created by MI on 2020/4/28.
 */

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author llzguazi
 * @Date 2020/4/28
 **/
public class PoiUtil {
    private static final String XLS = ".XLS";
    private static final String XLSX = ".XLSX";

    public static JSONObject readExcel(MultipartFile file){
        JSONObject result = null;
        try {
            Workbook wb = getWorkbook(file);
            for(int i = 0;i < wb.getNumberOfSheets();i++){
                Sheet sheet = wb.getSheetAt(i);
                int startRowNum = sheet.getFirstRowNum() + 1;
                int lastRowNum = sheet.getLastRowNum();
                //是否存在有效row
                if(lastRowNum - startRowNum > 1 && startRowNum > -1){
                    String sheetName = sheet.getSheetName();
                    JSONArray jsonArray = new JSONArray();
                    Map<Integer,String> propertityName = new HashMap<>();
                    for(int r = startRowNum + 1; r <= lastRowNum; r++){
                        Row row = sheet.getRow(r);
                        int startCellNum = row.getFirstCellNum();
                        int lastCellNum = row.getLastCellNum();
                        //是否存在有效cell
                        if(lastCellNum - startCellNum > 0 && startCellNum > -1){
                            DataFormatter formatter = new DataFormatter();
                            JSONObject jsonObject = new JSONObject();
                            if(r == startRowNum + 1){
                                //获取属性名
                                for(int c = startCellNum; c <= lastCellNum;c++){
                                    Cell cell = row.getCell(c);
                                    if(cell != null){
                                        propertityName.put(c,formatter.formatCellValue(cell));
                                    }
                                }
                            }else{
                                //获取属性值
                                for(int c = startCellNum; c <= lastCellNum;c++){
                                    Cell cell = row.getCell(c);
                                    if(cell != null){
                                        jsonObject.set(propertityName.get(c),formatter.formatCellValue(cell));
                                    }
                                }
                                jsonArray.add(jsonObject);
                            }
                        }
                    }
                    if(jsonArray.size() > 0){
                        if(result == null){
                            result = new JSONObject();
                        }
                        if(jsonArray.size() > 1){
                            result.set(sheetName,jsonArray);
                        }else{
                            result.set(sheetName,jsonArray.get(0));
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(result == null){
            throw new ServiceException("解析文档为空");
        }
        return result;
    }

    /**
     * 根据文件后缀名类型获取对应的工作簿对象
     *
     * @param file 文件
     * @return 包含文件数据的工作簿对象
     * @throws IOException
     */
    public static Workbook getWorkbook(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        Workbook workbook = null;
        InputStream inputStream = file.getInputStream();
        if (originalFilename.toUpperCase().endsWith(XLS)) {
            workbook = new HSSFWorkbook(inputStream);
        } else if (originalFilename.toUpperCase().endsWith(XLSX)) {
            workbook = new XSSFWorkbook(inputStream);
        }

        if (null == workbook) {
            throw new ServiceException("文件后缀名不正确");
        }

        return workbook;
    }

    /**
     * 读取Excel
     * @param fileInputStream 文件输入流
     * @param filename 文件名，包括扩展名
     * @param startrow 开始行号，索引从0开始
     * @param startcol 开始列号，索引从0开始
     * @param sheetnum 工作簿，索引从0开始
     * @return
     */
    public static List<Map<String,String>> readExcel(InputStream fileInputStream, String filename, int startrow, int startcol, int sheetnum) {
        List<Map<String, String>> varList = new ArrayList<>();
        String suffix = filename.substring(filename.lastIndexOf(".") + 1);
        if ("xls".equals(suffix)) {
            varList = readExcel2003(fileInputStream, startrow, startcol, sheetnum);
        } else if ("xlsx".equals(suffix)) {
            varList = readExcel2007(fileInputStream, startrow, startcol, sheetnum);
        } else {
            System.out.println("Only excel files with XLS or XLSX suffixes are allowed to be read!");
            return null;
        }
        return varList;
    }

    /**
     * 读取2003Excel
     *
     * @param fileInputStream 文件输入流
     * @param startrow 开始行号，索引从0开始
     * @param startcol 开始列号，索引从0开始
     * @param sheetnum 工作簿，索引从0开始
     * @return
     */
    private static List<Map<String,String>> readExcel2003(InputStream fileInputStream, int startrow, int startcol, int sheetnum) {
        List<Map<String, String>> varList = new ArrayList<>();
        try {
            HSSFWorkbook wb = new HSSFWorkbook(fileInputStream);
            fileInputStream.close();
            // sheet 从0开始
            HSSFSheet sheet = wb.getSheetAt(sheetnum);
            // 取得最后一行的行号
            int rowNum = sheet.getLastRowNum() + 1;

            HSSFRow rowTitle = sheet.getRow(0);
            // 标题行的最后一个单元格位置
            int cellTitleNum = rowTitle.getLastCellNum();
            String[] title = new String[cellTitleNum];
            for (int i = startcol; i < cellTitleNum; i++) {
                HSSFCell cell = rowTitle.getCell(Short.parseShort(i + ""));
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    title[i] = cell.getStringCellValue();
                } else {
                    title[i] = "";
                }
            }

            // 行循环开始
            for (int i = startrow + 1; i < rowNum; i++) {
                Map<String, String> varpd = new HashMap<String, String>();
                // 行
                HSSFRow row = sheet.getRow(i);
                // 列循环开始
                for (int j = startcol; j < cellTitleNum; j++) {

                    HSSFCell cell = row.getCell(Short.parseShort(j + ""));
                    String cellValue = "";
                    if (cell != null) {
                        // 把类型先设置为字符串类型
                        cell.setCellType(CellType.STRING);
                        cellValue = cell.getStringCellValue();
                    }
                    varpd.put(title[j], cellValue);
                }
                varList.add(varpd);
            }
            wb.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return varList;
    }

    /**
     * 读取2007Excel
     *
     * @param fileInputStream 文件输入流
     * @param startrow 开始行号，索引从0开始
     * @param startcol 开始列号，索引从0开始
     * @param sheetnum 工作簿，索引从0开始
     * @return
     */
    private static List<Map<String,String>> readExcel2007(InputStream fileInputStream, int startrow, int startcol, int sheetnum) {
        List<Map<String, String>> varList = new ArrayList<>();
        try {
            XSSFWorkbook wb = new XSSFWorkbook(fileInputStream);
            fileInputStream.close();
            // 得到Excel工作表对象
            XSSFSheet sheet = wb.getSheetAt(sheetnum);
            // 取得最后一行的行号
            int rowNum = sheet.getLastRowNum() + 1;

            XSSFRow rowTitle = sheet.getRow(0);
            int cellTitleNum = rowTitle.getLastCellNum();
            String[] title = new String[cellTitleNum];
            for (int i = startcol; i < cellTitleNum; i++) {
                XSSFCell cell = rowTitle.getCell(Short.parseShort(i + ""));
                if (cell != null) {
                    // 把类型先设置为字符串类型
                    cell.setCellType(CellType.STRING);
                    title[i] = cell.getStringCellValue();
                } else {
                    title[i] = "";
                }
            }

            // 行循环开始
            for (int i = startrow + 1; i < rowNum; i++) {
                Map<String, String> varpd = new HashMap<String, String>();
                // 得到Excel工作表的行
                XSSFRow row = sheet.getRow(i);
                // 列循环开始
                for (int j = startcol; j < cellTitleNum; j++) {
                    // 得到Excel工作表指定行的单元格
                    XSSFCell cell = row.getCell(j);
                    String cellValue = "";
                    if (cell != null) {
                        // 把类型先设置为字符串类型
                        cell.setCellType(CellType.STRING);
                        cellValue = cell.getStringCellValue();
                    }
                    varpd.put(title[j], cellValue);
                }
                varList.add(varpd);
            }
            wb.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return varList;
    }
}
