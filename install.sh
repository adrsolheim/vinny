# docker compose local build
rm -rf ./nightfly/target
rm -rf ./gatekeeper/target

mvn -f ./nightfly/pom.xml clean install
mvn -f ./gatekeeper/pom.xml clean install
