# Run project

## Local
The `dev` Spring profile executes the project without the need for external services, e.g., an H2 memory database. `prod` is the default profile.
```
SPRING_PROFILES_ACTIVE=dev
```

  
For running the app locally a set of key/value properties can be defined in `local.yml` to enable certain features.
Secret properties are likely to reside here and will enable the app to connect to external services.
```
# local.yml
authentication:
  enabled: true
supabase:
  url: <SUPABASE_URL>
  anon: <ANON_KEY>
  jwt_secret: <SUPABASE_SECRET>
brewfather:
  api_key: <BREWFATHER_API_KEY>
  user_id: <BREWFATHER_USER_ID>
```

## Jobs

Regular applications should be run with the profile `cron`