package com.enroll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enroll.entity.SysUser;

public interface SysUserService extends IService<SysUser> {
    SysUser findByUsername(String username);
}
