package main

import (
    "github.com/gin-gonic/gin"
    "gorm.io/driver/postgres"
    "gorm.io/gorm"
    "restapi/controller"
    "restapi/model"
    "restapi/repository"
    "restapi/service"
)

func main() {
    dsn := "host=postgres user=user password=password dbname=moviesdb port=5432 sslmode=disable"
    db, err := gorm.Open(postgres.Open(dsn), &gorm.Config{})
    if err != nil {
        panic("❌ Failed to connect to database: " + err.Error())
    }

    db.AutoMigrate(&model.Movie{})

    movieRepo := repository.NewMovieRepository(db)
    movieService := service.NewMovieService(movieRepo)

    r := gin.Default()

    controller.RegisterMovieRoutes(r, movieService)
    controller.RegisterHealthRoutes(r)
    controller.RegisterInternalRoutes(r)

    r.Run(":8080")
}
