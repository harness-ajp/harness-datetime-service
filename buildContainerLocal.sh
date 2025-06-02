#!/bin/bash
./gradlew clean bootJar
podman build -t datetime-service .
podman run -p 8080:8080 datetime-service
