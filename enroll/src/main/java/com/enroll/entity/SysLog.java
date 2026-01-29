package com.enroll.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_log")
public class SysLog {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 操作人ID（Sa-Token 的 loginId） */
    private Long userId;

    /** 操作人用户名（方便导师看审计） */
    private String username;

    /** 操作类型：AUDIT / DELETE / EXPORT 等 */
    private String action;

    /** 被操作对象：STUDENT */
    private String targetType;

    /** 被操作对象ID：student.id */
    private Long targetId;

    /** 审核前状态（可空） */
    private String beforeStatus;

    /** 审核后状态（可空） */
    private String afterStatus;

    /** 备注（比如导出条件、删除原因等，可空） */
    private String remark;

    /** 操作来源IP（可空） */
    private String ip;

    /** 操作时间（沿用你现有 MetaObjectHandler 的 INSERT 自动填充） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
