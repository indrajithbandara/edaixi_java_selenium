package com.ibm.docs.test.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import com.ibm.icu.text.Transliterator;

/**
 * GVT test data characters, this class is a instance of gvtdata.xml.
 * 
 * @author xinyutan@cn.ibm.com
 * 
 */
public class GData {
	private static GData instance;
	private Document doc;

	private GData() {
		SAXReader reader = new SAXReader();
		try {
			doc = reader.read(new File("./data/gvtdata.xml"));

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the instance of gvt/gvtdata.xml
	 */
	public static GData getInstance() {
		if (instance == null) {
			instance = new GData();
		}
		return instance;
	}

	/**
	 * @return a general test data List in GVT Guide Appendix-A.
	 */
	public List<String> getGeneralDataList() {
		List<String> list = new ArrayList<String>();
		String xPath = "//testdata[@id<29]/Hex|//testdata[@id=40 or @id=44 or @id=45]/Hex";
		List<? extends Node> nodeList = doc.selectNodes(xPath);
		Transliterator transliterator = Transliterator.getInstance("Hex-Any");
		for (Node n : nodeList) {
			list.add(transliterator.transform(n.getText().trim()));
		}
		return list;
	}

	/**
	 * @param id
	 *            the test data id in each GVT Guide test area.
	 * @return a test data assigned by id
	 */
	public String getDataById(int id) {
		String data;
		String xPath = "//testdata[@id=" + id + "]/Hex";
		List<? extends Node> nodeList = doc.selectNodes(xPath);
		Transliterator transliterator = Transliterator.getInstance("Hex-Any");
		data = transliterator.transform(nodeList.get(0).getText().trim());
		return data;
	}

}
