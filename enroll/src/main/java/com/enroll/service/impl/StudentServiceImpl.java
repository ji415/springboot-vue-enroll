package com.enroll.service.impl;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.context.SaHolder;
import com.enroll.entity.SysLog;
import com.enroll.entity.SysUser;
import com.enroll.enums.StudentStatus;
import com.enroll.mapper.SysLogMapper;
import com.enroll.service.SysUserService;

import java.io.Serializable;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enroll.dto.StudentSubmitDTO;
import com.enroll.entity.Student;
import com.enroll.mapper.StudentMapper;
import com.enroll.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {
    private final SysLogMapper sysLogMapper;
    private final SysUserService sysUserService;

    public StudentServiceImpl(SysLogMapper sysLogMapper, SysUserService sysUserService) {
        this.sysLogMapper = sysLogMapper;
        this.sysUserService = sysUserService;
    }

    @Override
    public Student getByIdCard(String idCard) {
        return this.getOne(new LambdaQueryWrapper<Student>()
                .eq(Student::getIdCard, idCard)
                .last("limit 1"));
    }

    @Override
    @Transactional
    public Student submitOrResubmit(StudentSubmitDTO dto) {
        Student exist = getByIdCard(dto.getIdCard());

        if (exist == null) {
            Student s = new Student();
            s.setName(dto.getName());
            s.setIdCard(dto.getIdCard());
            s.setParentPhone(dto.getParentPhone());
            s.setAddress(dto.getAddress());
            s.setStatus(0);
            this.save(s);
            return s;
        }

        // 已存在：如果是驳回(2)允许修改并重新提交（状态重置为0）
        if (exist.getStatus() != null && exist.getStatus() == 2) {
            exist.setName(dto.getName());
            exist.setParentPhone(dto.getParentPhone());
            exist.setAddress(dto.getAddress());
            exist.setStatus(0);
            this.updateById(exist);
            return exist;
        }

        // 待审核/通过不允许重复提交
        throw new RuntimeException("该身份证号已提交报名，当前状态不允许重复提交");
    }

    @Override
    @Transactional
    public void audit(Long id, Integer status) {
        if (status == null || (status != 1 && status != 2)) {
            throw new RuntimeException("审核状态非法");
        }
        Student s = this.getById(id);
        if (s == null) throw new RuntimeException("记录不存在");
        if (s.getStatus() == null || s.getStatus() != 0) {
            throw new RuntimeException("仅待审核记录可审核");
        }

        String beforeKey = statusKey(s.getStatus());
        String afterKey = statusKey(status);

        s.setStatus(status);
        this.updateById(s);

        insertLog("AUDIT", id, beforeKey, afterKey, null);
    }
    @Override
    @Transactional
    public boolean removeById(Serializable id) {
        Long studentId = null;
        if (id instanceof Long) studentId = (Long) id;
        else if (id instanceof String) {
            try { studentId = Long.parseLong((String) id); } catch (Exception ignored) {}
        }

        Student before = (studentId == null) ? null : this.getById(studentId);
        String beforeKey = before == null ? null : statusKey(before.getStatus());

        boolean ok = super.removeById(id);
        if (ok && studentId != null) {
            insertLog("DELETE", studentId, beforeKey, null, null);
        }
        return ok;
    }
    private String statusKey(Integer code) {
        StudentStatus st = StudentStatus.fromCode(code);
        return st == null ? null : st.getKey();
    }

    private void insertLog(String action, Long studentId, String beforeStatus, String afterStatus, String remark) {
        Long userId = -1L;
        String username = "unknown";

        try {
            userId = StpUtil.getLoginIdAsLong();
            SysUser u = sysUserService.getById(userId);
            if (u != null && u.getUsername() != null) username = u.getUsername();
        } catch (Exception ignored) {}

//        String ip = null;
//        try { ip = SaHolder.getRequest().getRemoteAddr(); } catch (Exception ignored) {}
        String ip = null;
        try {
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attrs != null) {
                HttpServletRequest req = attrs.getRequest();
                ip = req.getRemoteAddr();
            }
        } catch (Exception ignored) {}

        SysLog log = new SysLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setAction(action);
        log.setTargetType("STUDENT");
        log.setTargetId(studentId);
        log.setBeforeStatus(beforeStatus);
        log.setAfterStatus(afterStatus);
        log.setRemark(remark);
        log.setIp(ip);

        sysLogMapper.insert(log);
    }

}
