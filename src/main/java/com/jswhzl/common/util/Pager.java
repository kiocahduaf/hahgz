package com.jswhzl.common.util;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * @author xuchao
 * @ClassName Pager
 * @Description
 * @date 2018-10-17 17:32
 **/
@Data
public class Pager<T> extends Page<T> {

    private static final long serialVersionUID = -9073299106170920089L;
    /*private long page;
    private long limit;*/

    public Pager() {
        super();
    }

    public Pager(long page, long limit) {
        super(page, limit);
    }

    public Pager(long page, long limit, long count) {
        super(page, limit, count);
    }

    /*public <T> Page<T> initPage() {
        return new Page<T>(this.page, this.limit);
    }*/
}
