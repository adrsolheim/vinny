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

## OpenID

Token grants the client rights to act on the user's behalf. OpenID Connect extends on this to give the client more information to log the user in. On website X, Y and Z you can log in with Facebook credentials. The OpenID Connect authorization servers provide standard endpoints the client can use to retrieve basic information about the user.
