use actix_web::{web, get, post, HttpResponse, Responder};
use crate::{repository::{MovieRepository, DbPool}, service::MovieService, model::Movie};
use rand::seq::SliceRandom;
use rand::thread_rng;


#[post("/api/movies")]
async fn create_movie(
    movie: web::Json<Movie>,
    pool: web::Data<DbPool>,
) -> impl Responder {
    let pool = pool.clone();
    let movie_data = movie.into_inner();

    let result = web::block(move || {
        let mut conn = pool.get().expect("Failed to get DB connection");
        let mut repo = MovieRepository { conn: &mut conn };
        repo.save(&movie_data)
    })
    .await;

    match result {
        Ok(_) => HttpResponse::Ok().json(serde_json::json!({
            "message": "Movie saved successfully"
        })),
        Err(err) => {
            eprintln!("Erreur lors de la sauvegarde : {:?}", err);
            HttpResponse::InternalServerError().json(serde_json::json!({
                "error": "Failed to save movie"
            }))
        }
    }
}


#[get("/api/movies")]
async fn get_movies(
    query: web::Query<std::collections::HashMap<String, String>>,
    service: web::Data<MovieService>,
) -> impl Responder {
    if let Some(query_title) = query.get("title") {
        match service.get_by_title(query_title).await {
            Ok(movie) => HttpResponse::Ok().json(movie), // Succès
            Err(e) => HttpResponse::InternalServerError().json(serde_json::json!({
                "error": format!("Failed to fetch movie: {}", e)
            })),
        }
    } else {
        HttpResponse::BadRequest().json(serde_json::json!({
            "error": "Query parameter 'title' is required"
        }))
    }
}


#[get("/api/random")]
async fn random_movie(pool: web::Data<DbPool>) -> impl Responder {
    let pool = pool.clone();

    let result = web::block(move || {
        let mut conn = pool.get().expect("Failed to get DB connection");
        let mut repo = MovieRepository { conn: &mut conn };
        let all_movies = repo.find_all().unwrap_or_default();

        let random = all_movies.choose(&mut thread_rng()).cloned();
        Ok::<_, diesel::result::Error>(random)
    })
    .await;

    match result {
        Ok(Ok(Some(movie))) => HttpResponse::Ok().json(movie),
        Ok(Ok(None)) => HttpResponse::NotFound().json(serde_json::json!({
            "error": "No movie found"
        })),
        Ok(Err(diesel_error)) => {
            eprintln!("Erreur Diesel : {:?}", diesel_error);
            HttpResponse::InternalServerError().json(serde_json::json!({
                "error": "Database error"
            }))
        }
        Err(blocking_err) => {
            eprintln!("Erreur web::block : {:?}", blocking_err);
            HttpResponse::InternalServerError().json(serde_json::json!({
                "error": "Internal server error"
            }))
        }
    }
}


#[get("/api/movies/{id}")]
async fn get_movie_by_id(
    path: web::Path<i32>,
    pool: web::Data<DbPool>,
) -> impl Responder {
    let pool = pool.clone();
    let movie_id = path.into_inner();

    let result = web::block(move || {
        let mut conn = pool.get().expect("Failed to get DB connection");
        let mut repo = MovieRepository { conn: &mut conn };
        repo.find_by_id(movie_id)
    })
    .await;

    match result {
        Ok(Ok(movie)) => HttpResponse::Ok().json(movie),
        Ok(Err(err)) => {
            eprintln!("Erreur Diesel : {:?}", err);
            HttpResponse::NotFound().json(serde_json::json!({
                "error": "Movie not found"
            }))
        }
        Err(blocking_err) => {
            eprintln!("Erreur web::block : {:?}", blocking_err);
            HttpResponse::InternalServerError().json(serde_json::json!({
                "error": "Internal server error"
            }))
        }
    }
}
