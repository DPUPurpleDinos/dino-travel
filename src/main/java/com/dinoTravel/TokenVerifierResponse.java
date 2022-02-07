package com.dinoTravel;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;

public class TokenVerifierResponse {
  private final Payload payload;
  private final boolean validity;

  public TokenVerifierResponse(Payload payload, Boolean validity) {
    this.validity = validity;
    this.payload = payload;
  }

  public Payload getPayload() {
    return payload;
  }

  public boolean isValid() {
    return validity;
  }
}
