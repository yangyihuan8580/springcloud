package com.yyh.common.base;

public interface BaseRepository<I extends ID,ENTITY> {

    ENTITY save(ENTITY order);

    void remove(I id);

    ENTITY queryOne(I id);
}
