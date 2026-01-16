# Instalacja (jednorazowo)
brew install spring-io/tap/spring-boot

# Generowanie projektu

spring init \
  --type=maven-project \
  --java-version=25 \
  --boot-version=4.0.1 \
  --dependencies=web,devtools \
  --group-id=com.example \
  --artifact-id=ex01 \
  --name=Ex01 \
  --package-name=com.example.ex01 \
ex01

#Odpalenie projektu
./mvnw spring-boot:run