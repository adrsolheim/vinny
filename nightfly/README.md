# Run project

## Local
The `dev` Spring profile executes the project without the need for external services, e.g., an H2 memory database. `prod` is the default profile.
```
SPRING_PROFILES_ACTIVE=dev
```

  
For running the app locally a set of key/value properties can be defined in `local.yml` to enable certain features.
Secret properties are likely to reside here.