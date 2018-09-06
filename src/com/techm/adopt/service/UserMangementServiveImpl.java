package com.techm.adopt.service;

import java.security.MessageDigest;
import org.apache.log4j.Logger;
import com.techm.adopt.bean.UserBean;
import com.techm.adopt.dao.UserMangementDao;
	
public class UserMangementServiveImpl {
	
	private final  Logger LOGGER = Logger.getLogger(UserMangementServiveImpl.class);
	
	public  void cryptWithMD5(UserBean userDetails) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
			md.update(userDetails.getPassword().getBytes());

			byte byteData[] = md.digest();

			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
						.substring(1));
			}
			userDetails.setPassword(sb.toString());
			new UserMangementDao().insertUserDetails(userDetails);
		} catch (Exception e) {
			LOGGER.error("Exception in UserMangementServiveImply", e);
		}
		
	}
}
