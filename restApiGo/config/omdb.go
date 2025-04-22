package config

import (
	"os"
	"log"
)

type OmdbConfig struct {
	OmdbApiUrl string
	OmdbApiKey string
}

func NewOmdbConfig() *OmdbConfig {
	// Lire les variables d'environnement
	omdbApiUrl := os.Getenv("OMDB_API_URL")
	omdbApiKey := os.Getenv("OMDB_API_KEY")

	// Vérifier que les variables d'environnement sont bien définies
	if omdbApiUrl == "" || omdbApiKey == "" {
		log.Fatal("OMDB_API_URL or OMDB_API_KEY not set")
	}

	return &OmdbConfig{
		OmdbApiUrl: omdbApiUrl,
		OmdbApiKey: omdbApiKey,
	}
}

