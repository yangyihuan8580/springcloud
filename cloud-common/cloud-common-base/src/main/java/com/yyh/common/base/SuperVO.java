package com.yyh.common.base;

import lombok.Data;

import java.io.Serializable;

@Data
public class SuperVO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 主键id 所有实体的主键都命名为id
     */
    private Long id;
}
