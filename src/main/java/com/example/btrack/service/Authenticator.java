package com.example.btrack.service;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import com.amazonaws.services.cognitoidp.model.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class Authenticator {

    private final String clientId;
    private final String userPoolId;
    private final AWSCognitoIdentityProvider cognitoIdentityProvider;

    @Autowired
    public Authenticator(@Value("${clientId}")String clientId, @Value("${userPoolId}") String userPoolId) {
        this.clientId = clientId;
        this.userPoolId = userPoolId;
        this.cognitoIdentityProvider = AWSCognitoIdentityProviderClientBuilder.defaultClient();
    }

    public boolean isUserAuthenticated(String token) {
        System.out.println("Entered isUserAuthenticated");
        AuthFlowType authFlowType;
        authFlowType = AuthFlowType.REFRESH_TOKEN_AUTH;
        InitiateAuthRequest authRequest = new InitiateAuthRequest()
                .withAuthFlow(authFlowType)
                .withClientId(clientId)
                .addAuthParametersEntry("idToken", token);

        try {
            InitiateAuthResult authResult = cognitoIdentityProvider.initiateAuth(authRequest);

            if (authResult.getChallengeName() == null) {
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error in authenticating user: " + e.getMessage());
        }

        return false;
    }


    public boolean isTokenExpired(String token) {
        try {
            // Decode the token to get the header and claims
            JWT jwt = new JWT();
            Map<String, Claim> claims = jwt.decodeJwt(token).getClaims();

            // Get the "exp" claim from the claims map
            Claim expClaim = claims.get("exp");
            if (expClaim == null) {
                return true;
            }

            // Get the expiry time as a long value
            long expiryTime = expClaim.asLong();

            // Get the current time as a long value
            long currentTime = System.currentTimeMillis() / 1000L;

            // Compare the current time with the expiry time
            return currentTime >= expiryTime;
        } catch (JWTDecodeException e) {
            return true;
        }
    }


    public ResponseEntity<String> refreshUserToken(String refreshToken) {
        AuthFlowType authFlowType;
        authFlowType = AuthFlowType.REFRESH_TOKEN_AUTH;
        InitiateAuthRequest authRequest = new InitiateAuthRequest()
                .withAuthFlow(authFlowType)
                .withClientId(clientId)
                .addAuthParametersEntry("REFRESH_TOKEN", refreshToken);

        try {
            InitiateAuthResult authResult = cognitoIdentityProvider.initiateAuth(authRequest);
            String idToken = authResult.getAuthenticationResult().getIdToken();
            String accessToken = authResult.getAuthenticationResult().getAccessToken();
            String refreshTokenf = authResult.getAuthenticationResult().getRefreshToken();

            return new ResponseEntity<>("{\"idToken\": \"" + idToken + "\", " +
                    "\"accessToken\": \"" + accessToken + "\", " +
                    "\"refreshToken\": \"" + refreshToken + "\"}",
                    HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("{\"error\": \"" + e.getMessage() + "\"}",
                    HttpStatus.BAD_REQUEST);
        }
    }




}

