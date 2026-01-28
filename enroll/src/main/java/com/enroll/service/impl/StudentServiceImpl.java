package com.enroll.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enroll.dto.StudentSubmitDTO;
import com.enroll.entity.Student;
import com.enroll.mapper.StudentMapper;
import com.enroll.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

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
    public void audit(Long id, Integer status) {
        if (status == null || (status != 1 && status != 2)) {
            throw new RuntimeException("审核状态非法");
        }
        Student s = this.getById(id);
        if (s == null) throw new RuntimeException("记录不存在");
        if (s.getStatus() == null || s.getStatus() != 0) {
            throw new RuntimeException("仅待审核记录可审核");
        }
        s.setStatus(status);
        this.updateById(s);
    }
}
