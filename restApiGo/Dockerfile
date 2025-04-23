FROM golang:1.20

WORKDIR /app

# Copie tout le code source
COPY . .

# Par défaut, le container build et lance l'app
CMD ["bash", "-c", "go mod tidy && go build -o restApi && ./restApi"]
