package main

import (
	"bytes"
	"crypto/aes"
	"crypto/cipher"
	"encoding/base64"
	"errors"
	"fmt"
)

// pKCS7Padding 加密
func pKCS7Padding(plaintext string, blockSize int) []byte {
	padding := blockSize - (len(plaintext) % blockSize)
	padtext := bytes.Repeat([]byte{byte(padding)}, padding)
	var buffer bytes.Buffer
	buffer.WriteString(plaintext)
	buffer.Write(padtext)
	return buffer.Bytes()
}

// pKCS7Padding 解密
func pKCS7Unpadding(plaintext []byte, block_size int) ([]byte, error) {
	plaintext_len := len(plaintext)
	if nil == plaintext || plaintext_len == 0 {
		return nil, errors.New("pKCS7Unpadding error nil or zero")
	}
	if plaintext_len%block_size != 0 {
		return nil, errors.New("pKCS7Unpadding text not a multiple of the block size")
	}
	padding_len := int(plaintext[plaintext_len-1])
	if len(plaintext) < plaintext_len-padding_len || plaintext_len-padding_len < 0 {
		return nil, errors.New("pKCS7Unpadding plaintext len too small")
	}
	return plaintext[:plaintext_len-padding_len], nil
}

// CbcEncrypt 加密
func CbcEncrypt(plaintext, encodingAes string) ([]byte, error) {
	aeskey, err := base64.StdEncoding.DecodeString(encodingAes + "=")
	if nil != err {
		return nil, err
	}
	const blockSize = 32
	padMsg := pKCS7Padding(plaintext, blockSize)

	block, err := aes.NewCipher(aeskey)
	if err != nil {
		return nil, err
	}

	ciphertext := make([]byte, len(padMsg))
	iv := aeskey[:aes.BlockSize]

	mode := cipher.NewCBCEncrypter(block, iv)

	mode.CryptBlocks(ciphertext, padMsg)
	base64Msg := make([]byte, base64.StdEncoding.EncodedLen(len(ciphertext)))
	base64.StdEncoding.Encode(base64Msg, ciphertext)

	return base64Msg, nil
}

// CbcDecrypt 解密
func CbcDecrypt(base64EncryptMsg, encodingAes string) ([]byte, error) {
	aeskey, err := base64.StdEncoding.DecodeString(encodingAes + "=")
	if nil != err {
		return nil, err
	}
	encrypt_msg, err := base64.StdEncoding.DecodeString(base64EncryptMsg)
	if err != nil {
		return nil, err
	}

	block, err := aes.NewCipher(aeskey)
	if err != nil {
		return nil, err
	}

	if len(encrypt_msg) < aes.BlockSize {
		return nil, err
	}
	iv := aeskey[:aes.BlockSize]
	if len(encrypt_msg)%aes.BlockSize != 0 {
		return nil, err
	}
	mode := cipher.NewCBCDecrypter(block, iv)
	mode.CryptBlocks(encrypt_msg, encrypt_msg)

	const blockSize = 32
	encrypt_msg, err = pKCS7Unpadding(encrypt_msg, blockSize)
	return encrypt_msg, err
}

// CbcEncryptReturnStr 加密
func CbcEncryptReturnStr(plaintext, encodingAes string) (string, error) {
	bs, err := CbcEncrypt(plaintext, encodingAes)
	if err != nil {
		return plaintext, err
	}
	return string(bs), err
}

// CbcDecryptReturnStr 解密
func CbcDecryptReturnStr(base64EncryptMsg, encodingAes string) (string, error) {
	bs, err := CbcDecrypt(base64EncryptMsg, encodingAes)
	if err != nil {
		return base64EncryptMsg, err
	}
	return string(bs), err
}

func main() {
	plaintext := "1111"
	aesKey := "0VShOOAlYzVbKvcU0JRfsEdArPPunB5B6CMoFdAYVXa"

	encryptMsg, err := CbcEncryptReturnStr(plaintext, aesKey)
	if err != nil {
		fmt.Println("err:", err.Error())
		return
	}
	fmt.Println("encryptMsg:", encryptMsg)

	decryptMsg, err := CbcDecryptReturnStr(encryptMsg, aesKey)
	if err != nil {
		fmt.Println("err:", err.Error())
		return
	}
	fmt.Println("decryptMsg:", decryptMsg)
}
