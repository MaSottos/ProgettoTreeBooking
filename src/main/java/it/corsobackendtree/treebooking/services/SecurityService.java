package it.corsobackendtree.treebooking.services;

import org.springframework.stereotype.Service;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class SecurityService {
    public String computeHash(String s, Integer salt){
        String mix = salt+s+salt;
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] digest = md.digest(mix.getBytes());
        BigInteger bi = new BigInteger(1, digest);
        String hash = bi.toString(16);
        return hash;
    }
}
