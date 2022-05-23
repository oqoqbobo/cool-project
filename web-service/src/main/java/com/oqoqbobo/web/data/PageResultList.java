package com.oqoqbobo.web.data;

import lombok.Data;

import java.util.List;

@Data
public class PageResultList<T> {
    private Integer pageNo;

    private Integer pageSize;

    private Long total;

    private Long pageCount;

    private List<T> result;

    public PageResultList(PageQuery pageQuery,List<T> result){
        this.pageNo = pageQuery.getPageNo();
        this.pageSize = pageQuery.getPageSize() <= 0 ? 1 : pageQuery.getPageSize();
        this.result = result;
        this.total = pageQuery.getTotal() == null?0:pageQuery.getTotal();
        this.pageCount = this.total % this.pageSize > 0 ?
                this.total / this.pageSize + 1 :
                this.total / this.pageSize;
    }

    public static PageResultList result(PageQuery pageQuery,List result){
        return new PageResultList(pageQuery,result);
    }
}
