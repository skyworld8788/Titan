package offline;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;


import db.mongodb.MongoDBUtil;

public class Purify {
	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient();
		MongoDatabase db = mongoClient.getDatabase(MongoDBUtil.DB_NAME);
		// Switch to your own path
		String fileName = "D:\\3_Code\\Eclipse_Java\\LaiProject\\tomcat_log.txt";

		try {
			db.getCollection("logs").drop();

			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				// Sample input:
				// 73.223.210.212 - - [19/Aug/2017:22:00:24 +0000] "GET
				// /Titan/history?user_id=1111 HTTP/1.1" 200 11410
				List<String> values = Arrays.asList(line.split(" "));//按空格拆分

				String ip = values.size() > 0 ? values.get(0) : null;
				String timestamp = values.size() > 3 ? values.get(3) : null;
				String method = values.size() > 5 ? values.get(5) : null;
				String url = values.size() > 6 ? values.get(6) : null;
				String status = values.size() > 8 ? values.get(8) : null;
				//正则表达式的匹配
				Pattern pattern = Pattern.compile("\\[(.+?):(.+)");//[19/Aug/2017:22:00:24 日期和时间两部分
				Matcher matcher = pattern.matcher(timestamp);
				matcher.find();

				db.getCollection("logs")
						.insertOne(new Document().append("ip", ip).append("date", matcher.group(1))
								.append("time", matcher.group(2)).append("method", method.substring(1))
								.append("url", url).append("status", status));
			}
			System.out.println("Import Done!");
			bufferedReader.close();
			mongoClient.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
