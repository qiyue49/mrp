package com.sunseagear.common.datarule.handler;

import com.baomidou.mybatisplus.extension.plugins.handler.DataPermissionHandler;
import com.sunseagear.common.datarule.model.DataRuleModel;
import com.sunseagear.common.utils.StringUtils;
import com.sunseagear.common.utils.UserUtils;
import com.sunseagear.common.utils.entity.Principal;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import org.apache.commons.text.StringEscapeUtils;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.beetl.core.statement.ErrorGrammarProgram;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class MyDataPermissionHandler implements DataPermissionHandler {

    private final DataRuleHandler dataRuleHandler;
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

    /**
     * @param where             原SQL Where 条件表达式
     * @param mappedStatementId Mapper接口方法ID
     * @return
     */
    @SneakyThrows
    @Override
    public Expression getSqlSegment(Expression where, String mappedStatementId) {
        //未取到用户则放行
        Principal principal = UserUtils.getPrincipal();
        if (principal == null) {
            return where;
        }
        Map<String, Object> userMap = dataRuleHandler.getUser(principal.getId().toString());
        DataRuleModel dataScope = dataRuleHandler.getDataRule(mappedStatementId, principal.getRoleId());
        ;
        //如果还不行，那就只有不处理了
        if (dataScope == null) {
            return where;
        }
        Integer scopeRule = Objects.requireNonNull(dataScope).getScopeType();
        if (DataRuleModel.ALL == scopeRule) {
            return where;
        }

        if (DataRuleModel.CUSTOM == scopeRule) {
            Template template = groupTemplate.getTemplate(StringEscapeUtils.unescapeHtml4(dataScope.getScopeValue()));
            if (template.program instanceof ErrorGrammarProgram) {
                log.info(((ErrorGrammarProgram) template.program).getException().detailCode);
                return where;
            }
            userMap.forEach(template::binding);
            String whereSql = template.render();
            String sql = "select * from table where " + whereSql.replace("where", "");
            CCJSqlParserManager parserManager = new CCJSqlParserManager();
            Select select = (Select) parserManager.parse(new StringReader(sql));
            PlainSelect plain = (PlainSelect) select.getSelectBody();
            Expression where_expression = plain.getWhere();
            return new AndExpression(where, where_expression);

        } else if (DataRuleModel.OWN == scopeRule) {
            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression(new Column(dataScope.getScopeColumn()));
            equalsTo.setRightExpression(new StringValue(principal.getId().toString()));
            return new AndExpression(where, equalsTo);

        } else if (DataRuleModel.OWN_ORG == scopeRule) {
            String id = (String) userMap.get(dataScope.getUserEntityField());
            if (StringUtils.isEmpty(id)) {
                log.error(String.format("DataRuleSqlHandler OWN_ORG error id is not exist userId:%s,UserColumn：%s", principal.getId(), dataScope.getUserEntityField()));
                return where;
            }
            EqualsTo equalsTo = new EqualsTo(); // order_tbl.dept_id = userDeptId
            equalsTo.setLeftExpression(new Column(dataScope.getTableName() + "." + dataScope.getScopeColumn()));
            equalsTo.setRightExpression(new StringValue(id));
            // 创建 AND 表达式 拼接Where 和 = 表达式
            return new AndExpression(where, equalsTo); // WHERE user_id = 2 AND order_tbl.dept_id = 3

        } else if (DataRuleModel.OWN_ORG_CHILDREN == scopeRule) {
            String id = (String) userMap.get(dataScope.getUserEntityField());
            if (StringUtils.isEmpty(id)) {
                log.error(String.format("DataRuleSqlHandler OWN_ORG_CHILDREN error id is not exist userId:%s,UserColumn：%s", principal.getId(), dataScope.getUserEntityField()));
                return where;
            }

            Set<String> ids = new LinkedHashSet<>();
            ids.add(id);
            List<String> deptIdList = dataRuleHandler.getChildren(dataScope.getTableName(), id);
            ids.addAll(deptIdList);

            ItemsList itemsList = new ExpressionList(ids.stream().map(StringValue::new).collect(Collectors.toList())); // 把集合转变为JSQLParser需要的元素列表
            InExpression inExpression = new InExpression(new Column(dataScope.getTableName() + "." + dataScope.getScopeColumn()), itemsList);
            return new AndExpression(where, inExpression);

        }
        return where;
    }

}

