#!/bin/bash

# Oppdater systemet
echo "Oppdaterer system..."
sudo apt-get update
sudo apt-get upgrade -y

# Installer nødvendige pakker
echo "Installerer nødvendige pakker..."
sudo apt-get install -y nginx openjdk-21-jdk unzip wget

# Opprett nødvendige mapper
echo "Oppretter mapper..."
sudo mkdir -p /opt/calculator
sudo chown azureuser:azureuser /opt/calculator

# Stopp eksisterende tjenester
echo "Stopper eksisterende tjenester..."
sudo systemctl stop calculator-backend || true
sudo systemctl disable calculator-backend || true

# Fjern gamle filer
echo "Fjerner gamle filer..."
sudo rm -f /etc/systemd/system/calculator-backend.service
sudo rm -f /opt/calculator/calculator-backend-0.0.1-SNAPSHOT.jar

# Kopier JAR fil
echo "Kopierer JAR fil..."
sudo cp calculator-backend-0.0.1-SNAPSHOT.jar /opt/calculator/
sudo chown azureuser:azureuser /opt/calculator/calculator-backend-0.0.1-SNAPSHOT.jar

# Opprett og konfigurer loggfiler
echo "Oppretter loggfiler..."
sudo touch /var/log/calculator-backend.log
sudo touch /var/log/calculator-backend.error.log
sudo chown azureuser:azureuser /var/log/calculator-backend.log
sudo chown azureuser:azureuser /var/log/calculator-backend.error.log

# Opprett systemd service for backend
echo "Oppretter systemd service for backend..."
sudo tee /etc/systemd/system/calculator-backend.service << 'EOF'
[Unit]
Description=Calculator Backend Service
After=network.target

[Service]
Type=simple
User=azureuser
WorkingDirectory=/opt/calculator
ExecStart=/usr/bin/java -jar calculator-backend-0.0.1-SNAPSHOT.jar --server.port=8080
StandardOutput=append:/var/log/calculator-backend.log
StandardError=append:/var/log/calculator-backend.error.log

[Install]
WantedBy=multi-user.target
EOF

# Start backend service
echo "Starter backend service..."
sudo systemctl daemon-reload
sudo systemctl start calculator-backend

# Vis status
echo "Viser service status..."
sudo systemctl status calculator-backend

echo "Deployment fullført!"



