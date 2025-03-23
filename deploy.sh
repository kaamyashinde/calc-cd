#!/bin/bash

# Build the application
echo "Building the application..."
./mvnw clean package -DskipTests

# Deploy to Heroku
echo "Deploying to Heroku..."
heroku deploy:jar target/calculator-backend-0.0.1-SNAPSHOT.jar --app calculator-cd-backend

echo "Deployment completed!"



