import org.hanmeis.common.html.Jsoups;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

public class SdoListSpider {
	// 用于保存医药信息的列表
	static List<Sdo> sdos = new LinkedList<Sdo>();
	private static Integer id = 2000;

	public static void main(String[] args) throws Exception {
		// 解析过程
		for (int i = 2; i < 330; i++) {
			Thread.sleep(3000);
			System.out.println("第" + i + "次");
			// URL index = new URL("https://db.yaozh.com/atc");
			URL index = new URL("https://db.yaozh.com/atc?p=" + i + "&pageSize=20");
			parsePage(index);
		}

		// 将信息存档
		FileWriter writer = new FileWriter("sdo.txt");
		for (Sdo sdo : sdos) {
			writer.write(sdo.toString());
		}
		writer.close();
	}

	static void parsePage(URL url) throws Exception {

		try {
			// 使用Jsoup的解析方法进行填装Dom
			Document doc = Jsoups.parse(url, 5000);

			// System.out.println(doc);
			// 列表集合
			Elements atcList = doc.select("tbody tr");
			for (Element element : atcList) {
				Sdo sdo = new Sdo();

				sdo.setId(id);
				// 获取atc编码
				Element codeElement = element.select("th a").first();
				if (codeElement != null) {
					sdo.setSdoCode(codeElement.html());
				}

				// 获取atc中文名
				Element nameElement = element.select("tr a").last();
				if (nameElement != null) {
					sdo.setSdoName(nameElement.html());
				}

				sdo.setType("药品编码");
				System.out.println(sdo);
				sdos.add(sdo);
				id++;
			}

		} catch (IOException e) {
			System.out.println(url);
			e.printStackTrace();
		}
	}

	/**
	 * sdo数据对象
	 */
	static class Sdo {
		private int id;
		private String sdoCode;
		private String sdoName;
		private String type;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getSdoCode() {
			return sdoCode;
		}

		public void setSdoCode(String sdoCode) {
			this.sdoCode = sdoCode;
		}

		public String getSdoName() {
			return sdoName;
		}

		public void setSdoName(String sdoName) {
			this.sdoName = sdoName;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		@Override
		public String toString() {
			// return "Sdo" + "," + sdoCode + "," + sdoName + "," + type;
			return "insert into Sdo(sdoCode,sdoName,type) values ('" + sdoCode + "','" + sdoName + "','" + type + ");";
		}

	}
}