package com.enroll.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import com.enroll.enums.StudentStatus;

@Data
@TableName("student")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    @TableField("id_card")
    private String idCard;

    @TableField("parent_phone")
    private String parentPhone;

    private String address;

    /** 0待审核 1通过 2驳回 */
    private Integer status;

    @TableLogic
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    // ====== 计算字段：给前端/日志用（不入库） ======
    public String getStatusKey() {
        StudentStatus s = StudentStatus.fromCode(this.status);
        return s == null ? null : s.getKey();
    }

    public String getStatusLabelCn() {
        StudentStatus s = StudentStatus.fromCode(this.status);
        return s == null ? null : s.getLabelCn();
    }

}
