package com.base.sbc.config.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.xml.bind.DatatypeConverter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Fred
 * @data 创建时间:2021/9/26
 */
public class TokenUtils {

	  private static String secret = "XX#asd$%()(#asdklaslkdjl!()!KL<><MQLasdMdba*?/cNQNQJQQ:7311asd39982>?Nasd<:{LWPasdW";
	  private static long day = 7L;
	  public static String ID = "userCompany";
	  public static String USER_NAME = "username";

	/**
	 * 解析令牌
	 * @param jwt
	 * @return
	 */
	public static Claims parseJwt(String jwt) {
	    Claims claims = (Claims)Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secret)).parseClaimsJws(jwt).getBody();
	    return claims;
	  }

	/**
	 * 生成jwt令牌
	 * @param map
	 * @return
	 */
	public static String createJwt(Map<String, String> map) {
	    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	    long nowMillis = System.currentTimeMillis();
	    Date now = new Date(nowMillis);
	    
	    JwtBuilder builder = Jwts.builder()
				 //设置jwt编码
				.setId((String)map.get(ID))
				//设置jwt主题
				.setSubject((String)map.get(USER_NAME))
				 //设置jwt签发日期
				.setIssuedAt(now)
		  	    .signWith(signatureAlgorithm, secret);
	    /*设置令牌失效时间*/
	    if (day >= 0L) {
	      long expMillis = nowMillis + day * 12L * 60L * 60L * 1000L;
	      Date exp = new Date(expMillis);
	      builder.setExpiration(exp);
	    }
	    return builder.compact();
	  }
	  
	  public static void main(String[] args) {
	  	Map<String, String> map = new HashMap<>(4);
		map.put("userCompany", "138287186175852544");
		map.put("username", "衣智云");
	  	String c = createJwt(map);
	    Claims c2 = parseJwt("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMzgyODcxODYxNzU4NTI1NDQiLCJzdWIiOiLooaPmmbrkupEiLCJpYXQiOjE2MzI0NjMzNTYsImV4cCI6MTYzMjc2NTc1Nn0.BWZObYEd7YZ357gf_DR0d5h7HjpHn3wTRUXQbma-fZ4");
	    System.out.println(c);
		System.out.println(c2.getId());
	  }
}
