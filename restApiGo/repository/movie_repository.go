package repository

import (
    "restapi/model"
    "gorm.io/gorm"
)

type MovieRepository struct {
    db *gorm.DB
}

func NewMovieRepository(db *gorm.DB) *MovieRepository {
    return &MovieRepository{db}
}

func (r *MovieRepository) Save(movie *model.Movie) error {
    return r.db.Create(movie).Error
}

func (r *MovieRepository) FindAll() []model.Movie {
    var movies []model.Movie
    r.db.Find(&movies)
    return movies
}

func (r *MovieRepository) FindByID(id uint) *model.Movie {
    var movie model.Movie
    r.db.First(&movie, id)
    return &movie
}
