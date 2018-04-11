package org.shareme;

public class Constant {
	public static final String DB_IP = "localhost";
	public static final String DB_NAME = "ShareMe";
	public static final String DB_USER = "sa";
	public static final String DB_PASSWORD = "2rrxuolx";
	public static final String DB_CONNECT_STRING = String.format(
			"jdbc:sqlserver://%s;databaseName=%s;user=%s;password=%s", DB_IP, DB_NAME, DB_USER, DB_PASSWORD
			);
	
	public static final String KEY_STATUS = "Status";
	public static final String KEY_USER_ID = "UserId";
	public static final String KEY_PASSWORD = "Password";
	public static final String KEY_NAME = "Name";
	public static final String KEY_DEPARTMENT = "Department";
	public static final String KEY_GENDER = "Gender";
	public static final String KEY_EMAIL = "Email";
	public static final String KEY_TYPE = "Type";
	public static final String KEY_KEYWORD = "Keyword";
	public static final String KEY_TITLE = "Title";
	public static final String KEY_PRICE = "Price";
	public static final String KEY_CONDITION = "Condition";
	public static final String KEY_NOTE = "Note";
	public static final String KEY_PS = "PS";
	public static final String KEY_SELLER = "Seller";
	public static final String KEY_PHOTO1 = "Photo1";
	public static final String KEY_PHOTO2 = "Photo2";
	public static final String KEY_PHOTO3 = "Photo3";
	public static final String KEY_PHOTO4 = "Photo4";
	public static final String KEY_PHOTO5 = "Photo5";
	public static final String KEY_PRODUCT = "Product";
	public static final String KEY_PRODUCT_ID = "ProductId";
	public static final String KEY_ANYWAY = "Anyway";
	public static final String KEY_FAVORITE = "Favorite";
	public static final String KEY_IS_ADD = "IsAdd";
	public static final String KEY_MEMBER_ID = "MemberId";
	public static final String KEY_IS_SETTING = "IsSetting";
	public static final String KEY_GIVER_ID = "GiverId";
	public static final String KEY_RECEIVER_ID = "ReceiverId";
	
	public static final String KEY_POSITIVE = "Positive";
	public static final String KEY_NEGATIVE = "Negative";
	public static final String KEY_VALUE = "Value";
	public static final String KEY_SENDER_ID = "SenderId";
	public static final String KEY_AVATAR = "Avatar";
	
	
	public static final String KEY_PROFILE = "Profile";
	public static final String KEY_PRODUCTS = "Products";
	public static final String KEY_STOCK = "Stock";
	public static final String KEY_MAILS = "Mails";
	public static final String KEY_MESSAGE = "Message";
	

}
