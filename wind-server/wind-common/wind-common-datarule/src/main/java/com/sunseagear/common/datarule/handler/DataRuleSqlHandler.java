package com.sunseagear.common.datarule.handler;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
public class DataRuleSqlHandler {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    private static GroupTemplate groupTemplate;

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

    public String sqlCondition(DataRuleModel dataScope, Principal principal, String originalSql) {

        //判断数据权限类型并组装对应Sql
        Integer scopeRule = Objects.requireNonNull(dataScope).getScopeType();
        String id = "";
        Map<String, Object> userMap = dataRuleHandler.getUser(principal.getId());
        if (DataRuleModel.ALL == scopeRule) {
            return null;
        } else if (DataRuleModel.CUSTOM == scopeRule) {
            Template template = groupTemplate.getTemplate(StringEscapeUtils.unescapeHtml4(dataScope.getScopeValue()));
            if (template.program instanceof ErrorGrammarProgram){
                logger.info(((ErrorGrammarProgram)template.program).getException().detailCode);
            }
            userMap.forEach(template::binding);
            String whereSql =  template.render();
            return String.format(" select %s from (%s) scope " + whereSql, dataScope.getScopeField(), originalSql);
        } else if (DataRuleModel.OWN == scopeRule) {
            String whereSql = "where scope.%s = '%s'";
            id = principal.getId();
            return String.format(" select %s from (%s) scope " + whereSql, dataScope.getScopeField(), originalSql, dataScope.getScopeColumn(), id);

        } else if (DataRuleModel.OWN_ORG == scopeRule) {
            id = (String) userMap.get(dataScope.getUserEntityField());
            if (StringUtils.isEmpty(id)) {
                logger.error(String.format("DataRuleSqlHandler OWN_ORG error id is not exist userId:%s,UserColumn：%s", principal.getId(), dataScope.getUserEntityField()));
                return null;
            }
            return String.format("select %s from (%s) scope, %s a, sys_user sysu where a.id = sysu.%s" +
                            " and sysu.id = scope.%s and a.id = '%s'",
                    dataScope.getScopeField(), originalSql,
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
            return String.format("select %s from (%s) scope, %s a, sys_user sysu where a.id = sysu.%s" +
                            " and sysu.id = scope.%s and a.id in ('%s')",
                    dataScope.getScopeField(), originalSql,
                    dataScope.getTableName(),
                    dataScope.getUserColumn(), dataScope.getScopeColumn(),
                    id);
        }
        return null;
    }

}
