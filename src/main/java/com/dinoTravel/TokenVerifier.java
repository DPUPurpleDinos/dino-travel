package com.dinoTravel;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.common.io.BaseEncoding;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

/***
 * Class for verifying any incoming tokens
 */
public class TokenVerifier {

  /***
   * The google ID verifier
   */
  private final static GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
      .setAudience(Collections.singleton("1042234633479-gpprc2adcpltfjnaij7gib55ko91441n.apps.googleusercontent.com"))
      .build();

  /***
   * Class for verifying if a token is valid
   * @param tokenString the token string to verify
   * @return Token Verifier Response. Returned if valid
   * @throws TokenInvalid Error thrown if the token is invalid
   */
  public static TokenVerifierResponse verifyToken(String tokenString) throws TokenInvalid{
    GoogleIdToken idToken = null;
    try {
      idToken = verifier.verify(tokenString);
    } catch (GeneralSecurityException | IOException e) {
      System.out.println("bad");
      throw new TokenInvalid(e.getMessage());
    }
    if (idToken != null) {
      return new TokenVerifierResponse(idToken.getPayload(), true);
    }else{
      throw new TokenInvalid("Token Expired");
    }
  }
}
