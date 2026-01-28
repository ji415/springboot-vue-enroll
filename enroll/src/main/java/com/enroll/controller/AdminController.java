package com.enroll.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.enroll.common.R;
import com.enroll.entity.Student;
import com.enroll.service.StudentService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.alibaba.excel.EasyExcel;
import com.enroll.vo.StudentExportVO;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/admin/students")
public class AdminController {

    private final StudentService studentService;

    public AdminController(StudentService studentService) {
        this.studentService = studentService;
    }

    // 分页 + 姓名/手机号模糊搜索
    @GetMapping
    public R<Page<Student>> page(
            @RequestParam(defaultValue = "1") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String keyword
    ) {
        LambdaQueryWrapper<Student> qw = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            qw.and(w -> w.like(Student::getName, keyword)
                    .or()
                    .like(Student::getParentPhone, keyword));
        }
        qw.orderByDesc(Student::getCreateTime);
        return R.ok(studentService.page(new Page<>(page, size), qw));
    }

    // 详情
//    @GetMapping("/{id}")
    // 详情（只匹配数字 id）
    @GetMapping("/{id:\\d+}")
    public R<Student> detail(@PathVariable Long id) {
        Student s = studentService.getById(id);
        if (s == null) return R.fail("记录不存在");
        return R.ok(s);
    }

    // 审核：通过(1) / 驳回(2)
//    @PutMapping("/{id}/audit")
    // 审核（只匹配数字 id）
    @PutMapping("/{id:\\d+}/audit")

    public R<?> audit(@PathVariable Long id, @RequestParam Integer status) {
        studentService.audit(id, status);
        return R.ok(true);
    }

    // 删除（若配置了@TableLogic就是逻辑删）
//    @DeleteMapping("/{id}")
    // 删除（只匹配数字 id）
    @DeleteMapping("/{id:\\d+}")
    public R<?> delete(@PathVariable Long id) {
        studentService.removeById(id);
        return R.ok(true);
    }

    // 导出“通过审核”的学生名单（status=1）
    @GetMapping("/export-approved")
    public void exportApproved(HttpServletResponse response) throws Exception {
        // 1) 查通过的学生
        List<Student> list = studentService.list(new LambdaQueryWrapper<Student>()
                .eq(Student::getStatus, 1)
                .orderByDesc(Student::getCreateTime)
        );

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 2) 转 VO
        List<StudentExportVO> rows = list.stream().map(s -> {
            StudentExportVO vo = new StudentExportVO();
            vo.setName(s.getName());
            vo.setIdCard(s.getIdCard());
            vo.setParentPhone(s.getParentPhone());
            vo.setAddress(s.getAddress());
            vo.setCreateTime(s.getCreateTime() == null ? "" : s.getCreateTime().format(fmt));
            return vo;
        }).collect(Collectors.toList());

        // 3) 下载响应头
        String fileName = URLEncoder.encode("通过审核学生名单", StandardCharsets.UTF_8) + ".xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename*=UTF-8''" + fileName);

        // 4) 写出 Excel
        EasyExcel.write(response.getOutputStream(), StudentExportVO.class)
                .sheet("通过审核")
                .doWrite(rows);
    }
}
