FROM bellsoft.azurecr.io/liberica-openjdk-debian:25 AS build

WORKDIR /sandcastle
COPY . /sandcastle
RUN chmod 755 ./gradlew
# build
RUN ./gradlew :sandcastle-app-web:build -x test --info

FROM bellsoft.azurecr.io/liberica-openjdk-debian:25

WORKDIR /opt/sandcastle

RUN groupadd -r sandcastle && useradd -r -g sandcastle sandcastle

COPY --from=build /sandcastle/sandcastle-app-web/build/libs/sandcastle-app-web.jar ./sandcastle-app-web.jar

RUN chown -R sandcastle:sandcastle /opt/sandcastle

USER sandcastle

ENTRYPOINT ["java", "-jar", "-XX:+UseContainerSupport", "/opt/sandcastle/sandcastle-app-web.jar"]

EXPOSE 8080