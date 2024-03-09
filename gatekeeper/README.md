# Authorization Server

Cookies are tied to hostname. Redirects will use `127.0.0.1` and not `localhost`.

![](./docs/authorization_code.png)


```
challenge: 7fb6e885-087e-4475-8dbf-9eff12e361fa
verifier: 10dc2313a16897a3d18fdcba80472e6054ba4c5ae51c1ab60ea8184e11cbaf94
```

Authorization request
```
http://localhost:9000/oauth2/authorize?
response_type=code&
client_id=oidc-client&
scope=openid&
redirect_uri=https://google.com&
code_challenge=7fb6e885-087e-4475-8dbf-9eff12e361fa&
code_challenge_method=S256

http://localhost:9000/oauth2/authorize?response_type=code&client_id=oidc-client&scope=openid&redirect_uri=https://www.google.com&code_challenge=7fb6e885-087e-4475-8dbf-9eff12e361fa&code_challenge_method=S256
```

## Authorize Client

Make sure all client details and uris match with what is registered on the auth server. Client id and client secret is provided to the token endpoint using `httpie`
```
http -f POST :9000/oauth2/token grant_type=client_credentials scope='batches.read' -a simpleclient:simpleclient
```

The response should grant an access token

```
{
    "access_token": "eyJraWQiOiJhZGNiMGI1ZS1hYjUwLTQ5NjUtYTFhYS05MjJiOGYwMTBjNjkiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJzaW1wbGVjbGllbnQiLCJhdWQiOiJzaW1wbGVjbGllbnQiLCJuYmYiOjE3MDc1Nzk5NDQsInNjb3BlIjpbImJhdGNoZXMucmVhZCJdLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjkwMDAiLCJleHAiOjE3MDc1ODAyNDQsImlhdCI6MTcwNzU3OTk0NCwianRpIjoiMjFkMjQ5NDktMDY1MC00YjQ3LWEyZWUtOGRiMTgzMzBmNWYyIn0.h3_fElkfkMPhN8TjEj_X9PWhXeInfZrt3pOklhNfkQmG80c8FwVvMmatJYBemL-rmRGdovCcKewy7aBlVTmvbSj90QHWT-osSqOV1wX2J4zSloW8Q2XjUq9Skm2SoE1a1VLE_1Nh2LRuRuqlO-f_RDHiahvAeIW4kAqOIe9cqM7L3QTf0an1R-2hseXSlPvp5c51KtjMvYUG0OfPZL6USaIWRp-n9B8LTUnzQf0wIM5R_4XpOt_ZPN0ctCJQxfNFWqhaXZ6Bl2zq2R-fTCj_PQIYEXs4Q5m4T4MVhZ4nyo4vCLfVVge4yi8IEm-ybCJBJK99u59xJy_R7-ibewkj2g",
    "expires_in": 299,
    "scope": "batches.read",
    "token_type": "Bearer"
}

```

## OpenID

Token grants the client rights to act on the user's behalf. OpenID Connect extends on this to give the client more information to log the user in. On website X, Y and Z you can log in with Facebook credentials. The OpenID Connect authorization servers provide standard endpoints the client can use to retrieve basic information about the user.

# Auth Flow

The authorization server registers the resource api server as a client. The client credentials can be used to extract an access token with a subset of scopes. 

An access token can be retrieved with one or more of the available scopes registered to the client.
```
RegisteredClient
    .withId(UUID.randomUUID().toString())
    .clientId("nightfly")
    .clientSecret(passwordEncoder.encode("nightfly"))
    .redirectUri("http://127.0.0.1:8080/login/oauth2/code/nightfly")
    .redirectUri("http://127.0.0.1:8080/authorized")
    .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
    .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
    .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS) 
    .scope(OidcScopes.OPENID)   // // // //
    .scope(OidcScopes.PROFILE)  //
    .scope("batches.read")      //
    .scope("batches.write")     //  Scopes
    .scope("recipes.read")      //
    .scope("recipes.write")     //
    .scope("taps.read")         //
    .scope("taps.write")        // // // //
    .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
    .build();
```

The Client Credentials grant type enable clients to obtain an access token outside the context of a user by using `client_id` and `client_secret`
```
curl -X POST http://localhost:9000/oauth2/token \
    -u "nightfly:nightfly" \
    -d "grant_type=client_credentials&scope=batches.read batches.write openid profile"
```
