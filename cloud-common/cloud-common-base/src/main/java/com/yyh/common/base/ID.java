package com.yyh.common.base;

import com.yyh.common.exception.IDRuntimeException;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
public class ID implements Serializable {

    private Long id;

    public ID(Long id) {
        if (id <= 0L) {
            throw new IDRuntimeException();
        }
        this.id = id;
    }


    public Long getValue() {
        return this.id;
    }

    public void setValue(Long id) {
        this.id = id;
    }
}
