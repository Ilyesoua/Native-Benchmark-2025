package controller

import (
    "github.com/gin-gonic/gin"
    "net/http"
)

func RegisterInternalRoutes(r *gin.Engine) {
    r.GET("/api/internal/process", func(c *gin.Context) {
        var sum uint64 = 0
        for i := 0; i < 500_000_000; i++ {
            sum += uint64(i)
        }
        c.JSON(http.StatusOK, gin.H{"result": sum})
    })
}
