#!/usr/bin/python
# -*- coding: utf-8 -*-
import base64

from Crypto.Cipher import AES


class AESCipher:
    def __init__(self, key):
        self.key = base64.b64decode(key + "=")  # 只截取16位
        self.iv = self.key[:16]  # 16位字符，用来填充缺失内容，可固定值也可随机字符串，具体选择看需求。
        self.block_size = 32

    def __unpad(self, text):
        pad = ord(text[-1])
        return text[:-pad]

    def decrypt(self, enc):
        """解密"""
        enc = base64.b64decode(enc)
        cipher = AES.new(self.key, AES.MODE_CBC, self.iv)
        return self.__unpad(cipher.decrypt(enc).decode("utf-8"))

    def __pad(self, text):
        """填充方式，加密内容必须为16字节的倍数，若不足则使用self.iv进行填充"""
        text_length = len(text)
        amount_to_pad = AES.block_size - (text_length % AES.block_size)
        if amount_to_pad == 0:
            amount_to_pad = AES.block_size
        pad = chr(amount_to_pad)
        return text + pad * amount_to_pad

    def encode(self, text):
        """ 对需要加密的明文进行填充补位
        @param text: 需要进行填充补位操作的明文
        @return: 补齐明文字符串
        """
        text = text.encode()
        text_length = len(text)
        # 计算需要填充的位数
        amount_to_pad = self.block_size - (text_length % self.block_size)
        if amount_to_pad == 0:
            amount_to_pad = self.block_size
        # 获得补位所用的字符
        pad = chr(amount_to_pad)
        return text + (pad * amount_to_pad).encode()

    def encrypt(self, raw):
        """加密"""
        raw = self.encode(raw)
        cipher = AES.new(self.key, AES.MODE_CBC, self.iv)
        return base64.b64encode(cipher.encrypt(raw))


if __name__ == '__main__':
    aesKey = "0VShOOAlYzVbKvcU0JRfsEdArPPunB5B6CMoFdAYVXa"
    e = AESCipher(aesKey)
    plaintext = "1111"
    secretText = e.encrypt(plaintext)
    print("密文：", secretText)
    text = e.decrypt(secretText)
    print('明文：' + text)
