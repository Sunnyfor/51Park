package cn.com.unispark.fragment.home.pay.uubeepay;

/**
 * 请求结果对象基类
 */
public class ResultBase {
    public static final String RES_SUCCESS = "000000";
    public String ret_code;
    public String ret_msg;

    public boolean isSuccess() {
        return RES_SUCCESS.equals(ret_code);
    }

	@Override
	public String toString() {
		return "ResultBase [ret_code=" + ret_code + ", ret_msg=" + ret_msg
				+ "]";
	}
    
    
}
