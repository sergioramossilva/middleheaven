package org.middleheaven.crypto;

import org.apache.commons.codec.binary.Base64;



public class CryptoUtils {


    
    public static byte[] encodeBase64(byte[] data ){
        return Base64.encodeBase64(data);
    }

    
    public static byte[] decodeBase64(byte[] encripted ){
        return Base64.decodeBase64(encripted);
    }
}
