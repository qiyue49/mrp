package com.sunseagear.wind.config;

import com.sunseagear.common.utils.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * All rights Reserved, Designed By www.gangwantech.com
 *
 * @version V1.0
 * @package com.gangwantech.spring
 * @title:
 * @description: WEB 初始化相关配置 * @date: 2018/2/22 12:35
 * @copyright: 2017 www.gangwantech.com Inc. All rights reserved.
 */

@ControllerAdvice
@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();//4
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
//        FastjsonUnXssFilter jsonUnFilter = new FastjsonUnXssFilter();
//        fastJsonConfig.setSerializeFilters(jsonUnFilter);
//        //处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<MediaType>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        converter.setSupportedMediaTypes(fastMediaTypes);
//        converter.setFastJsonConfig(fastJsonConfig);
//        converters.add(converter);
//    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

}
