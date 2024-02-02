# Authorization Server

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


