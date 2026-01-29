package com.enroll.vo;

import com.enroll.entity.Student;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StudentVO {
    private Long id;
    private String name;
    private String idCard;
    private String parentPhone;
    private String address;

    /** 关键：直接给前端字符串枚举 */
    private String status;        // PENDING / APPROVED / REJECTED
    private String statusLabelCn; // 待审核 / 通过 / 驳回

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public static StudentVO from(Student s) {
        if (s == null) return null;
        StudentVO vo = new StudentVO();
        vo.setId(s.getId());
        vo.setName(s.getName());
        vo.setIdCard(s.getIdCard());
        vo.setParentPhone(s.getParentPhone());
        vo.setAddress(s.getAddress());

        // 这里用你 Step2 已经加好的计算字段（不会出现数字）
        vo.setStatus(s.getStatusKey());
        vo.setStatusLabelCn(s.getStatusLabelCn());

        vo.setCreateTime(s.getCreateTime());
        vo.setUpdateTime(s.getUpdateTime());
        return vo;
    }
}
