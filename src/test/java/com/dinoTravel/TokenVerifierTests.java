package com.dinoTravel;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TokenVerifierTests {

  private final String ValidToken;
  private final String InValidToken;

  public TokenVerifierTests(){
    Properties properties = new Properties();
    //open the application-test file from the classpath
    try(InputStream is = getClass().getResourceAsStream("/application-test.properties")){
      properties.load(is);
    }catch (IOException e){
      e.printStackTrace();
    }
    //set the key and secretKey
    ValidToken = properties.getProperty("ValidToken");
    InValidToken = properties.getProperty("InValidToken");
  }

  @Test
  void TestInValid(){
    Assertions.assertThrows(TokenInvalid.class, () -> {TokenVerifier.verifyToken(InValidToken);});
  }

  //If this test fails the token may have expired
  @Test
  void TestValid(){
      Assertions.assertTrue(TokenVerifier.verifyToken(ValidToken).isValid());
  }

  @Test
  void TestTooLong(){
    Assertions.assertThrows(TokenInvalid.class, () -> {TokenVerifier.verifyToken(ValidToken + "aaa");});
  }

  @Test
  void TestTooShort(){
    Assertions.assertThrows(TokenInvalid.class, () -> {TokenVerifier.verifyToken("a");});
  }

}
