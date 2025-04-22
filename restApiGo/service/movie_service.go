package service

import (
    "encoding/json"
    "fmt"
    "io"
    "net/http"
    "os"
    "restapi/model"
    "restapi/repository"
)

type MovieService struct {
    repo *repository.MovieRepository
}

func NewMovieService(repo *repository.MovieRepository) *MovieService {
    return &MovieService{repo}
}

func (s *MovieService) Save(movie *model.Movie) error {
    return s.repo.Save(movie)
}

func (s *MovieService) GetAll() []model.Movie {
    return s.repo.FindAll()
}

func (s *MovieService) GetByID(id uint) *model.Movie {
    return s.repo.FindByID(id)
}

func (s *MovieService) GetByTitle(title string) (*model.Movie, error) {
    apiKey := os.Getenv("OMDB_API_KEY")
    url := fmt.Sprintf("https://www.omdbapi.com/?apikey=%s&t=%s", apiKey, title)

    resp, err := http.Get(url)
    if err != nil {
        return nil, err
    }
    defer resp.Body.Close()

    body, _ := io.ReadAll(resp.Body)
    var movie model.Movie
    err = json.Unmarshal(body, &movie)
    if err != nil {
        return nil, err
    }
    return &movie, nil
}
