package com.sunseagear.wind.modules.email.service.impl;

import com.sunseagear.common.mvc.service.impl.CommonServiceImpl;
import com.sunseagear.wind.modules.email.entity.EmailSendLog;
import com.sunseagear.wind.modules.email.mapper.EmailSendLogMapper;
import com.sunseagear.wind.modules.email.service.IEmailSendLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.email.service.impl
 * @title: 邮件发送日志服务实现
 * @description: 邮件发送日志服务实现 * @date: 2018-09-12 10:58:46
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */
@Transactional
@Service("emailSendLogService")
public class EmailSendLogServiceImpl extends CommonServiceImpl<EmailSendLogMapper, EmailSendLog> implements IEmailSendLogService {

}
