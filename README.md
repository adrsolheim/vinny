## Application
```
                     127.0.0.1:9090                      
                     +-------------+                     
                     |    gate     |                     
                     |  (Gateway)  |                     
                     +------+------+                     
                            |                            
                            |                            
       +--------------------+--------------------+       
       |                    |                    |       
+------v------+      +------v------+      +------v------+
|  gatekeeper |      |   nightfly  |      |  sunflower  |
|    (Auth)   |      |    (API)    |      |  (Frontend) |
+-------------+      +-------------+      +-------------+
```

### Authorization Code Flow
Client `simple_client` access api resources from the resource server `nightfly` *on behalf of a user*. In order to access resources the client first authenticates itself to an authorization server `gatekeeper`. The user logs in to the authorization server, grants the client access to user resources and finally an access token is granted to the client upon completing the **authorization code flow**.

*Note: Use `http://127.0.0.1` and not `http://localhost` in order for the configured redirect uris to match the uri from the browser*
