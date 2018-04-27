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
	
	//���U
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
					String.format("exec ���U '%s', '%s', '%s', '%s', '%s', '%s'", userId, pwd, name, dep, gender, email
			));
			
			optObj.put(Constant.KEY_STATUS, true);
		}catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}

	//�n�J
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
					String.format("exec �n�J '%s', '%s'", userId, pwd), false
			)));
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException|SQLException e) {
			optObj.put(Constant.KEY_STATUS, false);
		}
		return getJSONResponse(optObj.toString());
	}

	//�C�ܰӫ~
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
					String.format("exec �C�ܰӫ~ '%s', '%s'", type, keyword)
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
	
	//�Z�n�ӫ~
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
					String.format("exec �Z�n�ӫ~ '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'", 
							title, price, type, condition, note, ps, seller, photo1, photo2, photo3, photo4, photo5)
					, false)));
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//��ܰӫ~�Ա�
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
			
			//�S���ӫ~�|�Ǧ^false
			JSONObject obj = new JSONObject(getQueryJSONString(
					String.format("exec ��ܰӫ~�Ա� '%s', '%s', '%s'", userId, productId, anyway), false
			)); 
			
			//�л\�O�_�̷R���Ȭ����L
			if (obj.getString(Constant.KEY_FAVORITE).equals("1"))
				obj.put(Constant.KEY_FAVORITE, true);
			else
				obj.put(Constant.KEY_FAVORITE, false);
			
			//�л\�O�_�͹L���Ȭ����L
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
	
	//�[�J�ڪ��̷R
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
					String.format("exec �[�J�ڪ��̷R '%s', '%s'", userId, productId), false
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
	
	//��ܷ|���ɮ�
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
					String.format("exec ��ܷ|���ɮ� '%s', '%s'", userId, memberId), false
			));
			
			//�Y�O�b�|���ɮ׭����A�h�N�K�X�����A�å[�J�w�s
			if (!isSetting) {
				obj.remove(Constant.KEY_PASSWORD);
				optObj.put(Constant.KEY_PROFILE, obj);
				
				optObj.put(Constant.KEY_STOCK, new JSONArray(getQueryJSONString(
						String.format("exec �C�ܮw�s '%s'", memberId), true					
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
	
	//�����|��
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
					String.format("exec �����|�� '%s', '%s', '%s'", giverId, receiverId, value)
			);
			
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//�C�ܧڪ��̷R
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/list_favorite")
	public Response listFavorite (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			
			JSONArray ary = new JSONArray(getQueryJSONString(
					String.format("exec �C�ܧڪ��̷R '%s'", userId), true
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
	
	//�C�ܫH�c�T��
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/list_mails")
	public Response listMails (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			
			JSONArray ary = new JSONArray(getQueryJSONString(
					String.format("exec �C�ܫH�c�T�� '%s'", userId), true
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
	
	//��ܧ����ͫ�
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
					String.format("exec �j�M�j�Y�� '%s', '%s'", userId, memberId), false
			));
			optObj.put(Constant.KEY_STATUS, false);
						
			optObj.put(Constant.KEY_PRODUCTS, new JSONArray(getQueryJSONString(
					String.format("exec �C�ܥ�Ͱӫ~ '%s', '%s'", userId, memberId), true
			)));
			
			JSONObject obj = new JSONObject(getQueryJSONString(
					String.format("exec �j�M�ӫ~�W�� '%s'", productId), false					
			));
			obj.put(Constant.KEY_MESSAGE, new JSONArray(getQueryJSONString(
					String.format("exec �C�ܥ�ͰT�� '%s', '%s', '%s'", userId, memberId, productId), true
			)));			
			optObj.put(Constant.KEY_CHAT, obj);			
			
			optObj.put(Constant.KEY_STATUS, true);			
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//�C�ܥ�ͰT��
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
					String.format("exec �j�M�ӫ~�W�� '%s'", productId), false					
			));
			obj.put(Constant.KEY_MESSAGE, new JSONArray(getQueryJSONString(
					String.format("exec �C�ܥ�ͰT�� '%s', '%s', '%s'", userId, memberId, productId), true
			)));			
			optObj.put(Constant.KEY_CHAT, obj);		
			
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//�ǰe�T��
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
					String.format("exec �ǰe�T�� '%s', '%s', '%s', '%s'", 
									senderId, receiverId, productId, message)					
			);			
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//�C�ܮw�s
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/show_stock")
	public Response showStock (String reqJSONString) {
		JSONObject optObj = new JSONObject();
		try {
			JSONObject iptObj = new JSONObject(reqJSONString);
			String userId = iptObj.getString(Constant.KEY_USER_ID);
			
			JSONArray ary = new JSONArray(getQueryJSONString(
					String.format("exec �C�ܮw�s '%s'", userId), true					
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
	
	//�s��ӫ~
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
					String.format("exec �s��ӫ~ '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s'", 
									productId, title, price, type, condition, note, ps, photo1, photo2, photo3, photo4, photo5)
			);
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//�U�[�ӫ~
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
					String.format("exec �U�[�ӫ~ '%s'", productId)
			);
			optObj.put(Constant.KEY_STATUS, true);
		} catch (JSONException | SQLException e) {
			e.printStackTrace();
		}
		return getJSONResponse(optObj.toString());
	}
	
	//�s��ӤH�ɮ�
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
					String.format("exec �s��ӤH�ɮ� '%s', '%s', '%s', '%s', '%s', '%s'", 
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
    	JSONArray optAry = new JSONArray(); //�w�][]
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
    	
    	if (convertToArray) //�Y�o�ͨҥ~�A�Ǧ^[]�B{}
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
