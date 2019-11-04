import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class JDomParse {

	public JDomParse() {

		String xmlpath = "library.xml";
		// try {
		// SAXBuilder builder = new SAXBuilder(false);
		//
		// Document doc = builder.build(xmlpath);
		//
		// Element books = doc.getRootElement();

		// List booklist = books.getChildren("book");

		// for (Iterator iter = booklist.iterator(); iter.hasNext();) {
		//
		// Element book = (Element) iter.next();
		//
		// String email = book.getAttributeValue("email");
		//
		// System.out.println(email);
		//
		// String name = book.getChildTextTrim("name");
		//
		// System.out.println(name);

		// book.getChild("name").setText("alterrjzjh");
		//
		// XMLOutputter outputter = new XMLOutputter();
		//
		// outputter.output(doc, new FileOutputStream(xmlpath));
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	@SuppressWarnings("unchecked")
	public static void getElementValueByFathersKey(String param, int operType) {
		String xmlpath = "library.xml";
		SAXBuilder builder = new SAXBuilder(false);
		try {
			Document doc = builder.build(xmlpath);
			Element jsps = doc.getRootElement();
			if (operType == 1) {
				System.out.println(jsps.getChildText(param));
			} else if (operType == 2) {
				List<Element> jspList = jsps.getChildren("jsp");
				for (int i = 0; i < jspList.size(); i++) {
					String idValue = jspList.get(i).getAttributeValue("id");
					if (param.equals(idValue)) {
						System.out.println(jspList.get(i).getChildTextTrim(
								"title"));
						System.out.println(jspList.get(i).getChildTextTrim(
								"tipcontent"));
					}
				}
			} else if (operType == 3) {
				List<Element> codePointList = jsps.getChildren("codepoint");
				for (int i = 0; i < codePointList.size(); i++) {
					String idValue = codePointList.get(i).getAttributeValue("id");
					if(param.equals(idValue)){
						List<Element> selKeyList = codePointList.get(i).getChildren();
						for(int j=0;j<selKeyList.size();j++){
							System.out.println(selKeyList.get(j).getAttributeValue("name"));
							System.out.println(selKeyList.get(j).getAttributeValue("showtype"));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		// new JDomParse();
		getElementValueByFathersKey("list",3);
	}
}
