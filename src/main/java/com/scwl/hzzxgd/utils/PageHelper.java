package com.scwl.hzzxgd.utils;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;

@Data
@Accessors(chain = true)
public class PageHelper {
    /**
     * 每页数量
     */
    private int PageSize;
    /**
     * 当前为第几页,从1开始
     */
    private int PageIndex;
    /**
     * 总页数
     */
    private int totalPages;
    /**
     * 总记录数
     */
    private Long totalElements;
    /**
     * 当前页内容
     */
    private List<Object> content;
}
