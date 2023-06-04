package com.sunseagear.common.sms.disruptor;

import lombok.Data;

/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @title: LongEvent.java
 * @package com.sunseagear.sms.common.disruptor.sms
 * @description: 内容传递 * @date: 2017年6月7日 下午11:17:40
 * @copyright: 2017 www.sunseagear.com Inc. All rights reserved.
 */
@Data
public class SmsEvent {
    private SmsData smsData;
    private Long id;
    private SmsHandlerCallBack handlerCallBack;
}
