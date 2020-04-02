package com.ofcoder.mybatis.generator.plugin;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.Context;

import java.util.Iterator;
import java.util.List;

/**
 * @author: yuanyuan.liu
 * @date: 2019/12/23 10:49
 */
public class SelectivePlugin extends PluginAdapter {
    private CommentGenerator commentGenerator;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate(List<String> warnings) {

        return true;
    }

    @Override
    public void setContext(Context context) {
        super.setContext(context);
        commentGenerator = getContext().getCommentGenerator();
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return true;
    }
}
