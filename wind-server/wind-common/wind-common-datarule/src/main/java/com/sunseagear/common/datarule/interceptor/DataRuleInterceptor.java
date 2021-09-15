package com.sunseagear.common.datarule.interceptor;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.sunseagear.common.datarule.handler.DataRuleHandler;
import com.sunseagear.common.datarule.handler.DataRuleSqlHandler;
import com.sunseagear.common.datarule.model.DataRuleModel;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.common.utils.UserUtils;
import com.sunseagear.common.utils.entity.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.util.Properties;



@Slf4j
@RequiredArgsConstructor
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataRuleInterceptor extends AbstractSqlParserHandler implements Interceptor {


    private final DataRuleSqlHandler dataRuleSqlHandler;
    private final DataRuleHandler dataRuleHandler;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //未取到用户则放行
        Principal principal = UserUtils.getPrincipal();
        if (principal == null) {
            return invocation.proceed();
        }

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);

        //非SELECT操作放行
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (SqlCommandType.SELECT != mappedStatement.getSqlCommandType()
                || StatementType.CALLABLE == mappedStatement.getStatementType()) {
            return invocation.proceed();
        }

        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        String originalSql = boundSql.getSql();

        //注解为空并且数据权限方法名未匹配到,则放行
        String mapperId = mappedStatement.getId();
        DataRuleModel dataScope = dataRuleHandler.getDataRuleModel(mapperId);
        if (dataScope != null) {
            dataScope = dataRuleHandler.getDataRule(mapperId, principal.getRoleId());

//			String className = mapperId.substring(0, mapperId.lastIndexOf(StringPool.DOT));
//			String mapperName = ClassUtil.getShortName(className);
//			String methodName = mapperId.substring(mapperId.lastIndexOf(StringPool.DOT) + 1);
//			boolean mapperSkip = dataScopeProperties.getMapperKey().stream().noneMatch(methodName::contains)
//					|| dataScopeProperties.getMapperExclude().stream().anyMatch(mapperName::contains);
//			//动态配置生效，以动态配置为准
//			if (!mapperSkip) {
//				dataScope = dataRuleHandler.getDataRule(mapperId, principal.getRoleId());
//			}
        }
        //如果还不行，那就只有不处理了
        if (dataScope == null) {
            return invocation.proceed();
        }

        //获取数据权限规则对应的筛选Sql
        String sqlCondition = dataRuleSqlHandler.sqlCondition(dataScope, principal, originalSql);
        if (StringUtils.isBlank(sqlCondition)) {
            return invocation.proceed();
        } else {
            metaObject.setValue("delegate.boundSql.sql", sqlCondition);
            return invocation.proceed();
        }
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }


}
