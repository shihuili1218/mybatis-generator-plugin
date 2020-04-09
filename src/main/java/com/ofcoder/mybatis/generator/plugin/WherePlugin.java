package com.ofcoder.mybatis.generator.plugin;

import com.ofcoder.util.PropertyUtil;
import com.ofcoder.util.StringUtil;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.util.List;

/**
 * @author: yuanyuan.liu
 * @date: 2020/4/2 14:29
 */
public class WherePlugin extends PluginAdapter {
    private static final String ASSIST_WHERE = PropertyUtil.Param.Assist.WHERE;

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    private String getAppendWhere(IntrospectedTable introspectedTable) {
        String where = introspectedTable.getTableConfigurationProperty(ASSIST_WHERE);
        if (StringUtil.isNotEmpty(where)) {
            // todo ignore Case
            if (!StringUtil.startWith(where.trim(), "and")) {
                where = " and " + where;
            }
        }
        return where;
    }

    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        String appendWhere = getAppendWhere(introspectedTable);
        if (StringUtil.isNotEmpty(appendWhere)) {
            TextElement text = new TextElement(appendWhere);
            element.addElement(text);
        }
        return super.sqlMapSelectByPrimaryKeyElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        String appendWhere = getAppendWhere(introspectedTable);
        if (StringUtil.isNotEmpty(appendWhere)) {
            TextElement text = new TextElement(appendWhere);
            element.addElement(text);
        }
        return super.sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        String appendWhere = getAppendWhere(introspectedTable);
        if (StringUtil.isNotEmpty(appendWhere)) {
            TextElement text = new TextElement(appendWhere);
            element.addElement(text);
        }
        return super.sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapSelectByExampleWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        String appendWhere = getAppendWhere(introspectedTable);
        if (StringUtil.isNotEmpty(appendWhere)) {
            TextElement text = new TextElement(appendWhere);
            element.addElement(text);
        }
        return super.sqlMapSelectByExampleWithoutBLOBsElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapCountByExampleElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        String appendWhere = getAppendWhere(introspectedTable);
        if (StringUtil.isNotEmpty(appendWhere)) {
            TextElement text = new TextElement(appendWhere);
            element.addElement(text);
        }
        return super.sqlMapCountByExampleElementGenerated(element, introspectedTable);
    }


}
