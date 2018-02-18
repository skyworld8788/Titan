package rpc;

import java.io.BufferedReader;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public class RpcHelper {
	
	// Writes a JSONArray/JSONObject<T> to http response.
	public static<T> void writeJsonType(HttpServletResponse response, T typeObj) {
		try {
			
			response.setContentType("application/json");
			response.addHeader("Access-Control-Allow-Origin", "*");
			PrintWriter out = response.getWriter();
			
			out.print(typeObj);
			out.flush();
			out.close();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Parses a JSONObject from http request.
	public static JSONObject readJsonObject(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			reader.close();
			return new JSONObject(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
