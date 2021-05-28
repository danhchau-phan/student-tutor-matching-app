package mainview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A class providing easy access to repetively used static final objects
 */
public class Utils {
	private static final ObjectMapper mapper = new ObjectMapper();
	    
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	
    public static Date formatDate(String datetime) {
    	if (datetime == null) {
    		return null;
    	}
    	Date date = null;
		try {
			date = format.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
    	return date;
    }
    
    public static final NotificationFrame SUCCESS_CONTRACT_CREATION = new NotificationFrame("Contract created successfully");
    public static final NotificationFrame SUCCESS_BID_CREATION = new NotificationFrame("Bid created successfully");
    public static final NotificationFrame INSUFFICIENT_COMPETENCY =	new NotificationFrame("You don't have sufficient competency this bid required");
    public static final NotificationFrame INSUFFICIENT_COMPETENCY_REUSE_CONTRACT = new NotificationFrame("This tutor doesn't have sufficient competency this contract required");
    public static final NotificationFrame SUCCESS_MATCH_REQUEST =	new NotificationFrame("Match request created successfully");
    public static final NotificationFrame PLEASE_FILL_IN =	new NotificationFrame("Please fill in all fields");
    public static final NotificationFrame INVALID_FIELDS =	new NotificationFrame("Invalid fields");
	public static final NotificationFrame REACHED_CONTRACT_LIMIT =	new NotificationFrame("You have reached contract limit");
	public static final NotificationFrame CONTRACT_SIGNED =	new NotificationFrame("Contract signed successfully");
	public static final NotificationFrame OTHER_PARTY_PENDING =	new NotificationFrame("Contract pending on the other party's signature");
	public static final NotificationFrame INVALID_USER = new NotificationFrame("Invalid user credentials");
	public static final NotificationFrame CANNOT_CREATE_BID = new NotificationFrame("Please click modify bid");
	public static final NotificationFrame CANNOT_MODIFY_BID = new NotificationFrame("Please click create bid");
}
