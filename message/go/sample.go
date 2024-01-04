package main

import (
	"fmt"
	"github.com/sbzhu/weworkapi_golang/wxbizmsgcrypt"
)

func main() {
	token := "SdBcJhEt1X0izTA25VuGZFtAw7"
	appId := "801159"
	encodingAeskey := "HE2TfUnOpq8jWN5ZbFwMcvcmkcbXjPIn8afCSk4GT6q"
	wxcpt := wxbizmsgcrypt.NewWXBizMsgCrypt(token, encodingAeskey, appId, wxbizmsgcrypt.XmlType)

	// reqMsgSign := HttpUtils.ParseUrl("msg_signature")
	reqMsgSign := "83c29839d75980d98018c96094ef202ec129241a"
	// reqTimestamp := HttpUtils.ParseUrl("timestamp")
	reqTimestamp := "1701932041667"
	// reqNonce := HttpUtils.ParseUrl("nonce")
	reqNonce := "6284853754"
	// post请求的密文数据
	// reqData = HttpUtils.PostData()
	reqData := []byte("<xml><ToUserName><![CDATA[801159]]></ToUserName><Encrypt><![CDATA[ZI0Yy1szqujD5QZDIipGQErFlcI9xgE6bQqPw2iBajlYhnSdeOgOfqoniicEqtfucLFDkLPc6D9pG6QvuPZejVa0xq9H9kvMm9DdqrFPvb35EguA1Xljj4psixoJcAMAUCWodD4R74OpCdoXjwRdZzJOKVORbhtj+pNGY0tj3rbXa3obtGkebuJWBn+g2rBpgX0OvN6RhVSOkVbFl472f7sVFSxUd6Gg2OUi6no7xLMmrOup4VPdOq7FEmWsaJ7xC04aZWu2Od3/OjOfavTvwgwMmYkdhrwG+TqhW3venmZYZqPNO42dlYHXrVSB3M1IADthWyDReKvDdPZ5LjAspA==]]></Encrypt><AgentID><![CDATA[218]]></AgentID></xml>")

	msg, cryptErr := wxcpt.DecryptMsg(reqMsgSign, reqTimestamp, reqNonce, reqData)
	if cryptErr != nil {
		fmt.Println("DecryptMsg fail cryptErr:", cryptErr)
		return
	}
	fmt.Println("after decrypt msg:", string(msg))
}
