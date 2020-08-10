package com.yyh.common.factory;

import com.yyh.common.base.ID;
import com.yyh.common.util.SnowFlakeStrategy;

public class IDFactory {

    public static final ID createID() {
        return new ID(SnowFlakeStrategy.getPrimaryId());
    }
}
