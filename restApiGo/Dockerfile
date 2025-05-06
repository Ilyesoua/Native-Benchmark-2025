FROM golang:1.20

WORKDIR /app

# Copie tout le code source
COPY . .

# Par défaut, le container build et lance l'app
CMD ["bash", "-c", "START=$(date +%s); go mod tidy && go build -o restApi && echo ✅ Build successful; END=$(date +%s); echo ⏱️ Build took $((END - START))s; ./restApi"]

