package com.zy.blog.vo;

import lombok.Data;

/**
 * @author 张岩
 * @version 1.0
 */
@Data
public class LoginUserVo {
    private String id;

    private String account;

    private String nickname;

    private String avatar; //头像
}
