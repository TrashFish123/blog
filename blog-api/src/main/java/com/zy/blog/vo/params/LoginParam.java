package com.zy.blog.vo.params;

import lombok.Data;

/**
 * @author 张岩
 * @version 1.0
 */
@Data
public class LoginParam {
    private String account;

    private String password;

    private String nickname;
}
