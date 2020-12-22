package com.aquilaflycloud.mdc.util;
/**
 * Created by MI on 2020/4/28.
 */

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.gitee.sop.servercommon.exception.ServiceException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
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
}
