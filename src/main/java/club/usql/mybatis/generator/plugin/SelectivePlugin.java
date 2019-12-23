package club.usql.mybatis.generator.plugin;

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
    private CommentGenerator commentGenerator ;

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
        XmlElement parentElement = document.getRootElement();
        this.updateDocumentInsertSelective(parentElement);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private void updateDocumentInsertSelective(XmlElement parentElement) {
        XmlElement oldElement = null;
        XmlElement newElement = null;
        Iterator iterator = parentElement.getElements().iterator();

        while(true) {
            while(true) {
                XmlElement xmlElement;
                do {
                    if (!iterator.hasNext()) {
                        parentElement.getElements().remove(oldElement);
                        parentElement.getElements().add(newElement);
                        return;
                    }

                    Element element = (Element)iterator.next();
                    xmlElement = (XmlElement)element;
                } while(!"insert".equals(xmlElement.getName()));

                Iterator iteratorAttr = xmlElement.getAttributes().iterator();

                while(iteratorAttr.hasNext()) {
                    Attribute attribute = (Attribute)iteratorAttr.next();
                    if ("insertSelective".equals(attribute.getValue())) {
                        oldElement = xmlElement;
                        newElement = xmlElement;
                        xmlElement.addAttribute(new Attribute("useGeneratedKeys", "true"));
                        xmlElement.addAttribute(new Attribute("keyProperty", "id"));
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> elements = element.getElements();
        XmlElement setItem = null;
        int modifierItemIndex = -1;
        int gmtModifiedItemIndex = -1;
        Iterator iterator = elements.iterator();

        while(true) {
            Element e;
            do {
                if (!iterator.hasNext()) {
                    if (modifierItemIndex != -1 && setItem != null) {
                        this.addXmlElementModifier(setItem, modifierItemIndex);
                    }

                    if (gmtModifiedItemIndex != -1 && setItem != null) {
                        this.addGmtModifiedXmlElement(setItem, gmtModifiedItemIndex);
                    }

                    return super.sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable);
                }

                e = (Element)iterator.next();
            } while(!(e instanceof XmlElement));

            setItem = (XmlElement)e;

            for(int i = 0; i < setItem.getElements().size(); ++i) {
                XmlElement xmlElement = (XmlElement)setItem.getElements().get(i);
                Iterator iteratorAttr = xmlElement.getAttributes().iterator();

                while(iteratorAttr.hasNext()) {
                    Attribute att = (Attribute)iteratorAttr.next();
                    if ("modifier != null".equals(att.getValue())) {
                        modifierItemIndex = i;
                        break;
                    }

                    if ("gmtModified != null".equals(att.getValue())) {
                        gmtModifiedItemIndex = i;
                        break;
                    }
                }
            }
        }
    }

    private void addGmtModifiedXmlElement(XmlElement setItem, int gmtModifiedItemIndex) {
        XmlElement defaultGmtModified = new XmlElement("if");
        defaultGmtModified.addAttribute(new Attribute("test", "gmtModified == null"));
        defaultGmtModified.addElement(new TextElement("GMT_MODIFIED = current_timestamp,"));
        setItem.getElements().add(gmtModifiedItemIndex + 1, defaultGmtModified);
    }

    private void addXmlElementModifier(XmlElement setItem, int modifierItemIndex) {
        XmlElement defaultmodifier = new XmlElement("if");
        defaultmodifier.addAttribute(new Attribute("test", "modifier == null"));
        defaultmodifier.addElement(new TextElement("MODIFIER = 'system',"));
        setItem.getElements().add(modifierItemIndex + 1, defaultmodifier);
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        List<Element> elements = element.getElements();
        XmlElement fieldItem = null;
        XmlElement valueItem = null;
        Iterator iterator = elements.iterator();

        while(true) {
            while(true) {
                XmlElement xmlElement;
                do {
                    Element e;
                    do {
                        if (!iterator.hasNext()) {
                            XmlElement defaultGmtCreate;
                            XmlElement defaultGmtModified;
                            XmlElement defaultCreator;
                            XmlElement defaultIsDeleted;
                            if (fieldItem != null) {
                                defaultGmtCreate = new XmlElement("if");
                                defaultGmtCreate.addAttribute(new Attribute("test", "gmtCreate == null"));
                                defaultGmtCreate.addElement(new TextElement("GMT_CREATE,"));
                                fieldItem.addElement(1, defaultGmtCreate);
                                defaultGmtModified = new XmlElement("if");
                                defaultGmtModified.addAttribute(new Attribute("test", "gmtModified == null"));
                                defaultGmtModified.addElement(new TextElement("GMT_MODIFIED,"));
                                fieldItem.addElement(1, defaultGmtModified);
                                xmlElement = new XmlElement("if");
                                xmlElement.addAttribute(new Attribute("test", "modifier == null"));
                                xmlElement.addElement(new TextElement("MODIFIER,"));
                                fieldItem.addElement(1, xmlElement);
                                defaultCreator = new XmlElement("if");
                                defaultCreator.addAttribute(new Attribute("test", "creator == null"));
                                defaultCreator.addElement(new TextElement("CREATOR,"));
                                fieldItem.addElement(1, defaultCreator);
                                defaultIsDeleted = new XmlElement("if");
                                defaultIsDeleted.addAttribute(new Attribute("test", "isDeleted == null"));
                                defaultIsDeleted.addElement(new TextElement("IS_DELETED,"));
                                fieldItem.addElement(1, defaultIsDeleted);
                            }

                            if (valueItem != null) {
                                defaultGmtCreate = new XmlElement("if");
                                defaultGmtCreate.addAttribute(new Attribute("test", "gmtCreate == null"));
                                defaultGmtCreate.addElement(new TextElement("current_timestamp,"));
                                valueItem.addElement(1, defaultGmtCreate);
                                defaultGmtModified = new XmlElement("if");
                                defaultGmtModified.addAttribute(new Attribute("test", "gmtModified == null"));
                                defaultGmtModified.addElement(new TextElement("current_timestamp,"));
                                valueItem.addElement(1, defaultGmtModified);
                                xmlElement = new XmlElement("if");
                                xmlElement.addAttribute(new Attribute("test", "modifier == null"));
                                xmlElement.addElement(new TextElement("'system',"));
                                valueItem.addElement(1, xmlElement);
                                defaultCreator = new XmlElement("if");
                                defaultCreator.addAttribute(new Attribute("test", "creator == null"));
                                defaultCreator.addElement(new TextElement("'system',"));
                                valueItem.addElement(1, defaultCreator);
                                defaultIsDeleted = new XmlElement("if");
                                defaultIsDeleted.addAttribute(new Attribute("test", "isDeleted == null"));
                                defaultIsDeleted.addElement(new TextElement("'n',"));
                                valueItem.addElement(1, defaultIsDeleted);
                            }

                            return super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
                        }
                        e = (Element)iterator.next();
                    } while(!(e instanceof XmlElement));
                    xmlElement = (XmlElement)e;
                } while(!"trim".equals(xmlElement.getName()));

                Iterator iteratorAttr = xmlElement.getAttributes().iterator();

                while(iteratorAttr.hasNext()) {
                    Attribute arr = (Attribute)iteratorAttr.next();
                    if ("(".equals(arr.getValue())) {
                        fieldItem = xmlElement;
                        break;
                    }
                    if ("values (".equals(arr.getValue())) {
                        valueItem = xmlElement;
                        break;
                    }
                }
            }
        }
    }
}
