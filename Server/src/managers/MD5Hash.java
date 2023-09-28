package managers;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import javax.xml.bind.DatatypeConverter;


public class MD5Hash {
    public static String getMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger num = new BigInteger(1, messageDigest);
            String myHash = num.toString(16);
            while (myHash.length()<32){
                myHash = "0" + myHash;
             }
            return myHash;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 должен быть поддержан вашей Java Virtual Machine.", e);
        }
    }
}
