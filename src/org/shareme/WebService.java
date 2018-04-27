package org.shareme;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.microsoft.sqlserver.jdbc.SQLServerException;

@Path("/")
public class WebService {
	//public static void main(String[] args) {}
	
	//註冊
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/register")
	public Response register (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);
			
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			String pwd = iptObj.getString(Constant.KEY_PASSWORD);
			String name = iptObj.getString(Constant.KEY_NAME);
			String dep = iptObj.getString(Constant.KEY_DEPARTMENT);
			String gender = iptObj.getString(Constant.KEY_GENDER);
			String email = iptObj.getString(Constant.KEY_EMAIL);
			executeUpdate(
					String.format("exec 註冊 '%s', '%s', '%s', '%s', '%s', '%s'", userId, pwd, name, dep, gender, email
			));
			
			optObj.put(Constant.KEY_STATUS, true);
		}catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}

	//登入
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/login")
	public Response login (String reqJSONString) throws JSONException {
		JSONObject optObj = new JSONObject();
		String userId = "", pwd = "";
		try {
			JSONObject iptObj = new JSONObject(reqJSONString);
			userId = iptObj.getString(Constant.KEY_USER_ID);
			pwd = iptObj.getString(Constant.KEY_PASSWORD);
			
			optObj.put(Constant.KEY_PROFILE, new JSONObject(getQueryJSONString(
					String.format("exec 登入 '%s', '%s'", userId, pwd), false
			)));
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException|SQLException e) {
			optObj.put(Constant.KEY_STATUS, false);
		}
		return getJSONResponse(optObj.toString());
	}

	//列示商品
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/list_products")
	public Response listProducts (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			JSONObject iptObj = new JSONObject(reqJSONString);
			String type = iptObj.getString(Constant.KEY_TYPE);
			String keyword = iptObj.getString(Constant.KEY_KEYWORD);
			
			JSONArray ary = new JSONArray(getQueryJSONString(
					String.format("exec 列示商品 '%s', '%s'", type, keyword)
					, true));
			
			if (ary.length() != 0) {
				optObj.put(Constant.KEY_PRODUCTS, ary);
				optObj.put(Constant.KEY_STATUS, true);
			}else {
				optObj.put(Constant.KEY_STATUS, false);				
			}
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//刊登商品
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/post_product")
	public Response postProduct (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);
			
			JSONObject iptObj = new JSONObject(reqJSONString);
			String title = iptObj.getString(Constant.KEY_TITLE);
			String price = iptObj.getString(Constant.KEY_PRICE);
			String type = iptObj.getString(Constant.KEY_TYPE);
			String condition = iptObj.getString(Constant.KEY_CONDITION);
			String note = iptObj.getString(Constant.KEY_NOTE);
			String ps = iptObj.getString(Constant.KEY_PS);
			String seller = iptObj.getString(Constant.KEY_SELLER);
			String photo1 = iptObj.getString(Constant.KEY_PHOTO1);
			String photo2 = iptObj.getString(Constant.KEY_PHOTO2);
			String photo3 = iptObj.getString(Constant.KEY_PHOTO3);
			String photo4 = iptObj.getString(Constant.KEY_PHOTO4);
			String photo5 = iptObj.getString(Constant.KEY_PHOTO5);
						
			optObj.put(Constant.KEY_PRODUCT, new JSONObject(getQueryJSONString(
					String.format("exec 刊登商品 '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'", 
							title, price, type, condition, note, ps, seller, photo1, photo2, photo3, photo4, photo5)
					, false)));
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//顯示商品詳情
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/product_detail")
	public Response showProduct (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);
			
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			String productId = iptObj.getString(Constant.KEY_PRODUCT_ID);
			String anyway = iptObj.getString(Constant.KEY_ANYWAY);
			
			//沒找到商品會傳回false
			JSONObject obj = new JSONObject(getQueryJSONString(
					String.format("exec 顯示商品詳情 '%s', '%s', '%s'", userId, productId, anyway), false
			)); 
			
			//覆蓋是否最愛的值為布林
			if (obj.getString(Constant.KEY_FAVORITE).equals("1"))
				obj.put(Constant.KEY_FAVORITE, true);
			else
				obj.put(Constant.KEY_FAVORITE, false);
			
			//覆蓋是否談過的值為布林
			if (obj.getString(Constant.KEY_HAVE_TALKED).equals("1"))
				obj.put(Constant.KEY_HAVE_TALKED, true);
			else
				obj.put(Constant.KEY_HAVE_TALKED, false);
			
			optObj.put(Constant.KEY_PRODUCT, obj);
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//加入我的最愛
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/add_favorite")
	public Response addFavorite (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);
			
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			String productId = iptObj.getString(Constant.KEY_PRODUCT_ID);
			optObj = new JSONObject(getQueryJSONString(					
					String.format("exec 加入我的最愛 '%s', '%s'", userId, productId), false
			));
			if (optObj.get(Constant.KEY_IS_ADD).equals("1"))
				optObj.put(Constant.KEY_IS_ADD, true);
			else
				optObj.put(Constant.KEY_IS_ADD, false);				
			
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//顯示會員檔案
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/show_profile")
	public Response showProfile (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);
			
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			String memberId = iptObj.getString(Constant.KEY_MEMBER_ID);
			boolean isSetting = iptObj.getBoolean(Constant.KEY_IS_SETTING);
			
			JSONObject obj = new JSONObject(getQueryJSONString(
					String.format("exec 顯示會員檔案 '%s', '%s'", userId, memberId), false
			));
			
			//若是在會員檔案頁面，則將密碼移除，並加入庫存
			if (!isSetting) {
				obj.remove(Constant.KEY_PASSWORD);
				optObj.put(Constant.KEY_PROFILE, obj);
				
				optObj.put(Constant.KEY_STOCK, new JSONArray(getQueryJSONString(
						String.format("exec 列示庫存 '%s'", memberId), true					
				)));
			}else {
				obj.remove(Constant.KEY_POSITIVE);
				obj.remove(Constant.KEY_NEGATIVE);
				obj.remove(Constant.KEY_VALUE);
				optObj.put(Constant.KEY_PROFILE, obj);
			}
				
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {			
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}	
	
	//評價會員
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/evaluate")
	public Response evaluate (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);
			
			JSONObject iptObj = new JSONObject(reqJSONString);
			String giverId = iptObj.getString(Constant.KEY_GIVER_ID);
			String receiverId = iptObj.getString(Constant.KEY_RECEIVER_ID);
			String value = iptObj.getString(Constant.KEY_VALUE);
			executeUpdate(
					String.format("exec 評價會員 '%s', '%s', '%s'", giverId, receiverId, value)
			);
			
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//列示我的最愛
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/list_favorite")
	public Response listFavorite (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			
			JSONArray ary = new JSONArray(getQueryJSONString(
					String.format("exec 列示我的最愛 '%s'", userId), true
			));
			
			if (ary.length() != 0) {
				optObj.put(Constant.KEY_PRODUCTS, ary);
				optObj.put(Constant.KEY_STATUS, true);
			}else {
				optObj.put(Constant.KEY_STATUS, false);
			}
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//列示信箱訊息
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/list_mails")
	public Response listMails (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			
			JSONArray ary = new JSONArray(getQueryJSONString(
					String.format("exec 列示信箱訊息 '%s'", userId), true
			));
			
			if (ary.length() != 0) {
				optObj.put(Constant.KEY_MAILS, ary);
				optObj.put(Constant.KEY_STATUS, true);
			}else {
				optObj.put(Constant.KEY_STATUS, false);
			}			
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//顯示完整交談室
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/show_chatroom")
	public Response showChatroom (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);

			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			String memberId = iptObj.getString(Constant.KEY_MEMBER_ID);
			String productId = iptObj.getString(Constant.KEY_PRODUCT_ID);
			
			optObj = new JSONObject(getQueryJSONString(
					String.format("exec 搜尋大頭照 '%s', '%s'", userId, memberId), false
			));
			optObj.put(Constant.KEY_STATUS, false);
						
			optObj.put(Constant.KEY_PRODUCTS, new JSONArray(getQueryJSONString(
					String.format("exec 列示交談商品 '%s', '%s'", userId, memberId), true
			)));
			
			JSONObject obj = new JSONObject(getQueryJSONString(
					String.format("exec 搜尋商品名稱 '%s'", productId), false					
			));
			obj.put(Constant.KEY_MESSAGE, new JSONArray(getQueryJSONString(
					String.format("exec 列示交談訊息 '%s', '%s', '%s'", userId, memberId, productId), true
			)));			
			optObj.put(Constant.KEY_CHAT, obj);			
			
			optObj.put(Constant.KEY_STATUS, true);			
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//列示交談訊息
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/list_message")
	public Response listMessage (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);
			
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			String memberId = iptObj.getString(Constant.KEY_MEMBER_ID);
			String productId = iptObj.getString(Constant.KEY_PRODUCT_ID);			
			
			JSONObject obj = new JSONObject(getQueryJSONString(
					String.format("exec 搜尋商品名稱 '%s'", productId), false					
			));
			obj.put(Constant.KEY_MESSAGE, new JSONArray(getQueryJSONString(
					String.format("exec 列示交談訊息 '%s', '%s', '%s'", userId, memberId, productId), true
			)));			
			optObj.put(Constant.KEY_CHAT, obj);		
			
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//傳送訊息
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/send_message")
	public Response sendMessage (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);
			
			JSONObject iptObj = new JSONObject(reqJSONString);
			String senderId = iptObj.getString(Constant.KEY_SENDER_ID);
			String receiverId = iptObj.getString(Constant.KEY_RECEIVER_ID);
			String productId = iptObj.getString(Constant.KEY_PRODUCT_ID);
			String message = iptObj.getString(Constant.KEY_MESSAGE);
			executeUpdate(
					String.format("exec 傳送訊息 '%s', '%s', '%s', '%s'", 
									senderId, receiverId, productId, message)					
			);			
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//列示庫存
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/show_stock")
	public Response showStock (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			
			JSONArray ary = new JSONArray(getQueryJSONString(
					String.format("exec 列示庫存 '%s'", userId), true					
			));
			
			if (ary.length() != 0) {
				optObj.put(Constant.KEY_STOCK, ary);
				optObj.put(Constant.KEY_STATUS, true);
			}else {
				optObj.put(Constant.KEY_STATUS, false);
			}
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//編輯商品
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/edit_product")
	public Response editProduct (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);
			
			JSONObject iptObj = new JSONObject(reqJSONString);
			String productId = iptObj.getString(Constant.KEY_PRODUCT_ID);
			String title = iptObj.getString(Constant.KEY_TITLE);
			String price = iptObj.getString(Constant.KEY_PRICE);
			String type = iptObj.getString(Constant.KEY_TYPE);
			String condition = iptObj.getString(Constant.KEY_CONDITION);
			String note = iptObj.getString(Constant.KEY_NOTE);
			String ps = iptObj.getString(Constant.KEY_PS);
			String photo1 = iptObj.getString(Constant.KEY_PHOTO1);
			String photo2 = iptObj.getString(Constant.KEY_PHOTO2);
			String photo3 = iptObj.getString(Constant.KEY_PHOTO3);
			String photo4 = iptObj.getString(Constant.KEY_PHOTO4);
			String photo5 = iptObj.getString(Constant.KEY_PHOTO5);
			
			executeUpdate(
					String.format("exec 編輯商品 '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'", 
									productId, title, price, type, condition, note, ps, photo1, photo2, photo3, photo4, photo5)
			);
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//下架商品
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/drop_product")
	public Response dropProduct (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);
			
			JSONObject iptObj = new JSONObject(reqJSONString);
			String productId = iptObj.getString(Constant.KEY_PRODUCT_ID);
			
			executeUpdate(
					String.format("exec 下架商品 '%s'", productId)
			);
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//編輯個人檔案
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/edit_profile")
	public Response editProfile (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			optObj.put(Constant.KEY_STATUS, false);
			
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			String avatar = iptObj.getString(Constant.KEY_AVATAR);
			String name = iptObj.getString(Constant.KEY_NAME);
			String department = iptObj.getString(Constant.KEY_DEPARTMENT);
			String email = iptObj.getString(Constant.KEY_EMAIL);
			String pwd = iptObj.getString(Constant.KEY_PASSWORD);
			executeUpdate(
					String.format("exec 編輯個人檔案 '%s', '%s', '%s', '%s', '%s', '%s'", 
									userId, avatar, name, department, email, pwd)
			);
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
		
	private String getQueryJSONString (String mandate, boolean convertToArray) throws SQLException, SQLServerException, JSONException {
		System.out.println(mandate);
		
    	Connection con = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	JSONArray optAry = new JSONArray(); //預設[]
    	try {
    		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    		con = DriverManager.getConnection(Constant.DB_CONNECT_STRING);
    		stmt = con.createStatement();
    		rs = stmt.executeQuery(mandate);
    		
    		ResultSetMetaData rsmd = rs.getMetaData();
    		int colCount = rsmd.getColumnCount();
			while (rs.next()) {
				JSONObject tmpObj = new JSONObject();
    			for (int i=1; i<=colCount; i++) {					
    				tmpObj.put(rsmd.getColumnName(i), rs.getString(i));					
    			}
    			optAry.put(tmpObj);        			
    		}   		
    	} catch (ClassNotFoundException e) {
    	} finally {
    		try {
        		if (rs != null) {
            		rs.close();		
            		rs = null;	
        		}
        		if (stmt != null) {
            		stmt.close();
            		stmt = null;    			
        		}
        		if (con != null) {
            		con.close();
            	    con = null;
        		}
    		} catch (SQLException e) {
        	}
    	}
    	
    	if (convertToArray) //若發生例外，傳回[]、{}
    		return optAry.toString();
    	else {
    		JSONObject optObj = optAry.getJSONObject(0);
    		return optObj.toString();
    	}
	}
    	
	private void executeUpdate (String mandate) throws SQLException {
		System.out.println(mandate);
		
		Connection con = null;
    	Statement stmt = null;
    	try {
    		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");    		
    		con = DriverManager.getConnection(Constant.DB_CONNECT_STRING);
    		stmt = con.createStatement();
    		stmt.executeUpdate(mandate);
    		
    	} catch (ClassNotFoundException e) {
    	} finally {
    		try {
        		if (stmt != null) {
            		stmt.close();
            		stmt = null;    			
        		}
        		if (con != null) {
            		con.close();			              
            	    con = null;    			
        		}	
    		} catch (SQLException e) {
        	}
    	}
	}
	
    private Response getJSONResponse(String resJSONString) {
    	return Response.ok(resJSONString, MediaType.APPLICATION_JSON).build();
    }

}
