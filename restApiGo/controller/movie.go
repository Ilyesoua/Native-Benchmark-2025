package controller

import (
    "math/rand"
    "net/http"
    "strconv"

    "github.com/gin-gonic/gin"
    "restapi/model"
    "restapi/service"
)

func RegisterMovieRoutes(r *gin.Engine, service *service.MovieService) {
    group := r.Group("/api/movies")
    group.POST("", func(c *gin.Context) {
        var movie model.Movie
        if err := c.ShouldBindJSON(&movie); err != nil {
            c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
            return
        }
        service.Save(&movie)
        c.JSON(http.StatusOK, movie)
    })

    group.GET("", func(c *gin.Context) {
        title := c.Query("title")
        if title != "" {
            movie, err := service.GetByTitle(title)
            if err != nil {
                c.JSON(http.StatusInternalServerError, gin.H{"error": err.Error()})
                return
            }
            c.JSON(http.StatusOK, movie)
            return
        }
        c.JSON(http.StatusOK, service.GetAll())
    })

    group.GET("/:id", func(c *gin.Context) {
        id, _ := strconv.Atoi(c.Param("id"))
        movie := service.GetByID(uint(id))
        c.JSON(http.StatusOK, movie)
    })

    group.GET("/random", func(c *gin.Context) {
        all := service.GetAll()
        if len(all) == 0 {
            c.JSON(http.StatusNotFound, gin.H{"error": "no movies"})
            return
        }
        c.JSON(http.StatusOK, all[rand.Intn(len(all))])
    })
}
