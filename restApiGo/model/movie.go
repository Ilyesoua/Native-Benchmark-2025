package model

type Movie struct {
    ID          uint   `json:"id" gorm:"primaryKey"`
    Title       string `json:"title"`
    Plot        string `json:"plot"`
    ReleaseDate string `json:"releaseDate"`
    Poster      string `json:"poster"`
}
