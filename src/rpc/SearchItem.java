package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

/*		response.addHeader("Access-Control-Allow-Origin", "*");
		//Create a PrintWriter from response such that we can add data to response.
		PrintWriter out = response.getWriter();
		if (request.getParameter("username") != null) {
			//Get the username sent from the client
			String username = request.getParameter("username");
			out.print("Hello," + username);
		}
		// Send the response back to the client
		out.flush();
		out.close();*/
		
/*		response.setContentType("text/html");
		response.addHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		out.println("<h1>This is an HTML page!</h1>");
		out.println("</body></html>");
		
		out.flush();
		out.close();*/
		
/*		String username = "";
		if (request.getParameter("username") != null) {
			username = request.getParameter("username");
		}
		
		JSONObject  obj = new JSONObject();
		try {
			obj.put("username", username);
		} catch(JSONException e) {
			e.printStackTrace();
		}
				
		RpcHelper.writeJsonType(response, obj);*/
		
/*		JSONArray array = new JSONArray();
		try {
			array.put(new JSONObject().put("username", "1234"));
			array.put(new JSONObject().put("username", "abcd"));
		} catch(JSONException e) {
			e.printStackTrace();
		}
		RpcHelper.writeJsonType(response, array);*/
		
		// allow access only if session exists		
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		String userId = session.getAttribute("user_id").toString();
		
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		// keyword can be empty or null.
		String keyword = request.getParameter("keyword");
		//TicketMasterAPI tmAPI = new TicketMasterAPI();
		//st<Item> items = tmAPI.search(lat, lon, keyword);
		
		//String userId = request.getParameter("user_id");
		
		DBConnection connenction = DBConnectionFactory.getDBConnection();
		List<Item> items = connenction.searchItems(lat, lon, keyword);		
		List<JSONObject> list = new ArrayList<>();
		
		Set<String> favorite = connenction.getFavoriteItemIds(userId);
		try {
			for (Item item : items) {
				// Add a thin version of item object
				JSONObject obj = item.toJSONObject();
				
				// Check if this is a favorite one.
				// This field is required by front end to correctly display favorite items.
				obj.put("favorite", favorite.contains(item.getItemId()));
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray array = new JSONArray(list);
		RpcHelper.writeJsonType(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
