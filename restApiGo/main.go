package main

import (
    "github.com/gin-gonic/gin"
    "gorm.io/driver/sqlite"
    "gorm.io/gorm"

    "restapi/controller"
    "restapi/model"
    "restapi/repository"
    "restapi/service"
)

func main() {
    db, _ := gorm.Open(sqlite.Open("movies.db"), &gorm.Config{})
    db.AutoMigrate(&model.Movie{})

    movieRepo := repository.NewMovieRepository(db)
    movieService := service.NewMovieService(movieRepo)

    r := gin.Default()

    controller.RegisterMovieRoutes(r, movieService)
    controller.RegisterHealthRoutes(r)
    controller.RegisterInternalRoutes(r)

    r.Run(":8080")
}
