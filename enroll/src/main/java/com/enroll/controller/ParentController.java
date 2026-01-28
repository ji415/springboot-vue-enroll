package com.enroll.controller;

import com.enroll.common.R;
import com.enroll.dto.StudentSubmitDTO;
import com.enroll.entity.Student;
import com.enroll.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parent")
public class ParentController {

    private final StudentService studentService;

    public ParentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // 提交/驳回后再提交
    @PostMapping("/submit")
    public R<Student> submit(@RequestBody @Valid StudentSubmitDTO dto) {
        return R.ok(studentService.submitOrResubmit(dto));
    }

    // 查看进度（按身份证号查询）
    @GetMapping("/progress")
    public R<Student> progress(@RequestParam String idCard) {
        Student s = studentService.getByIdCard(idCard);
        if (s == null) return R.fail("未找到该身份证号的报名记录");
        return R.ok(s);
    }
}
