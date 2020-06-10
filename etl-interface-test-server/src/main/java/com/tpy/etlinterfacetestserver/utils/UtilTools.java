package com.tpy.etlinterfacetestserver.utils;

import com.alibaba.fastjson.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

public class UtilTools {

    public static List<Map<String,Object>> parseXML(String xmlStr){
        List<Map<String,Object>> list=null;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try{
            DocumentBuilder builder = factory.newDocumentBuilder();
            StringReader sr = new StringReader(xmlStr);
            InputSource is = new InputSource(sr);
            Document doc = builder.parse(is);
            //
            Element rootElement = doc.getDocumentElement();
            //
            NodeList users = rootElement.getElementsByTagName("user");
            //
            list=new ArrayList<Map<String, Object>>();
            for(int i=0;i<users.getLength();i++){
                Node node = users.item(i);
                NodeList childs = node.getChildNodes();
                Map<String,Object> map=new HashMap<String, Object>();
                for(int j=0;j<childs.getLength();j++){
                    Node childNode = childs.item(j);
                    String nodeName=childNode.getNodeName();
                    String value=childNode.getTextContent();
                    map.put(nodeName,value);
                }
                list.add(map);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static Map<String,String> strToMap(String json,Map<String,String> paramMap){
        JSONObject jsonObject = JSONObject.parseObject(json);
        Set<String> strings = jsonObject.keySet();
        for(String str : strings){
            paramMap.put(str,(String) jsonObject.get(str));
        }
        return paramMap;
    }

    public static void writeFile(String fileName, String conent){

        File file = new File("C:\\log");
        if (!file.exists()) {
            file.mkdirs();
        }

        String filePath = "C:\\log\\"+fileName;

        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath, true)));
            out.write(conent+"\r\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
