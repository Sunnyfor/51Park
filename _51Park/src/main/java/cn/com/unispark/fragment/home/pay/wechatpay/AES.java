package cn.com.unispark.fragment.home.pay.wechatpay;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import cn.com.unispark.fragment.home.pay.alipay.Base64;
/**
 * AES加密
 * @author 	Tom
 */
public class AES {

	public static String encrypt(String input, String key){
        byte[] crypted = null;
        try{
          SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skey);
            crypted = cipher.doFinal(input.getBytes());
          }catch(Exception e){
        	  e.printStackTrace();
              System.out.println(e.toString());
          }
          return new String(Base64.encode(crypted));
      }

      public static String decrypt(String input, String key){
    	  
          byte[] output = null;
          
          try{
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skey);
            output = cipher.doFinal(Base64.decode(input));
          }catch(Exception e){
        	  e.printStackTrace();
        	  System.out.println(e.toString());
          }
          return new String(output);
      }
}
