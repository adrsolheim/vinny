# docker compose local build
rm -rf */target
cd nightfly && mvn clean install && cd ..
cd gatekeeper && mvn clean install && cd ..

docker compose down && docker compose build && docker compose up
