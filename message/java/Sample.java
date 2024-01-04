import com.qq.weixin.mp.aes.WXBizMsgCrypt;

public class Sample {

    public static void main(String[] args) throws Exception {
        String appId = "801159";
        String token = "SdBcJhEt1X0izTA25VuGZFtAw7";
        String encodingAESKey = "HE2TfUnOpq8jWN5ZbFwMcvcmkcbXjPIn8afCSk4GT6q";

        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(token, encodingAESKey, appId);

		/*
		------------使用示例：对推送Ticket消息解密---------------
		假设开发者收到教育号的回调消息如下：
		POST /push/ticket?msg_signature=83c29839d75980d98018c96094ef202ec129241a&timestamp=1701932041667&nonce=6284853754 HTTP/1.1
		Host: sso.qq.com
		Content-Length: 613
		<xml><ToUserName><![CDATA[801159]]></ToUserName><Encrypt><![CDATA[ZI0Yy1szqujD5QZDIipGQErFlcI9xgE6bQqPw2iBajlYhnSdeOgOfqoniicEqtfucLFDkLPc6D9pG6QvuPZejVa0xq9H9kvMm9DdqrFPvb35EguA1Xljj4psixoJcAMAUCWodD4R74OpCdoXjwRdZzJOKVORbhtj+pNGY0tj3rbXa3obtGkebuJWBn+g2rBpgX0OvN6RhVSOkVbFl472f7sVFSxUd6Gg2OUi6no7xLMmrOup4VPdOq7FEmWsaJ7xC04aZWu2Od3/OjOfavTvwgwMmYkdhrwG+TqhW3venmZYZqPNO42dlYHXrVSB3M1IADthWyDReKvDdPZ5LjAspA==]]></Encrypt><AgentID><![CDATA[218]]></AgentID></xml>"

		服务商收到post请求之后应该
		1.解析出url上的参数，包括消息体签名(msg_signature)，时间戳(timestamp)以及随机数字串(nonce)
		2.验证消息体签名的正确性。
		3.将post请求的数据进行xml解析，并将<Encrypt>标签的内容进行解密，解密出来的明文即是用户回复消息的明文，明文格式请参考官方文档
		第2，3步可以用代码提供的库函数DecryptMsg来实现。
		*/
        // String sReqMsgSig = HttpUtils.ParseUrl("msg_signature");
        String sReqMsgSig = "83c29839d75980d98018c96094ef202ec129241a";
        // String sReqTimeStamp = HttpUtils.ParseUrl("timestamp");
        String sReqTimeStamp = "1701932041667";
        // String sReqNonce = HttpUtils.ParseUrl("nonce");
        String sReqNonce = "6284853754";
        // post请求的密文数据
        // sReqData = HttpUtils.PostData();
        String sReqData = "<xml><ToUserName><![CDATA[801159]]></ToUserName><Encrypt><![CDATA[ZI0Yy1szqujD5QZDIipGQErFlcI9xgE6bQqPw2iBajlYhnSdeOgOfqoniicEqtfucLFDkLPc6D9pG6QvuPZejVa0xq9H9kvMm9DdqrFPvb35EguA1Xljj4psixoJcAMAUCWodD4R74OpCdoXjwRdZzJOKVORbhtj+pNGY0tj3rbXa3obtGkebuJWBn+g2rBpgX0OvN6RhVSOkVbFl472f7sVFSxUd6Gg2OUi6no7xLMmrOup4VPdOq7FEmWsaJ7xC04aZWu2Od3/OjOfavTvwgwMmYkdhrwG+TqhW3venmZYZqPNO42dlYHXrVSB3M1IADthWyDReKvDdPZ5LjAspA==]]></Encrypt><AgentID><![CDATA[218]]></AgentID></xml>";
        try {
            String sMsg = wxcpt.DecryptMsg(sReqMsgSig, sReqTimeStamp, sReqNonce, sReqData);
            System.out.println("after decrypt msg: " + sMsg);
        } catch (Exception e) {
            // TODO
            // 解密失败，失败原因请查看异常
            e.printStackTrace();
        }
    }
}
