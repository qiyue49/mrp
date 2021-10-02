package com.sunseagear.common.datarule.handler;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.sunseagear.common.datarule.model.DataRuleModel;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.common.utils.entity.Principal;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.beetl.core.statement.ErrorGrammarProgram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class DataRuleSqlHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static GroupTemplate groupTemplate;

    @Value("${spring.datasource.driver-class-name}")
    String driverClassName;

    static {
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration configuration = null;
        try {
            configuration = Configuration.defaultConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        groupTemplate = new GroupTemplate(resourceLoader, configuration);
    }


    private final DataRuleHandler dataRuleHandler;

    private String getDataSourceType() {
        if (driverClassName.contains("mysql")) {
            return JdbcConstants.MYSQL;
        } else if (driverClassName.contains("sqlserver")) {
            return JdbcConstants.SQL_SERVER;
        } else if (driverClassName.contains("oracle")) {
            return JdbcConstants.ORACLE;
        } else {
            return JdbcConstants.MYSQL;
        }
    }

    public String sqlCondition(DataRuleModel dataScope, Principal principal, String originalSql) {

        //判断数据权限类型并组装对应Sql
        Integer scopeRule = Objects.requireNonNull(dataScope).getScopeType();
        if (DataRuleModel.ALL == scopeRule) {
            return null;
        }
        Map<String, Object> userMap = dataRuleHandler.getUser(principal.getId());
        SQLSelectStatement selectStmt = (SQLSelectStatement) SQLUtils.parseSingleStatement(originalSql, getDataSourceType());
        SQLSelectQueryBlock mySqlSelectQueryBlock = (SQLSelectQueryBlock) selectStmt.getSelect().getQuery();
        SQLLimit limit = mySqlSelectQueryBlock.getLimit();
        mySqlSelectQueryBlock.setLimit(null);
        String tempSql = selectStmt.toString();
        String id = "";
        String result = "";
        if (DataRuleModel.CUSTOM == scopeRule) {
            Template template = groupTemplate.getTemplate(StringEscapeUtils.unescapeHtml4(dataScope.getScopeValue()));
            if (template.program instanceof ErrorGrammarProgram) {
                logger.info(((ErrorGrammarProgram) template.program).getException().detailCode);
            }
            userMap.forEach(template::binding);
            String whereSql = template.render();
            result = String.format(" select %s from (%s) scope " + whereSql, dataScope.getScopeField(), tempSql);
        } else if (DataRuleModel.OWN == scopeRule) {
            String whereSql = "where scope.%s = '%s'";
            id = principal.getId();
            result = String.format(" select %s from (%s) scope " + whereSql, dataScope.getScopeField(), tempSql, dataScope.getScopeColumn(), id);
        } else if (DataRuleModel.OWN_ORG == scopeRule) {
            id = (String) userMap.get(dataScope.getUserEntityField());
            if (StringUtils.isEmpty(id)) {
                logger.error(String.format("DataRuleSqlHandler OWN_ORG error id is not exist userId:%s,UserColumn：%s", principal.getId(), dataScope.getUserEntityField()));
                return null;
            }
            result = String.format("select %s from (%s) scope, %s a, sys_user sysu where a.id = sysu.%s" +
                            " and sysu.id = scope.%s and a.id = '%s'",
                    dataScope.getScopeField(), tempSql,
                    dataScope.getTableName(),
                    dataScope.getUserColumn(), dataScope.getScopeColumn(),
                    id);
        } else if (DataRuleModel.OWN_ORG_CHILDREN == scopeRule) {
            id = (String) userMap.get(dataScope.getUserEntityField());
            if (StringUtils.isEmpty(id)) {
                logger.error(String.format("DataRuleSqlHandler OWN_ORG_CHILDREN error id is not exist userId:%s,UserColumn：%s", principal.getId(), dataScope.getUserEntityField()));
                return null;
            }
            List<String> ids = new ArrayList<>();
            ids.add(id);
            List<String> deptIdList = dataRuleHandler.getChildren(dataScope.getTableName(), id);
            ids.addAll(deptIdList);
            id = StringUtils.join(ids, "','");
            result = String.format("select %s from (%s) scope, %s a, sys_user sysu where a.id = sysu.%s" +
                            " and sysu.id = scope.%s and a.id in ('%s')",
                    dataScope.getScopeField(), tempSql,
                    dataScope.getTableName(),
                    dataScope.getUserColumn(), dataScope.getScopeColumn(),
                    id);
        }
        SQLSelectStatement selectStmtResult = (SQLSelectStatement) SQLUtils.parseSingleStatement(result, getDataSourceType());
        SQLSelectQueryBlock mySqlSelectQueryBlockResult = (SQLSelectQueryBlock) selectStmtResult.getSelect().getQuery();
        mySqlSelectQueryBlockResult.setLimit(limit);
        logger.info(String.format("result sql: %s", selectStmtResult));
        return selectStmtResult.toString();
    }

}
