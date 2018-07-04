package com.xjcy.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtils
{
	public static String deserialize(BufferedReader reader) throws IOException
	{
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = reader.readLine()) != null)
		{
			buffer.append(line);
		}
		return buffer.toString();
	}

	public static String toXML(Map<String, Object> map)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		Object val;
		for (Object key : map.keySet())
		{
			val = map.get(key);
			if (val.toString().contains("![CDATA["))
			{
				sb.append("<");
				sb.append(key);
				sb.append(">");
				sb.append(val);
				sb.append("</");
				sb.append(key);
				sb.append(">");
			}
			else
			{
				sb.append("<");
				sb.append(key);
				sb.append("><![CDATA[");
				sb.append(val);
				sb.append("]]></");
				sb.append(key);
				sb.append(">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public static Map<String, Object> doXMLParse(String xml) {
		if (StringUtils.isEmpty(xml))
			return null;

		Map<String, Object> m = new HashMap<>();
		DocumentBuilderFactory factory = null;
		try {
			InputStream in = new ByteArrayInputStream(xml.getBytes("utf-8"));
			factory = DocumentBuilderFactory.newInstance();
			// 禁用外部实体，防止XXE
			factory.setExpandEntityReferences(false);
			Document doc = factory.newDocumentBuilder().parse(in);
			// 关闭流
			in.close();
			getMapByNode(doc.getDocumentElement(), m);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		} finally {
			factory = null;
		}
		return m;
	}
	
	private static void getMapByNode(Node node, Map<String, Object> m)
	{
		NodeList nodeList = node.getChildNodes();
		int nodeNum = nodeList.getLength();
		if (nodeNum > 0)
		{
			Node node2;
			for (int i = 0; i < nodeNum; i++)
			{
				node2 = nodeList.item(i);
				if (node2 instanceof Element)
				{
					String k = node2.getNodeName();
					String v = node2.getTextContent();
					m.put(k, v);
					getMapByNode(node2, m);
				}
			}
		}
	}

}
