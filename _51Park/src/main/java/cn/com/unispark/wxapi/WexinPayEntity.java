package cn.com.unispark.wxapi;


import cn.com.unispark.application.BaseEntity;

/**
 * <pre>
 * Function：
 *
 * Created by JoannChen on 2017/9/30 14:25
 * E-mail:Q8622268@foxmail.com
 * QQ:411083907
 * </pre>
 */
public class WexinPayEntity extends BaseEntity {

    private  Weixin data;

    public Weixin getData() {
        return data;
    }

    public void setData(Weixin data) {
        this.data = data;
    }

    public class Weixin{
        private String nonce_str;//随机字符串
        private String prepay_id;//预支付交易标识
        private String sign;//签名
        private String trade_type;//App
        private String result_code;//业务返回代码 success／fall
        private String result_msg;//业务信息
        private String timestamp;//时间戳

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getPrepay_id() {
            return prepay_id;
        }

        public void setPrepay_id(String prepay_id) {
            this.prepay_id = prepay_id;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTrade_type() {
            return trade_type;
        }

        public void setTrade_type(String trade_type) {
            this.trade_type = trade_type;
        }

        public String getResult_code() {
            return result_code;
        }

        public void setResult_code(String result_code) {
            this.result_code = result_code;
        }

        public String getResult_msg() {
            return result_msg;
        }

        public void setResult_msg(String result_msg) {
            this.result_msg = result_msg;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "Weixin{" +
                    "nonce_str='" + nonce_str + '\'' +
                    ", prepay_id='" + prepay_id + '\'' +
                    ", sign='" + sign + '\'' +
                    ", trade_type='" + trade_type + '\'' +
                    ", result_code='" + result_code + '\'' +
                    ", result_msg='" + result_msg + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    '}';
        }
    }
}
