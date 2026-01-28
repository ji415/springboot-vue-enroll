package com.enroll.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.enroll.dto.StudentSubmitDTO;
import com.enroll.entity.Student;

public interface StudentService extends IService<Student> {
    Student submitOrResubmit(StudentSubmitDTO dto);
    Student getByIdCard(String idCard);
    void audit(Long id, Integer status);
}
