package com.oqoqbobo.web.config;

import com.oqoqbobo.web.data.MyException;
import com.oqoqbobo.web.data.PageQuery;
import com.oqoqbobo.web.utils.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * 2022-04-25
 * cjb
 *  Mybatis拦截器，拦截StatementHandler 的 prepare方法（查询数据库时进行拦截  特别注意）
 *  prepare: 用于创建一个具体的 Statement 对象的实现类或者是 Statement 对象
 *  如果方法参数满足继承 PageQuery时，需要对结果进行分页查询
 */
@Component
@Intercepts( {
        @Signature(method = "prepare", type = StatementHandler.class, args = {
                Connection.class, Integer.class}) })
public class SqlInterceptor implements Interceptor {

    //    从代理对象中分离出真实statementHandler对象,非代理对象
    private StatementHandler getActuralHandlerObject(Invocation invocation) {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStatementHandler = SystemMetaObject.forObject(statementHandler);
        Object object = null;
//        分离代理对象链，目标可能被多个拦截器拦截，分离出最原始的目标类
        while (metaStatementHandler.hasGetter("h")) {
            object = metaStatementHandler.getValue("h");
            metaStatementHandler = SystemMetaObject.forObject(object);
        }

        if (object == null) {
            return statementHandler;
        }
        return (StatementHandler) object;
    }
    //拦截成功后进入该方法，凡是进入该拦截方法的，都是数据库操作语句，这是myBatis提供的方法拦截器（不要怀疑）
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler statementHandler = getActuralHandlerObject(invocation);
        MetaObject meta = SystemMetaObject.forObject(statementHandler);  //获取反射对象，可通过反射getValue获取对应属性下的值

        BoundSql boundSql = statementHandler.getBoundSql();
        Object param =  boundSql.getParameterObject();
        //如果接口参数不是继承 PageQuery（专门用于分页的），直接返回
        if(param == null){
            return invocation.proceed();
        }else if(!(param instanceof PageQuery)){
            return invocation.proceed();
        }
        //如果参数继承了PageQuery  需要进行分页处理，给查询SQL添加分页语句
        PageQuery page = (PageQuery) param;

        String sql = boundSql.getSql();
        String countSql = "select count(*) from (" + sql + ") temp";

        try{
            //这里可以拿到Connection 是因为拦截的是 StatementHandler 的 prepare方法，特别注意
            Connection con = (Connection)invocation.getArgs()[0];
            //预编译查询数量sql
            PreparedStatement countStatement = con.prepareStatement(countSql);
            ParameterHandler parameterHandler = (ParameterHandler) meta.getValue("delegate.parameterHandler");
            parameterHandler.setParameters(countStatement);
            //执行
            ResultSet rs = countStatement.executeQuery();
            if (rs.next()) {
                //为什么是getInt（1）? 因为数据表的列是从1开始计数
                Integer anInt = rs.getInt(1);
                page.setTotal(Long.parseLong(anInt.toString()));
//                System.out.println("拦截器得知page的记录总数为：" + page.getTotal());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        if(page.getPageNo() < 1){
            page.setPageNo(1);
        }
        String pageSql = (String) meta.getValue("delegate.boundSql.sql");
        String limitSql = "";
        if(StringUtils.isNotBlank(page.getPageSort())){
            //默认sort的内容满足   属性 + 排序 （desc || asc || 不写，默认升序）
            limitSql = "SELECT TEMP.* FROM (" + pageSql + ") TEMP "+ "ORDER BY "+ changePageSort(page.getPageSort()) +" LIMIT " + (page.getPageNo()-1) * page.getPageSize() + "," + page.getPageSize();
        }else{
            //构建新的分页sql语句
            limitSql = "SELECT TEMP.* FROM (" + pageSql + ") TEMP LIMIT " + (page.getPageNo()-1) * page.getPageSize() + "," + page.getPageSize();
        }



        //修改当前要执行的sql语句
        meta.setValue("delegate.boundSql.sql", limitSql);

        return invocation.proceed();
    }

    /**
     * pageSort : xxx_xxx,yyy_yyy desc,ggg_ggg asc
     * @param pageSort
     * @return
     */
    private String changePageSort(String pageSort) throws MyException {
        StringBuilder sb = new StringBuilder();
        List<String> collect = Arrays.asList(pageSort.split(","))
                .stream()
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toList());
        for(String sort : collect){
            List<String> column = Arrays.asList(sort.split(" "))
                    .stream()
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());

            String first = StringUtils.camelToUnderline(column.get(0));
            if(column.size()>1){
                sb.append(" TEMP.").append(first).append(" ").append(column.get(1)).append(",");
            }else {
                sb.append(" TEMP.").append(first).append(" asc,");
            }
        }
        return sb.substring(0,sb.length()-1);
    }



    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    public void setProperties(Properties properties) {
        String prop1 = properties.getProperty("prop1");
        String prop2 = properties.getProperty("prop2");
        System.out.println(prop1 + "------" + prop2);
    }

}