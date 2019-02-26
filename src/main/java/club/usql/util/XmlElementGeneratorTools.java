package club.usql.util;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.GeneratedKey;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @author: yuanyuan.liu
 * @date: 2019/2/26 18:00
 */
public class XmlElementGeneratorTools {

    /**
     * This method should return an XmlElement for the select key used to
     * automatically generate keys.
     *
     * @param introspectedColumn the column related to the select key statement
     * @param generatedKey       the generated key for the current table
     * @return the selectKey element
     */
    public static Element getSelectKey(IntrospectedColumn introspectedColumn, GeneratedKey generatedKey) {
        return getSelectKey(introspectedColumn, generatedKey, null);
    }

    public static Element getSelectKey(IntrospectedColumn introspectedColumn, GeneratedKey generatedKey, String prefix) {
        String identityColumnType = introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName();

        XmlElement answer = new XmlElement("selectKey");
        answer.addAttribute(new Attribute("resultType", identityColumnType));
        answer.addAttribute(new Attribute("keyProperty", (prefix == null ? "" : prefix) + introspectedColumn.getJavaProperty()));
        answer.addAttribute(new Attribute("order", generatedKey.getMyBatis3Order()));

        answer.addElement(new TextElement(generatedKey.getRuntimeSqlStatement()));

        return answer;
    }

    public static Element getBaseColumnListElement(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId()));
        return answer;
    }

    public static Element getBlobColumnListElement(IntrospectedTable introspectedTable) {
        XmlElement answer = new XmlElement("include");
        answer.addAttribute(new Attribute("refid", introspectedTable.getBlobColumnListId()));
        return answer;
    }

    public static Element getExampleIncludeElement(IntrospectedTable introspectedTable) {
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "_parameter != null"));

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", introspectedTable.getExampleWhereClauseId()));
        ifElement.addElement(includeElement);

        return ifElement;
    }

    public static Element getUpdateByExampleIncludeElement(IntrospectedTable introspectedTable) {
        XmlElement ifElement = new XmlElement("if");
        ifElement.addAttribute(new Attribute("test", "_parameter != null"));

        XmlElement includeElement = new XmlElement("include");
        includeElement.addAttribute(new Attribute("refid", introspectedTable.getMyBatis3UpdateByExampleWhereClauseId()));
        ifElement.addElement(includeElement);

        return ifElement;
    }




    /**
     * 生成keys Ele
     *
     * @param columns
     * @return
     */
    public static List<Element> generateKeys(List<IntrospectedColumn> columns) {
        return generateKeys(columns, false);
    }

    /**
     * 生成keys Ele
     *
     * @param columns
     * @param bracket
     * @return
     */
    public static List<Element> generateKeys(List<IntrospectedColumn> columns, boolean bracket) {
        return generateCommColumns(columns, null, bracket, 1);
    }

    /**
     * 生成keys Selective Ele
     *
     * @param columns
     * @return
     */
    public static XmlElement generateKeysSelective(List<IntrospectedColumn> columns) {
        return generateKeysSelective(columns, null);
    }

    /**
     * 生成keys Selective Ele
     *
     * @param columns
     * @param prefix
     * @return
     */
    public static XmlElement generateKeysSelective(List<IntrospectedColumn> columns, String prefix) {
        return generateKeysSelective(columns, prefix, true);
    }

    /**
     * 生成keys Selective Ele
     *
     * @param columns
     * @param prefix
     * @param bracket
     * @return
     */
    public static XmlElement generateKeysSelective(List<IntrospectedColumn> columns, String prefix, boolean bracket) {
        return generateCommColumnsSelective(columns, prefix, bracket, 1);
    }

    /**
     * 生成values Ele
     *
     * @param columns
     * @return
     */
    public static List<Element> generateValues(List<IntrospectedColumn> columns) {
        return generateValues(columns, null);
    }

    /**
     * 生成values Ele
     *
     * @param columns
     * @param prefix
     * @return
     */
    public static List<Element> generateValues(List<IntrospectedColumn> columns, String prefix) {
        return generateValues(columns, prefix, true);
    }

    /**
     * 生成values Ele
     *
     * @param columns
     * @param prefix
     * @param bracket
     * @return
     */
    public static List<Element> generateValues(List<IntrospectedColumn> columns, String prefix, boolean bracket) {
        return generateCommColumns(columns, prefix, bracket, 2);
    }

    /**
     * 生成values Selective Ele
     *
     * @param columns
     * @return
     */
    public static XmlElement generateValuesSelective(List<IntrospectedColumn> columns) {
        return generateValuesSelective(columns, null);
    }

    /**
     * 生成values Selective Ele
     *
     * @param columns
     * @param prefix
     * @return
     */
    public static XmlElement generateValuesSelective(List<IntrospectedColumn> columns, String prefix) {
        return generateValuesSelective(columns, prefix, true);
    }

    /**
     * 生成values Selective Ele
     *
     * @param columns
     * @param prefix
     * @param bracket
     * @return
     */
    public static XmlElement generateValuesSelective(List<IntrospectedColumn> columns, String prefix, boolean bracket) {
        return generateCommColumnsSelective(columns, prefix, bracket, 2);
    }

    /**
     * 生成sets Ele
     *
     * @param columns
     * @return
     */
    public static List<Element> generateSets(List<IntrospectedColumn> columns) {
        return generateSets(columns, null);
    }

    /**
     * 生成sets Ele
     *
     * @param columns
     * @param prefix
     * @return
     */
    public static List<Element> generateSets(List<IntrospectedColumn> columns, String prefix) {
        return generateCommColumns(columns, prefix, false, 3);
    }

    /**
     * 生成sets Selective Ele
     *
     * @param columns
     * @return
     */
    public static XmlElement generateSetsSelective(List<IntrospectedColumn> columns) {
        return generateSetsSelective(columns, null);
    }

    /**
     * 生成sets Selective Ele
     *
     * @param columns
     * @param prefix
     * @return
     */
    public static XmlElement generateSetsSelective(List<IntrospectedColumn> columns, String prefix) {
        return generateCommColumnsSelective(columns, prefix, false, 3);
    }

    /**
     * 生成keys Ele (upsert)
     *
     * @param columns
     * @param prefix
     * @return
     */
    public static List<Element> generateUpsertKeys(List<IntrospectedColumn> columns, String prefix) {
        return generateCommColumns(columns, prefix, true, 1, true);
    }

    /**
     * 生成values Ele (upsert)
     *
     * @param columns
     * @param prefix
     * @param bracket
     * @return
     */
    public static List<Element> generateUpsertValues(List<IntrospectedColumn> columns, String prefix, boolean bracket) {
        return generateCommColumns(columns, prefix, bracket, 2, true);
    }

    /**
     * 生成sets Ele (upsert)
     *
     * @param columns
     * @param prefix
     * @return
     */
    public static List<Element> generateUpsertSets(List<IntrospectedColumn> columns, String prefix) {
        return generateCommColumns(columns, prefix, false, 3, true);
    }

    /**
     * 通用遍历columns
     *
     * @param columns
     * @param prefix
     * @param bracket
     * @param type    1:key,2:value,3:set
     * @return
     */
    private static List<Element> generateCommColumns(List<IntrospectedColumn> columns, String prefix, boolean bracket, int type) {
        return generateCommColumns(columns, prefix, bracket, type, false);
    }

    /**
     * 通用遍历columns
     *
     * @param columns
     * @param prefix
     * @param bracket
     * @param type    1:key,2:value,3:set
     * @param upsert
     * @return
     */
    private static List<Element> generateCommColumns(List<IntrospectedColumn> columns, String prefix, boolean bracket, int type, boolean upsert) {
        List<Element> list = new ArrayList<>();

        // 只有upsert插件才会传入 IdentityAndGeneratedAlwaysColumn
        if (upsert && hasIdentityAndGeneratedAlwaysColumns(columns)) {
            XmlElement trimEle = generateTrim(bracket);

            for (IntrospectedColumn introspectedColumn : columns) {
                if (introspectedColumn.isGeneratedAlways() || introspectedColumn.isIdentity()) {
                    generateSelectiveToTrimEleTo(trimEle, introspectedColumn, prefix, type);
                } else {
                    generateSelectiveCommColumnTo(trimEle, introspectedColumn, prefix, type);
                }
            }

            return Arrays.asList(trimEle);
        } else {
            StringBuilder sb = new StringBuilder(bracket ? "(" : "");
            Iterator<IntrospectedColumn> columnIterator = columns.iterator();
            while (columnIterator.hasNext()) {
                IntrospectedColumn introspectedColumn = columnIterator.next();

                switch (type) {
                    case 3:
                        sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
                        sb.append(" = ");
                        sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, prefix));
                        break;
                    case 2:
                        sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, prefix));
                        break;
                    case 1:
                        sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
                        break;
                }

                if (columnIterator.hasNext()) {
                    sb.append(", ");
                }

                // 保持和官方一致 80 进行换行
                if (type == 1 || type == 2) {
                    if (sb.length() > 80) {
                        list.add(new TextElement(sb.toString()));
                        sb.setLength(0);
                        OutputUtilities.xmlIndent(sb, 1);
                    }
                } else {
                    list.add(new TextElement(sb.toString()));
                    sb.setLength(0);
                }
            }
            if (sb.length() > 0 || bracket) {
                list.add(new TextElement(sb.append(bracket ? ")" : "").toString()));
            }

            return list;
        }
    }

    /**
     * 通用遍历columns
     *
     * @param columns
     * @param prefix
     * @param bracket
     * @param type    1:key,2:value,3:set
     * @return
     */
    private static XmlElement generateCommColumnsSelective(List<IntrospectedColumn> columns, String prefix, boolean bracket, int type) {
        XmlElement trimEle = generateTrim(bracket);
        for (IntrospectedColumn introspectedColumn : columns) {
            generateSelectiveToTrimEleTo(trimEle, introspectedColumn, prefix, type);
        }
        return trimEle;
    }

    /**
     * trim 节点
     *
     * @param bracket
     * @return
     */
    private static XmlElement generateTrim(boolean bracket) {
        XmlElement trimEle = new XmlElement("trim");
        if (bracket) {
            trimEle.addAttribute(new Attribute("prefix", "("));
            trimEle.addAttribute(new Attribute("suffix", ")"));
            trimEle.addAttribute(new Attribute("suffixOverrides", ","));
        } else {
            trimEle.addAttribute(new Attribute("suffixOverrides", ","));
        }
        return trimEle;
    }

    /**
     * 生成选择列到trim 节点
     *
     * @param trimEle
     * @param introspectedColumn
     * @param prefix
     * @param type               1:key,2:value,3:set
     */
    private static void generateSelectiveToTrimEleTo(XmlElement trimEle, IntrospectedColumn introspectedColumn, String prefix, int type) {
        if (type != 3 && (introspectedColumn.isSequenceColumn() || introspectedColumn.getFullyQualifiedJavaType().isPrimitive())) {
            // if it is a sequence column, it is not optional
            // This is required for MyBatis3 because MyBatis3 parses
            // and calculates the SQL before executing the selectKey

            // if it is primitive, we cannot do a null check
            generateSelectiveCommColumnTo(trimEle, introspectedColumn, prefix, type);
        } else {
            XmlElement eleIf = new XmlElement("if");
            eleIf.addAttribute(new Attribute("test", introspectedColumn.getJavaProperty(prefix) + " != null"));

            generateSelectiveCommColumnTo(eleIf, introspectedColumn, prefix, type);

            trimEle.addElement(eleIf);
        }
    }

    /**
     * 生成
     *
     * @param element
     * @param introspectedColumn
     * @param prefix
     * @param type               1:key,2:value,3:set
     */
    private static void generateSelectiveCommColumnTo(XmlElement element, IntrospectedColumn introspectedColumn, String prefix, int type) {
        switch (type) {
            case 3:
                element.addElement(new TextElement(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn) + " = " + MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, prefix) + ","));
                break;
            case 2:
                element.addElement(new TextElement(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, prefix) + ","));
                break;
            case 1:
                element.addElement(new TextElement(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn) + ","));
                break;
        }
    }

    /**
     * 生成 xxxByPrimaryKey 的where 语句
     *
     * @param element
     * @param primaryKeyColumns
     * @return
     */
    public static void generateWhereByPrimaryKeyTo(XmlElement element, List<IntrospectedColumn> primaryKeyColumns) {
        generateWhereByPrimaryKeyTo(element, primaryKeyColumns, null);
    }

    /**
     * 生成 xxxByPrimaryKey 的where 语句
     *
     * @param element
     * @param primaryKeyColumns
     * @param prefix
     * @return
     */
    public static void generateWhereByPrimaryKeyTo(XmlElement element, List<IntrospectedColumn> primaryKeyColumns, String prefix) {
        StringBuilder sb = new StringBuilder();
        boolean and = false;
        for (IntrospectedColumn introspectedColumn : primaryKeyColumns) {
            sb.setLength(0);
            if (and) {
                sb.append("  and ");
            } else {
                sb.append("where ");
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, prefix));
            element.addElement(new TextElement(sb.toString()));
        }
    }

    /**
     * 是否存在自增或者生成的column
     *
     * @param columns
     * @return
     */
    private static boolean hasIdentityAndGeneratedAlwaysColumns(List<IntrospectedColumn> columns) {
        for (IntrospectedColumn ic : columns) {
            if (ic.isGeneratedAlways() || ic.isIdentity()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 生成resultMap的result 节点
     *
     * @param name
     * @param introspectedColumn
     * @return
     */
    public static XmlElement generateResultMapResultElement(String name, IntrospectedColumn introspectedColumn) {
        XmlElement resultElement = new XmlElement(name);

        resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
        resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));
        resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName()));

        if (stringHasValue(introspectedColumn.getTypeHandler())) {
            resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn.getTypeHandler()));
        }
        return resultElement;
    }

    /**
     * 生成逻辑删除列的删除值
     *
     * @param logicalDeleteColumn
     * @param value
     * @return
     */
    public static String generateLogicalDeleteColumnValue(IntrospectedColumn logicalDeleteColumn, String value) {
        StringBuilder sb = new StringBuilder();
        // 判断字段类型
        if (value == null || "NULL".equalsIgnoreCase(value)) {
            sb.append("NULL");
        } else if (logicalDeleteColumn.isStringColumn()) {
            sb.append("'");
            sb.append(value);
            sb.append("'");
        } else if (logicalDeleteColumn.getFullyQualifiedJavaType().getFullyQualifiedName().equals(Long.class.getName())) {
            sb.append(value.replaceAll("L|l", ""));
        } else if (logicalDeleteColumn.getFullyQualifiedJavaType().getFullyQualifiedName().equals(Float.class.getName())) {
            sb.append(value.replaceAll("F|f", ""));
        } else {
            sb.append(value);
        }
        return sb.toString();
    }
}