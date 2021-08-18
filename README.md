# Callout Lambda

This module deploys the Pokémon app to AWS Lambda using the Amazon Java 11 runtime.

## To deploy on AWS

- Install and configure Pulumi with your AWS credentials
- Run `./deploy.sh`
- Hit `http://<pulumi output url>/b`

## To destroy on AWS

- Run `./destroy.sh`

## To Test locally

- Run `./gradlew run`
- Hit `http://localhost:7000/b`
