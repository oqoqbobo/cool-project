package com.oqoqbobo.web.service.fileHandle.impl;

import com.data.mapper.SecDictTypeMapper;
import com.data.model.SecDictType;
import com.data.model.SecDictTypeExample;
import com.oqoqbobo.web.data.CheckUtils;
import com.oqoqbobo.web.data.MyException;
import com.oqoqbobo.web.model.returnPojo.FileChineseVO;
import com.oqoqbobo.web.model.returnPojo.FileInfoVO;
import com.oqoqbobo.web.service.fileHandle.FileHandleService;
import com.oqoqbobo.web.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 对于重要的方法的提取，注意事项如下
 *
 * "H:/桌面/测试文件夹/out.txt"
 * fileName == "out.txt"  filePath="H:/桌面/测试文件夹/"
 *
 * 读取文件内容
 * String content = getFileContent(fileName, filePath);
 *
 * 将内容写入文件，直接覆盖
 * setFileContent(content,fileName, filePath);
 *
 */

@Service
public class FileHandleServiceImpl implements FileHandleService {
    @Value("${web.filePath}")
    private String outPath = "out.txt";

    @Value("${web.translate}")
    private String translate = "translate.txt";

    @Value("${web.rootPath}")
    private String rootPath = "D:/桌面/学习记录/翻译/";

    @Value("${web.isTranslate}")
    private Boolean isTranslate = true;

    @Autowired
    private SecDictTypeMapper dictTypeMapper;

    private static Map<String,String> todoCon = new LinkedHashMap<>();
    private String content = "添加备注内容";

    // -------------------------------------------|----------------------------------------------------------
    //****************************************** ↓
    // 获取文件内容

    /**
     * 以outPath为例 fileName是 out.txt   filePath是 H:/桌面/测试文件夹/  注意 outPath = filePath + fileName
     * 获取文件内容
     * @param fileName
     * @param filePath
     * @return
     * @throws IOException
     */
    @Override
    public String getFileContent(String fileName,String filePath) throws IOException {

        File file = new File(filePath+fileName);
        BufferedReader input = null;
        //保存原文件内容
        StringBuilder sb = new StringBuilder();

        try {
            sb.setLength(0);
            input = new BufferedReader(new FileReader(file));
            int read = -1;
            while (input.ready()){
                sb.append(input.readLine()).append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(input != null){
                input.close();
            }
        }
        return sb.toString();
    }

    // -------------------------------------------|----------------------------------------------------------
    //****************************************** ↓
    // 写入文件

    //把指定的内容写入到文件中，直接覆盖重写，不是追加
    @Override
    public void setFileContent(String content, String fileName,String filePath) throws IOException {
        File file = new File(filePath+fileName);
        BufferedWriter output = null;
        try {
            //该方法是覆盖，因为是重写修改的文件
            output = new BufferedWriter(new FileWriter(file));
            output.write(content);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(output != null){
                output.close();
            }
        }
    }

    // -------------------------------------------|----------------------------------------------------------
    //****************************************** ↓
    // 处理单个文件的主入口方法

    @Override
    public void handleContent(String path,String setContent,Boolean isAppened) throws IOException {
        if(StringUtils.isNotBlank(setContent.trim())){
            content = setContent.trim();
        }
        //前端不传值，默认追加
        if(isAppened == null){
            isAppened = true;
        }
        //调用接口前把内容清空，保证上次执行的数据不影响下次执行
        todoCon.clear();
        //获取对应的 文件名称 和 路径
        String name = null;
        String[] strList = path.split("\\\\");
        StringBuilder sbPath = new StringBuilder();
        for(int i=0;i<strList.length;i++){
            if(i == strList.length - 1){
                name = strList[i];
            }else{
                sbPath.append(strList[i]).append("\\");
            }
        }
        setFile(name,sbPath.toString());
        if(CheckUtils.isChinese){
            appendMoreToOut(isAppened);
        }
    }

    /**
     * 该方法对是否追加的控制，是对每一行的内容是否忽略决定的
     *
     * 把收集的中文翻译添加到对应的文件中，可追加，也可覆盖
     * 追加的话会比较out.txt 文件的内容，做到内容不重复
     * 覆盖的话会把所有的国际化内容加载出来，不考虑重复，同时会把out.txt文件内容重置掉，备份之前的out在根目录中，rootPath
     * @param isAppened
     * @throws IOException
     */
    private void appendMoreToOut(Boolean isAppened) throws IOException {
        //对重复内容进行处理，out.txt
        Map<String, String> hasMap = makeOutFileMap();//将原本out文件的内容提取到todoCon中
        StringBuilder sb = new StringBuilder();
        StringBuilder translateSB = new StringBuilder();
        if(todoCon.size()>0){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sb.append("\n\n").append("//////////////////////////////////").append(content).append("//////////////////////////////////").append("\n");
            sb.append("//").append(format.format(new Date())).append(" 添加\n");
            translateSB.append("--------------------").append("\n");
        }
        //todoCon
        for(String key : todoCon.keySet()){
            key = key.trim();
            if(key.contains("\"")){
                //为 null 说明原本输出的翻译文件outFile没有这个中文，需要将其追加进去（isAppended 为true）
                if(isAppened && hasMap.get(key) == null){
                    sb.append("'").append(key).append("':'").append(key).append("',\n");
                    translateSB.append(key).append("\n");
                }
                //如果不需要追加，则认为是覆盖，不需要考虑输出文件中原本的内容
                else if(!isAppened){
                    sb.append("'").append(key).append("':'").append(key).append("',\n");
                    translateSB.append(key).append("\n");
                }
            }else{
                //为 null 说明原本输出的翻译文件outFile没有这个中文，需要将其追加进去（isAppended 为true）
                if(isAppened && hasMap.get(key) == null){
                    sb.append("\"").append(key).append("\":\"").append(key).append("\",\n");
                    translateSB.append(key).append("\n");
                }
                //如果不需要追加，则认为是覆盖，不需要考虑输出文件中原本的内容
                else if(!isAppened){
                    sb.append("\"").append(key).append("\":\"").append(key).append("\",\n");
                    translateSB.append(key).append("\n");
                }
            }

        }
        if(!isAppened){
            //备份数据
            backUpOutFile();
        }
        //用于中文翻译，将所有的中文key
        setOutFileContent(translateSB.toString(),translate,isAppened);
        //将翻译的内容读取处理，追加
        setOutFileContent(sb.toString(),outPath,isAppened); //将处理过的内容写入文件
    }

    //如果选择覆盖，默认备份一次上一次的数据到本地文件中
    private void backUpOutFile() throws IOException {
        //按时间戳创建一个备份文件
        String backUpFileName = System.currentTimeMillis()+"out.txt";
        File backUp = new File(rootPath + backUpFileName);
        if(!backUp.exists()){
            backUp.createNewFile();
        }

        //获取文件内容，认为out直接存在放在根目录中，备份也是
        String content = getFileContent("out.txt", rootPath);

        //写入到备份文件中，实现备份
        setFileContent(content,backUpFileName, rootPath);


    }

    //该方法提供可追加方式，true为追加，false为覆盖，将内容写入到配置文件指定的文件中，content为处理好的内容
    private void setOutFileContent(String content,String fileName,Boolean isAppend) throws IOException {
        File file = new File(rootPath+"/"+fileName);
        BufferedWriter output = null;
        try {
            output = new BufferedWriter(new FileWriter(file,isAppend));
            output.write(content);

        } catch (FileNotFoundException e){
            e.printStackTrace();
        }finally {
            if(output != null){
                output.close();
            }
        }
    }

    //将配置文件指定的文件内容读取出来，保存到Map集合中
    private Map<String,String> makeOutFileMap() throws IOException {
        Map<String,String> result = new LinkedHashMap<>();

        File file = new File(rootPath+"/"+outPath);
        BufferedReader input = null;
        StringBuilder sb = new StringBuilder();
        try {
            sb.setLength(0);
            input = new BufferedReader(new FileReader(file));
            while (input.ready()){
                sb.append(input.readLine()).append(",\n");
            }
            String[] rowList = sb.toString().split("\n");
            for(String row : rowList){
                //不是注释的行才需要处理
                if(!CheckUtils.ignoreChinese(row)){
                    String[] conList = row.split("\":\"|':'");
                    Integer flag = 1;
                    for(String con : conList){
                        con = con.trim();
                        if(StringUtils.isNotBlank(con)){
                            if(flag == 1 && con.startsWith("\"") || con.startsWith("'")){
                                result.put(con.substring(1),con.substring(1));
                            }
                            if(flag == 2 && con.endsWith("\"") || con.endsWith("'")){
                                result.put(con.substring(0,con.length()-1),con.substring(0,con.length()-1));
                            }

                            flag++;
                        }
                    }
                }
            }
        }  catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(input != null){
                input.close();
            }
        }
        return result;
    }

    // -------------------------------------------|----------------------------------------------------------
    //****************************************** ↓
    // 处理文件夹（多个文件）的主入口方法

    @Override
    public void handleFile(String path,String setContent,Boolean isAppened) throws IOException {
        if(StringUtils.isNotBlank(setContent.trim())){
            content = setContent.trim();
        }
        //前端不传值，默认追加
        if(isAppened == null){
            isAppened = true;
        }
        //调用接口前把内容清空，保证上次执行的数据不影响下次执行
        todoCon.clear();
        FileInfoVO allFileInfo = setFileInfo(path);
        outputContent(allFileInfo,"   ");
        if(CheckUtils.isChinese){
            appendMoreToOut(isAppened);
        }
    }

    //文件夹内容处理，对封装的文件信息进行读取处理
    private void outputContent(FileInfoVO vo,String tab){
        if(vo.getIsFile()){
            System.out.println(tab+vo.getFilePath()+vo.getFileName()+"  [文件夹]");
            if(!vo.getFileName().equals("demo")){
                List<FileInfoVO> myFileList = vo.getMyFileList();
                tab+="   ";
                if(myFileList != null && myFileList.size() > 0){
                    for(FileInfoVO myVo : myFileList){
                        outputContent(myVo,tab);
                    }
                }
            }
        }else{
            System.out.println(tab+vo.getFilePath()+vo.getFileName()+"  [文件]");
            if(!vo.getFileName().endsWith(".json") &&
                    !vo.getFileName().endsWith(".png") &&
                    !vo.getFileName().endsWith(".jpg")
            ){
                setFile(vo.getFileName(),vo.getFilePath());
            }
        }
    }

    //文件夹内部文件信息封装  设置整个文件对象FileInfoVO
    private FileInfoVO setFileInfo(String path){
        FileInfoVO result = new FileInfoVO();
        File file = new File(path);
        File[] files = file.listFiles();
        List<File> underFileList = new ArrayList<>();
        if(files != null && files.length > 0){
            underFileList = Arrays.stream(files).collect(Collectors.toList());
        }
        result.setFileName(file.getName());
        result.setFilePath(file.getAbsolutePath().split(file.getName())[0]);
        //没有子文件集说明是文件
        if(underFileList.size()==0){
            result.setIsFile(false);
        }
        //文件夹需要递归
        else{
            result.setIsFile(true);
            List<FileInfoVO> listVo = new ArrayList<>();
            for(File fileobj : underFileList){
                listVo.add(setFileInfo(fileobj.getAbsolutePath()));
            }
            result.setMyFileList(listVo);
        }
        return result;
    }

    //需要 文件名 和 文件路径 处理国际化内容，该方法只操作一个需要国际化的文件，多个文件则需要重复操作
    private void setFile(String name,String path){
        try {
            //对指定的文件进行添加  如果为中文处理，handleOriginalContent 否则为  handleOriginalContentOther
            String content = CheckUtils.isChinese?handleOriginalContent(name,path):handleOriginalContentOther(name,path);
            //将国际化后的内容写入到文件中
            setFileContent(content,name,path);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 由CheckUtils.isChinese 决定  为false
     * 处理文件内容  更新老版国际化为新版国际化
     * 老版：$t('public.table.tip')
     * 新版：$t('提示')
     * @param fileName
     * @param filePath
     * @return
     * @throws IOException
     */
    private String handleOriginalContentOther(String fileName,String filePath) throws IOException {
        //先将所有的内容保存到 todoCon
        Boolean isJs = false;
        Boolean isCss = false;
        //获取对应的key并保存在todoCon中
        readOutInTodoCon(outPath);
        System.out.println("--------------------html--------------");
        if(fileName.endsWith(".js")){
            isJs = true;
            isCss = false;
        }
        if(fileName.endsWith(".less")){
            isJs = false;
            isCss = true;
        }

        String content = getFileContent(fileName, filePath);

        //保存处理后的结果
        StringBuilder result = new StringBuilder();
        String[] rowList = content.split("\n");
        //使用正则  处理中文
        result.setLength(0);
        for(String row : rowList){
            //如果isChange为true，认为来到了js 的部分，其他时候保持原样
            isJs = CheckUtils.isChange(row)?true:isJs;
            if(CheckUtils.isChange(row)){
                //只会输出一次
                System.out.println("--------------------js--------------");
            }
            //如果isNotSet为true，认为到了 CSS 的部分，其他时候保持原样
            isCss = CheckUtils.isNotSet(row)?true:isCss;
            String[] otherContent = row.split("//");
            FileChineseVO vo = CheckUtils.getChinese(otherContent[0]);
            if(vo.getResult()){
                if(isJs && !isCss){
                    String[] contents = vo.getContents();
                    for(String con : contents){
                        //保证替换的内容不为空
                        if(StringUtils.isNotBlank(con) && todoCon.get(con) == null){
                            System.out.println(con + " = "+todoCon.get(con));
                            todoCon.put(con,"我是空的，请给我赋值");
                        }
                        //不为空值就要进行替换
                        if(StringUtils.isNotBlank(con)){
                            String value = todoCon.get(con);
                            if(value.contains("\"")){
                                //如果正好外层是双引号，需要修改成单引号
                                row = row.replaceAll("\""+con+"\"","'"+value+"'");
                                //如果修改成功，认为是正好是单引号包围
                                row = row.replaceAll(con,value);
                            }else if(value.contains("'")){
                                //如果正好外层是单引号，需要修改成双引号
                                row = row.replaceAll("'"+con+"'","\""+value+"\"");
                                //如果修改成功，认为是正好是双引号包围
                                row = row.replaceAll(con,value);
                            }else{
                                row = row.replaceAll(con,value);
                            }

                        }

                    }
                }
                else if(isCss){
                    //啥也不做
                }else{
                    String[] contents = vo.getContents();
                    for(String con : contents){
                        if(StringUtils.isNotBlank(con) && todoCon.get(con) == null){
                            System.out.println(con + " = "+todoCon.get(con));
                            todoCon.put(con,"我是空的，请给我赋值");
                        }
                        //不为空值就要进行替换
                        if(StringUtils.isNotBlank(con)){
                            String value = todoCon.get(con);
                            if(value.contains("\"")){
                                //如果正好外层是双引号，需要修改成单引号
                                row = row.replaceAll("\""+con+"\"","'"+value+"'");
                                //如果修改成功，认为是正好是单引号包围
                                row = row.replaceAll(con,value);
                            }else if(value.contains("'")){
                                //如果正好外层是单引号，需要修改成双引号
                                row = row.replaceAll("'"+con+"'","\""+value+"\"");
                                //如果修改成功，认为是正好是双引号包围
                                row = row.replaceAll(con,value);
                            }else{
                                row = row.replaceAll(con,value);
                            }

                        }
                    }
                }
                result.append(row).append("\n");
            }else{
                result.append(row).append("\n");;
            }
        }
        return result.toString();
    }

    /**
     * 由CheckUtils.isChinese 决定 为true
     * 处理文件内容，将未国际化的中文加上 $t() 进行国际化
     * @param fileName
     * @param filePath
     * @return
     * @throws IOException
     */
    private String handleOriginalContent(String fileName,String filePath) throws IOException {
        Boolean isJs = false;
        Boolean isCss = false;
        Boolean isTranslate = this.isTranslate;  //默认true表示需要翻译，false表示去国际化
        if(fileName.endsWith(".js")){
            isJs = true;
            isCss = false;
        }
        if(fileName.endsWith(".less")){
            isJs = false;
            isCss = true;
        }

        String content = getFileContent(fileName, filePath);

        //保存处理后的结果
        StringBuilder result = new StringBuilder();
        String[] rowList = content.split("\n");
        //使用正则  处理中文
        result.setLength(0);
        for(String row : rowList){
            //如果isChange为true，认为来到了js 的部分，其他时候保持原样
            isJs = CheckUtils.isChange(row)?true:isJs;
            //如果isNotSet为true，认为到了 CSS 的部分，其他时候保持原样
            isCss = CheckUtils.isNotSet(row)?true:isCss;
            String[] otherContent = row.split("//");
            FileChineseVO vo = null;
            if(otherContent.length>0){
                vo = CheckUtils.getChinese(otherContent[0]);
            }

            if(vo != null && vo.getResult()){
                if(isJs && !isCss){
                    //处理一行中各个中文区域，js部分可以保证需要国际化的中文都是被引号包裹（确信！！！！！！）
                    for(String otherStr : vo.getContents()){
                        //进行必要的转化，兼容以前的代码
                        String containStr = otherStr;
                        otherStr = otherStr.replaceAll("\\(","\\\\(")
                                .replaceAll("\\)","\\\\)")
                                .replaceAll("\\?","\\\\?")
                                .replaceAll(":","\\:")
                                .replaceAll("\\[","\\\\[")
                                .replaceAll("!","\\!")
                                .replaceAll("]","\\]")
                                .replaceAll(";","\\;")
                                .replaceAll("_","\\_")
                                .replaceAll("\"","\\\"")
                                .replaceAll("'","\\'")
                        ;
                        // 遇到console打印的中文，不操作
                        if(StringUtils.isNotBlank(otherStr) && !CheckUtils.isConsole(row) && !CheckUtils.parthIgnoreChinese(row,otherStr)){


                            //这里假设获取的内容正好是需要国际化的部分
                            //判断是否已经国际化，如果国际化了，不进行下一步操作
                            //需要判断当前是不是需要国际化的，isTranslate为false，说明是去掉国际化，无需国际化，跳过
                            if(CheckUtils.judgeInnertPattern(row,otherStr) && isTranslate){
                                //用Map可以自动去重，方便 这一步不能漏
                                todoCon.put(containStr,containStr);
                                continue;
                            }

                            //如果是default 目前不考虑那么多，需要再加
                            if(CheckUtils.isDefault(row)){
                                row = row.replaceAll("\""+otherStr+"\"|\\'"+otherStr+"\\'|\\`"+otherStr+"\\`",
                                        "\\(\\) \\=\\> \\{ return \\'"+otherStr+"\\' \\;\\}");
                            }
                            //需要翻译才做，否则就是去国际化，完美
                            if(isTranslate){
                                //国际化，同时把引号去掉 ， 三种引号的表示方式 \\` \\' \"
                                row = row.replaceAll("\\'"+otherStr+"\\'",
                                        "window.vm.\\$t\\(\\'"+otherStr+"\\'\\)");
                                row = row.replaceAll("\""+otherStr+"\"",
                                        "window.vm.\\$t\\(\""+otherStr+"\"\\)");
                                row = row.replaceAll("\\`"+otherStr+"\\`",
                                        "window.vm.\\$t\\(\\`"+otherStr+"\\`\\)");
                            }else{
                                //处理已经被国际化的部分，变成 引号+内容+引号  方便后续统一处理
                                row = row.replaceAll("window.vm.\\$t\\(\\'[\\s]{0,}"+otherStr+"[\\s]{0,}\\'\\)",
                                        "\\'"+otherStr+"\\'");
                                row = row.replaceAll("window.vm.\\$t\\(\"[\\s]{0,}"+otherStr+"[\\s]{0,}\"\\)",
                                        "\""+otherStr+"\"");
                                row = row.replaceAll("window.vm.\\$t\\(\\`[\\s]{0,}"+otherStr+"[\\s]{0,}\\`\\)",
                                        "\\`"+otherStr+"\\`");
                                row = row.replaceAll("\"[\\s]{0,}"+otherStr+"[\\s]{0,}\""
                                        ,"\""+otherStr+"\"");
                                row = row.replaceAll("\\'[\\s]{0,}"+otherStr+"[\\s]{0,}\\'"
                                        ,"\\'"+otherStr+"\\'");
                                row = row.replaceAll("\\`[\\s]{0,}"+otherStr+"[\\s]{0,}\\`"
                                        ,"\\`"+otherStr+"\\`");

                                //如果是default 目前不考虑那么多，需要再加
                                if(CheckUtils.isDefault(row)){
                                    row = row.replaceAll("\""+otherStr+"\"|\\'"+otherStr+"\\'|\\`"+otherStr+"\\`",
                                            "\\(\\) \\=\\> \\{ return \\'"+otherStr+"\\' \\;\\}");
                                }
                            }


                                /*
                                //不建议使用，容易导致奇怪的错误，应该由前端进行调整
                                String pattern = "^[\\s\\S]{0,}\\'"+otherStr+"\\'[\\s\\S]{0,}$";
                                if(!CheckUtils.selfJudge(row,pattern)){
                                    row = row.replaceAll(otherStr,"window.vm.\\$t\\(\\'"+otherStr+"\\'\\)");
                                }
                                */
                            //用Map可以自动去重，方便
                            todoCon.put(containStr,containStr);
                        }
                    }
                }
                else if(isCss){
                    //啥也不做
                }else{
                    //该部分处理HTML部分,需要操作的冒号先去掉
                    String[] strList = new String[]{"label","toolTip","title","btnTitle","placeholder","range-separator","prev-text","next-text","start-placeholder","end-placeholder","alt","header","content"};
                    Boolean isPartIgnore = false;
                    //如果后半部分备注，则去掉后面的注释，注释不需要国际化操作
                    if(CheckUtils.selfJudge(row,"(^[\\s\\S]{0,}//[\\s\\S]{0,}$)")){
                        //只取前半部份进行操作，后面的通过判断 isPartIgnore 是否为true 进行拼接
                        row = otherContent[0];
                        isPartIgnore = true;
                    }
                    //处理一行中各个中文区域，可能有多个，需要循环（不考虑注释的部分，目前只考虑 // 注释的部分）
                    for(String otherStr : vo.getContents()){
                        //进行必要的转化，兼容以前的代码
                        String containStr = otherStr;
                        otherStr = otherStr.replaceAll("\\(","\\\\(")
                                .replaceAll("\\)","\\\\)")
                                .replaceAll("\\?","\\\\?")
                                .replaceAll(":","\\:")
                                .replaceAll("\\[","\\\\[")
                                .replaceAll("!","\\!")
                                .replaceAll("]","\\]")
                                .replaceAll(";","\\;")
                                .replaceAll("_","\\_")
                                .replaceAll("\"","\\\"")
                                .replaceAll("'","\\'")
                        ;

                        //有中文且没有被注释
                        if(StringUtils.isNotBlank(otherStr) && !CheckUtils.parthIgnoreChinese(row,otherStr)){
                            //这里假设获取的内容正好是需要国际化的部分
                            //判断是否已经国际化，如果国际化了，不进行下一步操作
                            //需要判断当前是不是需要国际化的，isTranslate为false，说明是去掉国际化，无需跳过
                            if(CheckUtils.judgeInnertPattern(row,otherStr) && isTranslate){
                                //用Map可以自动去重，方便 这一步不能漏
                                todoCon.put(containStr,containStr);
                                continue;
                            }

                            //该部分处理HTML部分,只有加上的地方，把冒号加上去，由于判断的特殊性，需要去掉国际化后再对标签进行处理
                            for(String strCell : strList){
                                //满足在标签属性中时，则添加，不是就不添加
                                //因为标签属性只会出现一次，正常情况下，所以可以对整行进行取代
                                if(CheckUtils.judgeInnerLabel(row,otherStr,strCell)){
                                    //避免反复添加
                                    row = row.replaceAll("\\:"+strCell+"=|\\:"+strCell+" =",strCell+"=");
                                    //需要翻译才添加，特别注意
                                    if(isTranslate){
                                        //添加
                                        row = row.replaceAll(strCell+"=","\\:"+strCell+"=");
                                    }

                                }
                            }

                            //处理冲突的部分
                            row = row.replaceAll("\\:start-\\:placeholder","\\:start-placeholder");
                            row = row.replaceAll("\\:end-\\:placeholder","\\:end-placeholder");
                            row = row.replaceAll("start-\\:placeholder","\\:start-placeholder");
                            row = row.replaceAll("end-\\:placeholder","\\:end-placeholder");

                            //引号特别注意，添加国际化
                            /**
                             * 先处理复杂的，再处理简单的，同上去国际化理由一样
                             */
                            //由   } , \" 结尾的默认不需要引用，直接国际化即可
                            if(isTranslate){
                                if(CheckUtils.isNotQuotation(row,otherStr)){
                                    if(row.contains("'"+containStr+"'")){
                                        row = row.replaceAll("\\'"+otherStr+"\\'","\\$t\\(\\'"+otherStr+"\\'\\)");
                                    }else if(row.contains("\\`"+containStr+"\\`")){
                                        row = row.replaceAll("\\`"+otherStr+"\\`","\\$t\\(\\`"+otherStr+"\\`\\)");
                                    }else if(row.contains("\""+containStr+"\"")){
                                        row = row.replaceAll("\""+otherStr+"\"","\\$t\\(\\'"+otherStr+"\\'\\)");
                                    }else{
                                        row = row.replaceAll(otherStr,"\\$t\\(\\'"+otherStr+"\\'\\)");
                                    }
                                }else{
                                    if(row.contains("'"+containStr+"'")){
                                        row = row.replaceAll("\\'"+otherStr+"\\'","\\$t\\(\\'"+otherStr+"\\'\\)");
                                    }else if(row.contains("\""+containStr+"\"")){
                                        row = row.replaceAll("\""+otherStr+"\"","\"\\$t\\(\\'"+otherStr+"\\'\\)\"");
                                    }else if(row.contains(">"+containStr+"<")){
                                        row = row.replaceAll("\\>"+otherStr+"\\<","\\>\\{\\{\\$t\\(\\'"+otherStr+"\\'\\)\\}\\}\\<");
                                    }else if(!row.contains(">"+containStr+"<") && row.contains(containStr+"<")){
                                        row = row.replaceAll(otherStr+"\\<","\\{\\{\\$t\\(\\'"+otherStr+"\\'\\)\\}\\}\\<");
                                    }else if(!row.contains(">"+containStr+"<") && row.contains(">"+containStr)){
                                        row = row.replaceAll("\\>"+otherStr,"\\>\\{\\{\\$t\\(\\'"+otherStr+"\\'\\)\\}\\}");
                                    }else{
                                        //没有引号的特殊字符，只可能存在标签的innnerHTML ，所以可以是无忌惮的添加大括号
                                        row = row.replaceAll(otherStr,"\\{\\{\\$t\\(\\'"+otherStr+"\\'\\)\\}\\}");
                                    }
                                }
                            }else{
                                //取消国际化
                                //前置国际化处理，统一格式
                                //将国际化去掉，有四种情况，分别是 必须按照从上到下的顺序，因为 下面两种的情况 包含 上面两种的，顺序反了会有影响
                                //{{$t('str')}}  {{$t("str")}}  --->  str
                                // '$t("str")'  --->  'str'
                                // "$t('str')"  --->  "str"
                                //  $t('str')  --->  'str'
                                //  $t("str")  --->  "str"
                                if(CheckUtils.judgeHTMLpattern(row,otherStr)){
                                    row = row.replaceAll("\\{\\{\\s{0,}\\$t\\(\""+otherStr+"\"\\)\\s{0,}\\}\\}|"+
                                                    "\\{\\{\\s{0,}\\$t\\(\\'"+otherStr+"\\'\\)\\s{0,}\\}\\}|"+
                                                    "\\{\\{\\s{0,}\\$t\\(\\`"+otherStr+"\\`\\)\\s{0,}\\}\\}"
                                            ,otherStr);
                                }else if(row.contains("$t('"+containStr+"')")){
                                    row = row.replaceAll("\\$t\\(\\'"+otherStr+"\\'\\)","\\'"+otherStr+"\\'");
                                }else if(row.contains("$t(\""+containStr+"\")")){
                                    row = row.replaceAll("\\$t\\(\""+otherStr+"\"\\)","\""+otherStr+"\"");
                                }else if(row.contains("$t(`"+containStr+"`)")){
                                    row = row.replaceAll("\\$t\\(\\`"+otherStr+"\\`\\)","\\`"+otherStr+"\\`");
                                }else if(CheckUtils.judgeInnertPattern(row,otherStr)){
                                    row = row.replaceAll("\\$t\\(\"\\s{0,}"+otherStr+"\\s{0,}\"\\)|"+
                                                    "\\$t\\(\\'\\s{0,}"+otherStr+"\\s{0,}\\'\\)|"+
                                                    "\\$t\\(\\`\\s{0,}"+otherStr+"\\s{0,}\\`\\)"
                                            ,"\\'"+otherStr+"\\'");
                                }

                                //把特殊的情况 没有 $t 的国际化处理为 引号+内容+引号  ！！！保留最外层引号
                                row = row.replaceAll("\"\\s{0,}\\'"+otherStr+"\\'\\s{0,}\"","\""+otherStr+"\"");
                                row = row.replaceAll("\\'\\s{0,}\""+otherStr+"\"\\s{0,}\\'","\\'"+otherStr+"\\'");
                                row = row.replaceAll("\\`\\s{0,}\""+otherStr+"\"\\s{0,}\\`","\\`"+otherStr+"\\`");
                                row = row.replaceAll("\\`\\s{0,}\\'"+otherStr+"\\'\\s{0,}\\`","\\`"+otherStr+"\\`");
                                row = row.replaceAll("\"\\s{0,}\\`"+otherStr+"\\`\\s{0,}\"","\""+otherStr+"\"");
                                row = row.replaceAll("\\'\\s{0,}\\`"+otherStr+"\\`\\s{0,}\\'","\\'"+otherStr+"\\'");
                            }
                            //用Map可以自动去重，方便
                            todoCon.put(containStr,containStr);
                        }
                    }
                    //把注释的部分拼接回去，保证一行的数据不会丢失
                    if(isPartIgnore){
                        for(int index = 1;index < otherContent.length;index++){
                            row+="//"+otherContent[index];
                        }
                        //使用后重置为false，保证判断的准确性，一行中可能有多个中文
                        isPartIgnore = false;
                    }
                }

                result.append(row).append("\n");
            }else{
                result.append(row).append("\n");;
            }
        }
        return result.toString();
    }

    // -------------------------------------------|----------------------------------------------------------
    //****************************************** ↓
    // 下载outPath文件

    @Override
    public void downLoadOutText(HttpServletResponse response) throws IOException {
        InputStream input = null;
        OutputStream output = null;
        //获取文件的字节数组
        try {
            File file = new File(rootPath+"/"+outPath);
            input = new FileInputStream(file);
            byte[] byteFile = new byte[input.available()];
            input.read(byteFile);

            //获取输出流
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-download");
            //3.设置content-disposition响应头控制浏览器以下载的形式打开文件
            response.addHeader("Content-Disposition","attachment;filename=" + new String(file.getName().getBytes(),"utf-8"));
            output = new BufferedOutputStream(response.getOutputStream());

            //将对应的字节数组输出出去
            output.write(byteFile);
            output.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            if(output != null){
                output.close();
            }
        }
    }

    // -------------------------------------------|----------------------------------------------------------
    //****************************************** ↓
    // 重置outPath文件

    @Override
    //将outPath里不需要的key去掉，重置outPath文件
    public void resetOutText(String  filePath) throws IOException {
        //接口调用前对共享的静态资源清空，避免影响判断
        todoCon.clear();
        //获取指定文件夹里所有的文件信息
        FileInfoVO fileInfoVO = setFileInfo(filePath);
        //初始化todoCon
        readInternationalContent(fileInfoVO,"   ");
        //读取out.txt文件内容
        String content = getFileContent("out.txt", rootPath);
        String[] rows = content.split("\n");
        //处理文件内容
        StringBuilder newContent = new StringBuilder();
        for(String row : rows){
            String key = getKeyInRow(row);

            //如果不是忽略的内容，则进行下一步操作
            if(CheckUtils.ignoreChinese(row)){
                newContent.append(row).append("\n");
            }
            //此处说明文件中的key是需要的
            else if(todoCon.get(key)!=null){
                newContent.append(row).append("\n");
                todoCon.put(key,null);
            }
        }
        //覆盖文件内容
        setFileContent(newContent.toString(),"out.txt",rootPath);
    }
    //只针对outPath的文件，获取中文key
    private String getKeyInRow(String row){
        String[] split = row.split("\"|:|,");
        String result = "";
        for(String str : split){
            if(StringUtils.isNotBlank(str)){
                //第一个不为空的内容，就是key
                result = str;
                break;
            }
        }
        return result;
    }

    //该方法将所有需要国际化的中文保存到todoCon中，后续操作使用
    private void readInternationalContent(FileInfoVO vo,String tab) throws IOException {
        if(vo.getIsFile()){
            //文件夹则进行递归
            System.out.println(tab+vo.getFilePath()+vo.getFileName()+"  [文件夹]");
            if(!vo.getFileName().equals("demo")){
                List<FileInfoVO> myFileList = vo.getMyFileList();
                tab+="   ";
                if(myFileList.size()>0){
                    for(FileInfoVO myVo : myFileList){
                        readInternationalContent(myVo,tab);
                    }
                }

            }
        }else{
            System.out.println(tab+vo.getFilePath()+vo.getFileName()+"  [文件]");
            if(!vo.getFileName().endsWith(".json")){
                //文件的话则把国际化的中文部分取出，保存到todoCon中
                String content = getFileContent(vo.getFileName(),vo.getFilePath());
                String[] rows = content.split("\n");
                for(String row : rows){
                    //获取需要国际化的中文
                    FileChineseVO chineseVO = CheckUtils.getChinese(row);
                    if(chineseVO.getResult()){
                        //这里获取的中文，认为是需要国际化的
                        for(String theIntenationalStr : chineseVO.getContents()){
                            //对特殊字符进行了转义处理，避免正则判断出错，此处需要对转义的特殊字符修改回来
                            String containStr = theIntenationalStr.replace("\\(","(").replace("\\)",")")
                                    .replace("\\?","?").replace("\\:",":")
                                    .replace("\\[","[").replace("\\]","]")
                                    .replace("\\;",";").replace("\\_","_")
                                    ;
                            //将原始的国际化中文保存起来
                            todoCon.put(containStr,containStr);
                        }
                    }
                }
            }
        }
    }

    // -------------------------------------------|----------------------------------------------------------
    //****************************************** ↓
    // 协议添加翻译属性时使用

    /**
     * 该方法用于给协议对象自动添加字典翻译属性时使用
     * @param fileAllPath  处理文件的完全路径（只处理单个文件）
     * @throws IOException
     */
    @Override
    public void addFileContentForDict(String fileAllPath) throws IOException {
        //初始化path 和 name
        String[] split = fileAllPath.split("/|\\\\");
        StringBuffer sb = new StringBuffer();
        String fileName = "";
        String filePath = "";
        for(Integer index = 0; index<split.length; index++){
            if(index == split.length-1){
                fileName = split[index];
            }else{
                sb.append(split[index]).append("/");
            }
        }
        filePath = sb.toString();
        //获取文件内容
        String fileContent = getFileContent(fileName, filePath);
        String content = fileContent;
        String[] rowList = content.split("\n");
        //需要字典翻译的只有两种情况，Integer  String 两种属性
        String pattern = "^[\\s]{0,}private[\\s]{0,}(Integer|String)[\\s\\S]{0,}$";

        //废物利用
        sb.setLength(0);
        for(String row : rowList){
            Boolean result = CheckUtils.selfJudge(row, pattern);
            if(result){
                String[] cellList = row.split("[\\s]{0,}private[\\s]{0,}(Integer|String)[\\s]{0,}|\\;");
                String cell = getContent(cellList);
                if(cell != null){
                    /**
                     * 获取字典，如果存在，需要添加翻译的注解
                     */
                    SecDictTypeExample example = new SecDictTypeExample();
                    example.createCriteria().andTypeCodeEqualTo(cell);
                    //把当前行添加上去
                    sb.append(row).append("\n");
                    List<SecDictType> list = dictTypeMapper.selectByExample(example);
                    //有值说明需要翻译，添加翻译内容
                    if(list.size()>0){
                        sb.append("@ApiModelProperty(\""+list.get(0).getRemarks()+"\")\n");
                        //翻译注解
                        sb.append("@DictMapping(dictCode = \""+cell+"\",sourceField = \""+cell+"\")").append("\n");
                        //接收翻译属性
                        row = row.replaceAll(cell,cell+"Str");
                        sb.append(row).append("\n");
                    }
                    /*String translate = ExcelHandleServiceImpl.getContentFromExcelForTranslate(cell);
                    sb.append(row).append("\n");
                    //先看看效果
                    if(StringUtils.isNotBlank(translate)){
                        sb.append("//").append(translate).append("\n");
                    }*/
                }

            }else{
                sb.append(row).append("\n");
                if(row.startsWith("package")){
                    sb.append("import com.vdp.common.component.anotation.DictMapping;").append("\n");
                }
                if(row.contains("import com.vdp.common.component.anotation.DictMapping;")){
                    //再出现的话，不再添加
                    continue;
                }
            }
        }
        setFileContent(sb.toString(),fileName,filePath);

    }

    private String getContent(String[] cellList){
        for(String cell : cellList){
            if(StringUtils.isNotBlank(cell)){
                return cell;
            }
        }
        return null;
    }

    // -------------------------------------------|----------------------------------------------------------
    //****************************************** ↓
    //获取老版out.txt 文件的所有key  并保存在 todoCon 静态资源中 以便后续操作

    //删除最后一个key（对老板的key而言  public.table.tips  执行该方法就输出 public.table. ）
    private static void deleteEndKey(StringBuffer sb){
        //删掉最后一位key
         List<String> keySplit = Arrays.stream(sb.toString().split("\\."))
                 .filter(obj -> StringUtils.isNotBlank(obj)).collect(Collectors.toList());
        sb.setLength(0);
        for(int i=0;i<keySplit.size()-1;i++){
            sb.append(keySplit.get(i)).append(".");
        }
    }

    /**
     * 将老版out.txt文件的内容转成能被正确读取的（HTML $t() 方法的内容）Key
     * @param fileName
     * @throws IOException
     */
    public void readOutInTodoCon(String  fileName) throws IOException {
        //接口调用前对共享的静态资源清空，避免影响判断
        todoCon.clear();
        //读取out.txt文件内容
        String content = getFileContent(fileName, rootPath);
        String[] rows = content.split("\n");
        StringBuffer sb = new StringBuffer(); //保存key
        String value = "";
        Integer endCount = 0;
        //处理文件内容
        for(String row : rows){
            //有注释的行，直接跳过
            if(CheckUtils.ignoreChinese(row)){
                continue;
            }
            if(row.endsWith("{")){
                endCount++;
            }
            if(row.endsWith("},") || row.endsWith("}")){
                //删掉最后一位key
                deleteEndKey(sb);
                endCount--;
            }
            if(endCount != 0){
                String[] splitList = row.split(":");
                for(String cell : splitList){
                    cell = cell.trim();
                    if(cell.endsWith("\"") || cell.endsWith("'")){
                        value = cell.substring(1,cell.length()-1);
                        String key = sb.toString().substring(0,sb.length()-1);
                        todoCon.put(key,value);
                        //删掉最后一位key
                        deleteEndKey(sb);


                    }
                    else if(cell.endsWith("\",") || cell.endsWith("',")){
                        value = cell.substring(1,cell.length()-2);
                        String key = sb.toString().substring(0,sb.length()-1);
                        todoCon.put(key,value);
                        //删掉最后一位key
                        deleteEndKey(sb);

                    }else if(cell.endsWith("{") || cell.endsWith("}") || cell.endsWith("},")){
                        //该步骤啥也不干
                    }else{
                       //该判断用于快速定位！！！！
                       /*if(sb.toString().startsWith("appoint.detail")){
                            System.out.println(sb.toString().contains("appoint.detail"));
                        }*/
                        if(StringUtils.isNotBlank(cell)){
                            //说明是key的组成部分
                            sb.append(cell).append(".");
                        }
                    }
                }
            }else{
                //将保存当前key的内容清空 保存内容
                if(sb.length() > 0){
                    sb.setLength(0);
                }
            }
        }


            //该判断用于快速定位！！！！  只在调试的时候使用
            /*Set<String> keySet = todoCon.keySet();
                for(String key : keySet){
                    if(key.startsWith("driver")){
                    System.out.println(key + " : " + todoCon.get(key));
                }
            }*/

    }
    public static String setValueStr(double obj, String pattern){
        if (pattern == null || "".equals(pattern)) {
            pattern = "#,###.000";  //323,333,355.334
        }
        DecimalFormat format = new DecimalFormat(pattern);
        return format.format(new BigDecimal(obj));
    }

    // -------------------------------------------|----------------------------------------------------------
    //****************************************** ↓
    //main方法，及时测试各种方法
    public static void  main(String [] args) throws MyException {
        String aaaaCaaddJJld = StringUtils.camelToUnderline("aaa_aa");
        System.out.println(aaaaCaaddJJld);
    }
}
