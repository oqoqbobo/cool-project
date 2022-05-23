package com.oqoqbobo.web.data;

import com.oqoqbobo.web.model.returnPojo.FileChineseVO;
import com.oqoqbobo.web.model.util.CheckBO;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {
    //包含中文
    private final static String HAS_CHINESE = "^[\\s\\S]{0,}[\\u4e00-\\u9fa5]+[\\s\\S]{0,}$";
    //全是中文
    private final static String IS_CHINESE = "^[\\u4e00-\\u9fa5]+$";
    //已经被国际化 标致是被 $t(...) 包围
    private final static String IS_INTERNATIONAL= "^\\$t\\([\\s\\S]{0,}[\\u4e00-\\u9fa5]+[\\s\\S]{0,}\\)$";
    //一行内容  进行多行或单行注释  <!-- -->
    private final static String IGNORE_CHINESE = "(^\\s{0,}\\<\\!\\-\\-[\\s\\S]{0,}[\\u4e00-\\u9fa5]+[\\s\\S]{0,}\\-\\-\\>\\s{0,}$)|(^\\s{0,}//[\\s\\S]{0,}[\\u4e00-\\u9fa5]+[\\s\\S]{0,}$)|(^\\s{0,}/{0,}\\*{1,}[\\s\\S]{0,}[\\u4e00-\\u9fa5]+[\\s\\S]{0,}[\\s]{0,}$)|(^[\\s\\S]{0,}[\\u4e00-\\u9fa5]+(\\*/){1}\\s{0,}$)";

    //如果遇到 类似 default:中文 的内容，需要进行转换，变为default: () => {  中文 ; }的形式，以便进行下一步操作
    private final static String IS_DEFAULT = "^[\\s\\S]{0,}default[\\s]{0,}\\:[\\s]{0,}[\"|\\'|\\`]{1}[\\s\\S]{0,}[\\u4e00-\\u9fa5]+[\\s\\S]{0,}[\"|\\'|\\`]{1}[\\s]{0,}\\,{0,1}[\\s]{0,}$";
    //如果遇到 类似 console.log(中文) 的内容，不进行处理
    private final static String IS_CONSOLE = "^[\\s\\S]{0,}console\\.log\\([\\s\\S]{0,}[\\u4e00-\\u9fa5]+[\\s\\S]{0,}$";
    //是否转换 是否进入js部分
    private final static String IS_CHANGE = "^[\\s\\S]{0,}<script>[\\s\\S]{0,}$";
    //是否进入CSS部分 如果是，则忽略不操作
    private final static String IS_NOT_SET = "^[\\s\\S]{0,}<style[\\s\\S]{0,}$";

    static StringBuffer sb;
    static String patternStr;
    static Pattern pattern;
    static Matcher matcher;
    public static Boolean isChinese;

    static{
        sb = new StringBuffer();
        isChinese = true;
        //中文国际化
        if(isChinese){
            String unitStr = "m³°℃Ω㎡";
            String defaultSymbol = "\\.\\。\\,\\~\\?\\%\\!\\_\\，/\\【\\】\\？\\！\\[\\]\\(\\)\\（\\）\\、\\-\\”\\“\\‘\\’\\;\\；\\:\\：\\\\";
            //true 说明将可以国际化的中文添加上国际化的特有函数 $t()
            patternStr =
                    "\\`[\\u4e00-\\u9fa5a-zA-Z0-9\\s"+unitStr+defaultSymbol+"]+\\`|"+
                            "\\'[\\u4e00-\\u9fa5a-zA-Z0-9\\s"+unitStr+defaultSymbol+"\"]+\\'|"+
                            "\"[\\u4e00-\\u9fa5a-zA-Z0-9\\s"+unitStr+defaultSymbol+"\\`]+\"|"+
                            "\\>[\\u4e00-\\u9fa5a-zA-Z0-9\\s"+unitStr+defaultSymbol+"]+\\<|"+
                            "[\\u4e00-\\u9fa5a-zA-Z0-9\\s"+unitStr+defaultSymbol+"]+\\<|"+
                            "\\>[\\u4e00-\\u9fa5a-zA-Z0-9\\s"+unitStr+defaultSymbol+"]+|"+
                            "[\\u4e00-\\u9fa50-9"+unitStr+defaultSymbol+"]+";
            pattern = Pattern.compile(patternStr);
        }else{
            //false 说明将老版国际化改为新版国际化
            //获取所有国际化key  格式 $('xxx.xxx.xxx') $("xxx.xxx.xxx") $(`xxx.xxx.xxx`)
            patternStr =
                    "❤t\\(\\`[A-Za-z0-9\\.]+\\`\\)|"+
                            "❤t\\(\\'[A-Za-z0-9\\.]+\\'\\)|"+
                            "❤t\\(\"[A-Za-z0-9\\.]+\"\\)|"+
                            "\"([A-Za-z0-9]+\\.)+[A-Za-z0-9]+\""
            ;
            pattern = Pattern.compile(patternStr);
        }


    }

    public static FileChineseVO getChinese(String str){
        sb.setLength(0);
        //把特殊符号去掉，避免正则冲突 跟正则有关！！！！！！！
        str = str.replaceAll("\\$","❤")
                .replaceAll("\\|","❤")
                .replaceAll("\\^","❤")
        ;

        FileChineseVO obj = new FileChineseVO();
        obj.setResult(checkChinese(str));
        //未处理的中文才进行
        if(!isChinese || checkChinese(str)){
            // 非注释的中文需要处理
            if(!isChinese || !ignoreChinese(str)){
                matcher = pattern.matcher(str);
                while(matcher.find()){
                    String group = matcher.group();
                    sb.append(group).append("❤");
                    //replaceFirst 第一个参数是正则，需要对特殊符号转义
                    group = group.replace("\\","\\\\")
                            .replace("(","\\(").replace(")","\\)")
                            .replace("?","\\?").replace(":","\\:")
                            .replace("[","\\[").replace("]","\\]")
                            .replace(";","\\;").replace("_","\\_");
                    str = str.replaceFirst(group,"");
                    matcher.reset(new CheckBO(str));

                }
                //切割成字符串数组，兼容以前的代码
                String[] splitChinese = sb.toString().split("❤");
                String thePattern = "";
                for(Integer i=0;i<splitChinese.length;i++){
                    //如果包含中文认为是需要操作的数据
                    if(!checkChinese(splitChinese[i])){
                        splitChinese[i] = "";
                    }else{
                        splitChinese[i] = splitChinese[i].trim();
                        if(splitChinese[i].length() > 1 && (
                                splitChinese[i].startsWith("`") ||
                                splitChinese[i].startsWith("'") ||
                                splitChinese[i].startsWith("\"") ||
                                splitChinese[i].startsWith(">")
                        )){
                            //去掉头部多余的部分
                            splitChinese[i] = splitChinese[i].substring(1);
                        }
                        if(splitChinese[i].length() > 1 && (
                                splitChinese[i].endsWith("'") ||
                                splitChinese[i].endsWith("`") ||
                                splitChinese[i].endsWith("\"") ||
                                splitChinese[i].endsWith("<")
                        )){
                            //去掉尾部多余的部分
                            splitChinese[i] = splitChinese[i].substring(0,splitChinese[i].length()-1);
                        }
                        if(!isChinese &&
                                splitChinese[i].length() > 6 &&
                                splitChinese[i].startsWith("t(")){
                            //该处理只针对国际化中，非中文的替换
                            splitChinese[i] = splitChinese[i].substring(3,splitChinese[i].length()-2).trim();
                            //能来到这说明结果为true，双重判断，不要慌
                            obj.setResult(true);
                        }
                    }
                }

                obj.setContents(splitChinese);
            }else{
                obj.setContents(new String[]{});
            }
        }else{
            obj.setContents(new String[]{});
        }
        return obj;
    }

    public static Boolean checkChinese(String str){
        return selfJudge(str,HAS_CHINESE);
    }

    public static Boolean isChange(String str){
        return selfJudge(str,IS_CHANGE);
    }

    public static Boolean isNotSet(String str){
        return selfJudge(str,IS_NOT_SET);
    }

    public static Boolean isInternational(String str){
        return selfJudge(str,IS_INTERNATIONAL);
    }

    public static Boolean isNotQuotation(String str,String other){
        //普通符号
        String content = "[m³\\s\\,\\~\\?\\%\\!\\_\\，\\+/\\？\\！\\[\\]\\(\\)\\（\\）\\、\\-\\;\\；\\:\\：\\\\]{0,}";
        String IS_NOT_QUOTATION =
                "(^[\\s\\S]{0,}\\{\\{[\\s]{0,}"+other+"[\\s]{0,}\\}\\}[\\s\\S]{0,}$)|"+  //被{{}}包裹
                        "(^[\\s]{0,}\\{[\\s\\S]{0,}$)|(^[\\s\\S]{0,}\\}$)|(^[\\s\\S]{0,}\\,$)|"+ //大括号开始，大括号结束，逗号结束  特别注意有多个{{}} 的情况
                        "(^[\\s\\S]{0,}(label|title)[\\s]{0,}\\:[\\s]{0,}\""+other+"\"$)|"+ //一般标签的属性值为集合时
                        "(^[\\s\\S]{0,}(label|title)[\\s]{0,}\\:[\\s]{0,}\\`"+other+"\\`$)|"+ //一般标签的属性值为集合时
                        "(^[\\s\\S]{0,}(label|title)[\\s]{0,}\\:[\\s]{0,}\\'"+other+"\\'$)|"+ //一般标签的属性值为集合时
                        "(^[\\s\\S]{0,}\\'"+other+"\\'[\\s]{0,}\\,[\\s\\S]{0,}$)|"+ //遇到逗号时
                        "(^[\\s]{0,}(\\?|\\:)[\\s]{0,}\\'"+other+"\\'[\\s]{0,}$)|"+ //三木算法
                        "(^[\\s]{0,}(\\?|\\:)[\\s]{0,}\""+other+"\"[\\s]{0,}$)|"+ //三木算法
                        "(^[\\s]{0,}\""+other+"\"[\\s]{0,}$)|"+ //只有一行中文时
                        "(^[\\s]{0,}\\'"+other+"\\'[\\s]{0,}$)|"+ //只有一行中文时
                        "(^[\\s]{0,}"+other+"[\\s]{0,}$)|"+ //只有一行中文时
                        "(^[\\s\\S]{0,}\""+content+"\\'"+other+"\\'"+content+"\"[\\s\\S]{0,}$)|"+ //被包在双引号内
                        "(^[\\s\\S]{0,}\""+content+"\\`"+other+"\\`"+content+"\"[\\s\\S]{0,}$)"//被包在双引号内
                ;
        return selfJudge(str,IS_NOT_QUOTATION);
    }

    public static Boolean parthIgnoreChinese(String str,String other){
        String PARTH_IGNORE_CHINESE = "(^[\\s\\S]{0,}\\<\\!\\-\\-[\\s\\S]{0,}"+other+"[\\s\\S]{0,}\\-\\-\\>[\\s\\S]{0,}$)|"+
                "(^[\\s]{0,}//[\\s\\S]{0,}"+other+"[\\s\\S]{0,}$)|"+
                "(^[\\s\\S]{0,}/[\\s]{0,}\\*[\\s\\S]{0,}"+other+"[\\s\\S]{0,}$)|"+
                "(^[\\s]{0,}\\*[\\s\\S]{0,}"+other+"[\\s\\S]{0,}$)|"+
                "(^[\\s\\S]{0,}"+other+"[\\s\\S]{0,}\\*/$)"
                ;
        return selfJudge(str,PARTH_IGNORE_CHINESE);
    }
    //html 标签中的已国际化中文
    public static Boolean judgeHTMLpattern(String str,String other){
        String HTMLpattern = "(^[\\s\\S]{0,}\\{\\{\\s{0,}\\$t\\(\\'"+other+"\\'\\)\\s{0,}\\}\\}[\\s\\S]{0,}$)|"+
                "(^[\\s\\S]{0,}\\{\\{\\s{0,}\\$t\\(\""+other+"\"\\)\\s{0,}\\}\\}[\\s\\S]{0,}$)|"+
                "(^[\\s\\S]{0,}\\{\\{\\s{0,}\\$t\\(\\`"+other+"\\`\\)\\s{0,}\\}\\}[\\s\\S]{0,}$)";
        return selfJudge(str,HTMLpattern);
    }
    //html 标签中的属性国际化中文
    public static Boolean judgeInnerLabel(String str,String other,String strCell){
        String HTMLpattern = "(^[\\s\\S]{0,}"+strCell+"=\"[\\s\\S]{0,}"+other+"[\\s\\S]{0,}\"[\\s\\S]{0,}$)|"+
                "(^[\\s\\S]{0,}"+strCell+"=\\`[\\s\\S]{0,}"+other+"[\\s\\S]{0,}\\`[\\s\\S]{0,}$)|"+
                "(^[\\s\\S]{0,}"+strCell+"=\\'[\\s\\S]{0,}"+other+"[\\s\\S]{0,}\\'[\\s\\S]{0,}$)";
        return selfJudge(str,HTMLpattern);
    }
    //html 标签中的已国际化中文
    public static Boolean judgeInnertPattern(String str,String other){
        String InnertPattern = "(^[\\s\\S]{0,}\\$t\\(\\'\\s{0,}"+other+"\\s{0,}\\'\\)[\\s\\S]{0,}$)|"+
                "(^[\\s\\S]{0,}\\$t\\(\"\\s{0,}"+other+"\\s{0,}\"\\)[\\s\\S]{0,}$)|"+
                "(^[\\s\\S]{0,}\\$t\\(\\`\\s{0,}"+other+"\\s{0,}\\`\\)[\\s\\S]{0,}$)";
        return selfJudge(str,InnertPattern);
    }

    public static Boolean ignoreChinese(String str){
        return selfJudge(str,IGNORE_CHINESE);
    }

    public static Boolean isDefault(String str){
        return selfJudge(str,IS_DEFAULT);
    }

    public static Boolean isConsole(String str){
        return selfJudge(str,IS_CONSOLE);
    }

    public static Boolean selfJudge(String str,String pattern){
        Pattern ruler = Pattern.compile(pattern);
        Matcher other = ruler.matcher(str);
        return other.matches();
    }

}
