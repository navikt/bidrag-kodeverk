FROM gcr.io/distroless/java21
LABEL maintainer="Team Bidrag" \
      email="bidrag@nav.no"

COPY ./target/bidrag-kodeverk-*.jar app.jar

ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"
ENV SPRING_PROFILES_ACTIVE=nais

EXPOSE 8080
