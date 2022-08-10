package com.sunseagear.common.datarule.config;

import com.sunseagear.common.datarule.handler.DataRuleHandler;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;


@Configuration
@AllArgsConstructor
public class DataScopeConfiguration {

    private JdbcTemplate jdbcTemplate;

    @Bean
    @ConditionalOnMissingBean(DataRuleHandler.class)
    public DataRuleHandler dataRuleHandler() {
        DataRuleHandler dataRuleHandler = new DataRuleHandler(jdbcTemplate);
        dataRuleHandler.init();
        return dataRuleHandler;
    }

}
