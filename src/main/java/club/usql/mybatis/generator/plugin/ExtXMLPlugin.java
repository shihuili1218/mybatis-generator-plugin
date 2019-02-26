package club.usql.mybatis.generator.plugin;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.ShellRunner;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author: yuanyuan.liu
 * @date: 2019/2/26 14:36
 */
public class ExtXMLPlugin extends PluginAdapter {
    private static String XMLFILE_POSTFIX = "Ext";
    private static String JAVAFILE_POTFIX = "Ext";
    private static String SQLMAP_COMMON_POTFIX = "and is_deleted = 'n'";
    private static String ANNOTATION_RESOURCE = "org.apache.ibatis.annotations.Mapper";

    public ExtXMLPlugin() {
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return super.modelExampleClassGenerated(topLevelClass, introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement parentElement = document.getRootElement();
        updateDocumentNameSpace(introspectedTable, parentElement);
        updateDocumentInsertSelective(parentElement);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    private void updateDocumentInsertSelective(XmlElement parentElement) {
        XmlElement oldElement = null;
        XmlElement newElement = null;
        Iterator iterator = parentElement.getElements().iterator();

        while (true) {
            while (true) {
                XmlElement xmlElement;
                do {
                    if (!iterator.hasNext()) {
                        parentElement.getElements().remove(oldElement);
                        parentElement.getElements().add(newElement);
                        return;
                    }

                    Element element = (Element) iterator.next();
                    xmlElement = (XmlElement) element;
                } while (!"insert".equals(xmlElement.getName()));

                Iterator iteratorAttr = xmlElement.getAttributes().iterator();

                while (iteratorAttr.hasNext()) {
                    Attribute attribute = (Attribute) iteratorAttr.next();
                    if ("insertSelective".equals(attribute.getValue()) || "insert".equals(attribute.getValue())) {
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

    private void updateDocumentNameSpace(IntrospectedTable introspectedTable, XmlElement parentElement) {
        Attribute namespaceAttribute = null;
        Iterator iterator = parentElement.getAttributes().iterator();

        while (iterator.hasNext()) {
            Attribute attribute = (Attribute) iterator.next();
            if ("namespace".equals(attribute.getName())) {
                namespaceAttribute = attribute;
            }
        }

        parentElement.getAttributes().remove(namespaceAttribute);
        parentElement.getAttributes().add(new Attribute("namespace", introspectedTable.getMyBatis3JavaMapperType() + JAVAFILE_POTFIX));
    }

    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return super.sqlMapDeleteByPrimaryKeyElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return super.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    @Override
    public List<GeneratedXmlFile> contextGenerateAdditionalXmlFiles(IntrospectedTable introspectedTable) {
        String[] splitFile = introspectedTable.getMyBatis3XmlMapperFileName().split("\\.");
        String fileNameExt = null;
        if (splitFile[0] != null) {
            fileNameExt = splitFile[0] + XMLFILE_POSTFIX + ".xml";
        }

        if (isExistExtFile(context.getSqlMapGeneratorConfiguration().getTargetProject(), introspectedTable.getMyBatis3XmlMapperPackage(), fileNameExt)) {
            return super.contextGenerateAdditionalXmlFiles(introspectedTable);
        } else {
            Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
            XmlElement root = new XmlElement("mapper");
            document.setRootElement(root);
            String namespace = introspectedTable.getMyBatis3SqlMapNamespace() + XMLFILE_POSTFIX;
            root.addAttribute(new Attribute("namespace", namespace));
            GeneratedXmlFile gxf = new GeneratedXmlFile(document, fileNameExt, introspectedTable.getMyBatis3XmlMapperPackage(), context.getSqlMapGeneratorConfiguration().getTargetProject(), false, context.getXmlFormatter());
            List<GeneratedXmlFile> answer = new ArrayList(1);
            answer.add(gxf);
            return answer;
        }
    }

    @Override
    public List<GeneratedJavaFile> contextGenerateAdditionalJavaFiles(IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType() + JAVAFILE_POTFIX);
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        context.getCommentGenerator().addJavaFileComment(interfaze);
        FullyQualifiedJavaType baseInterfaze = new FullyQualifiedJavaType(introspectedTable.getMyBatis3JavaMapperType());
        interfaze.addSuperInterface(baseInterfaze);
        FullyQualifiedJavaType annotation = new FullyQualifiedJavaType(ANNOTATION_RESOURCE);
        interfaze.addAnnotation("@Mapper");
        interfaze.addImportedType(annotation);
        GeneratedJavaFile generatedJavaFile = new GeneratedJavaFile(interfaze, context.getJavaModelGeneratorConfiguration().getTargetProject(), context.getProperty("javaFileEncoding"), context.getJavaFormatter());
        if (isExistExtFile(generatedJavaFile.getTargetProject(), generatedJavaFile.getTargetPackage(), generatedJavaFile.getFileName())) {
            return super.contextGenerateAdditionalJavaFiles(introspectedTable);
        } else {
            List<GeneratedJavaFile> generatedJavaFiles = new ArrayList(1);
            generatedJavaFile.getFileName();
            generatedJavaFiles.add(generatedJavaFile);
            return generatedJavaFiles;
        }
    }

    private boolean isExistExtFile(String targetProject, String targetPackage, String fileName) {
        File project = new File(targetProject);
        if (!project.isDirectory()) {
            return true;
        } else {
            StringBuilder sb = new StringBuilder();
            StringTokenizer st = new StringTokenizer(targetPackage, ".");

            while (st.hasMoreTokens()) {
                sb.append(st.nextToken());
                sb.append(File.separatorChar);
            }

            File directory = new File(project, sb.toString());
            if (!directory.isDirectory()) {
                boolean rc = directory.mkdirs();
                if (!rc) {
                    return true;
                }
            }

            File testFile = new File(directory, fileName);
            return testFile.exists();
        }
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    public static void main(String[] args) {
        String config = ExtXMLPlugin.class.getClassLoader().getResource("generatorConfig.xml").getFile();
        String[] arg = new String[]{"-configfile", config};
        ShellRunner.main(arg);
    }
}
