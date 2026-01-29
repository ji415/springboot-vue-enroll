package com.enroll.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.enroll.common.R;
import com.enroll.dto.LoginDTO;
import com.enroll.entity.SysUser;
import com.enroll.service.SysUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final SysUserService userService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public AuthController(SysUserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public R<?> login(@RequestBody LoginDTO dto) {
        SysUser u = userService.findByUsername(dto.getUsername());
        if (u == null) return R.fail("用户名或密码错误");

        if (!encoder.matches(dto.getPassword(), u.getPassword())) {
            return R.fail("用户名或密码错误");
        }

        StpUtil.login(u.getId());

        // 在 token session 写入角色
        StpUtil.getTokenSession().set("role", u.getRole());

        Map<String, Object> data = new HashMap<>();
        data.put("token", StpUtil.getTokenValue());
        data.put("role", u.getRole());
        return R.ok(data);
    }

    @PostMapping("/logout")
    public R<?> logout() {
        StpUtil.logout();
        return R.ok(true);
    }
}
