package com.dinoTravel;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

public class TokenVerifier {

  private final static GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
      .setAudience(Collections.singleton("1042234633479-gpprc2adcpltfjnaij7gib55ko91441n.apps.googleusercontent.com"))
      .build();

  public static TokenVerifierResponse verifyToken(String  tokenString) throws GeneralSecurityException, IOException {
    GoogleIdToken idToken = verifier.verify(tokenString);
    if (idToken != null) {
      Payload payload = idToken.getPayload();
      return new TokenVerifierResponse(idToken.getPayload(), true);
    }else{
      return new TokenVerifierResponse(null, true);
    }
  }
}
