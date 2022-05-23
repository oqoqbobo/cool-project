package com.oqoqbobo.web.service.excelHandle.impl;

import com.oqoqbobo.web.service.excelHandle.ExcelHandleService;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class ExcelHandleServiceImpl implements ExcelHandleService {
    @Value("${web.filePath}")
    private String outPath = "out.txt";

    @Value("${web.rootPath}")
    private String rootPath = "H:/桌面/测试文件夹/";

    private static Map<String,String> todoCon = new TreeMap<>();
    private String content = "添加备注内容";

    @Override
    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public List<List<String>> readExcel(String path) {
        try {
            File file = new File(path);
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                List<List<String>> outerList=new ArrayList<List<String>>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getRows(); i++) {
                    List innerList=new ArrayList();
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getColumns(); j++) {
                        String cellinfo = sheet.getCell(j, i).getContents();
                        if(cellinfo.isEmpty()){
                            continue;
                        }
                        innerList.add(cellinfo);
                    }
                    outerList.add(i, innerList);
                }
                return outerList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getContentFromExcelForTranslate(String translate){
        ExcelHandleServiceImpl obj = new ExcelHandleServiceImpl();
        List<List<String>> excel = obj.readExcel("D:\\桌面\\一为企标解析.xls");
        String outputStr = "";
        Boolean isContinue = true;
        Scanner sc = new Scanner(System.in);
        for(List<String> row : excel){
            if(row.size()>4 && translate.equals(row.get(1))){
                outputStr = row.get(2);
            }
        }

        return outputStr;
    }

    public static void  main(String [] args){
        System.out.println(getContentFromExcelForTranslate("brakePedalVoltage1"));;
        /*for(List<String> row : excel){
            if(row.size()>4){
                System.out.println(row.get(2)+"   "+row.get(3));
            }
        }*/

    }
}
