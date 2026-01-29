package com.enroll.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.enroll.entity.SysLog;
import com.enroll.mapper.SysLogMapper;
import com.enroll.service.SysLogService;
import org.springframework.stereotype.Service;

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {}
