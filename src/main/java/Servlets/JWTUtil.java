/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;

/**
 *
 * @author fernan
 */
public class JWTUtil {
    
    private static final String SECRET = "MI_CLAVE_SUPER_LARGA_DE_MAS_DE_32_CARACTERES_123456";

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    public static String generarToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(KEY)
                .compact();
    }
}
