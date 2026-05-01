/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Servlets;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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

    // día de duración
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24;

    //GENERAR TOKEN
    public static String generarToken(int id, String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("id", id)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }

    // validar los tokens 
    public static Claims validarToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }
    //extraer los datos
    public static String getUsername(String token) {
        return validarToken(token).getSubject();
    }

    public static String getRol(String token) {
        return (String) validarToken(token).get("rol");
    }

    public static int getId(String token) {
    return ((Number) validarToken(token).get("id")).intValue();
}
}
