package utils;

import com.intellij.openapi.project.Project;
import entity.ScriptEntity;
import manager.Env;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ConfigParser {

//    <? xml version = "1.0"encoding="UTF-8"?>
//    <Config>
//        <Env>
//            <Python>/usr/local/bin/python3</Python>
//        </Env>
//        <Scripts>
//            <Script>
//                <name>脚本名称</name>
//                <description>脚本描述</description>
//                <code>xxxx</code>
//                <result>true</result>
//            </Script>
//        </Scripts>
//    </Config>

    private static final String CONFIG_FILE_NAME = "Scripts.xml";
    private static final String NODE_ENV = "Env";
    private static final String NODE_SCRIPT = "Script";
    private static final String NODE_NAME = "Name";
    private static final String NODE_DESCRIPTION = "Description";
    private static final String NODE_CODE = "Code";
    private static final String NODE_RESULT = "Result";

    public static ArrayList<ScriptEntity> loadConfigFile(Project project) {
        String rootFilePath = project.getBaseDir().getPath();
        String configFilePath = rootFilePath + File.separator + CONFIG_FILE_NAME;
        if (TextUtil.isEmpty(configFilePath)) {
            return null;
        }
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            return null;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(configFile);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileInputStream);
            parseEnv(document);
            return parseScriptNode(document);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            CloseableUtils.close(fileInputStream);
        }
    }

    private static void parseEnv(@NotNull Document document) {
        NodeList envNodeList = document.getElementsByTagName(NODE_ENV);
        if (envNodeList == null || envNodeList.getLength() < 1) {
            return;
        }
        Node envNode = envNodeList.item(0);
        if (envNode == null) {
            return;
        }
        //存在环境配置
        NodeList envChildNodeList = envNode.getChildNodes();
        if (envChildNodeList == null || envChildNodeList.getLength() < 1) {
            return;
        }
        for (int i = 0, j = envChildNodeList.getLength(); i < j; i++) {
            Node envChild = envChildNodeList.item(i);
            if (envChild.getNodeType() != Node.ELEMENT_NODE) continue;
            String env = envChild.getNodeName();
            String realEnv = envChild.getFirstChild().getNodeValue();
            Env.addEnv(env, realEnv);
            System.out.println("env = " + env + " realEnv = " + realEnv);
        }
    }


    private static ArrayList<ScriptEntity> parseScriptNode(@NotNull Document document) {
        NodeList scriptNodeList = document.getElementsByTagName(NODE_SCRIPT);
        if (scriptNodeList == null || scriptNodeList.getLength() < 1) {
            return null;
        }
        ScriptEntity scriptEntity;
        ArrayList<ScriptEntity> result = new ArrayList<>();
        for (int i = 0; i < scriptNodeList.getLength(); i++) {
            Node scriptNode = scriptNodeList.item(i);
            NodeList childNodes = scriptNode.getChildNodes();
            if (childNodes == null || childNodes.getLength() < 1) continue;
            scriptEntity = new ScriptEntity();
            for (int j = 0; j < childNodes.getLength(); j++) {
                Node childNote = childNodes.item(j);
                if (childNote.getNodeType() != Node.ELEMENT_NODE) continue;
                String nodeName = childNote.getNodeName();
                String nodeValue = childNote.getFirstChild().getNodeValue();
                switch (nodeName) {
                    case NODE_NAME:
                        scriptEntity.name = nodeValue;
                        break;
                    case NODE_DESCRIPTION:
                        scriptEntity.description = nodeValue;
                        break;
                    case NODE_CODE:
                        scriptEntity.code = nodeValue;
                        break;
                    case NODE_RESULT:
                        scriptEntity.result = Boolean.parseBoolean(nodeValue);
                        break;
                }
            }
            result.add(scriptEntity);
        }
        return result;
    }
}
