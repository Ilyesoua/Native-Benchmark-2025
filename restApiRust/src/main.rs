use actix_web::{App, HttpServer, web};
use dotenv::dotenv;
use diesel::r2d2::{self, ConnectionManager};
use diesel::PgConnection;
use std::sync::Arc;
use std::time::Instant;

use crate::service::MovieService;

mod model;
mod repository;
mod service;
mod controllers;
mod schema;

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let startup_start = Instant::now(); // ⏱️ début chrono

    dotenv().ok();
    let movie_service = Arc::new(MovieService::new());

    let database_url = std::env::var("DATABASE_URL").expect("DATABASE_URL must be set");
    let manager = ConnectionManager::<PgConnection>::new(database_url);
    let pool = r2d2::Pool::builder()
        .build(manager)
        .expect("Failed to create pool.");

    let server = HttpServer::new(move || {
        App::new()
            .app_data(web::Data::new(pool.clone()))
            .app_data(web::Data::from(movie_service.clone()))
            .service(controllers::health::health)
            .service(controllers::internal::process)
            .service(controllers::movies::create_movie)
            .service(controllers::movies::get_movies)
            .service(controllers::movies::get_movie_by_id)
            .service(controllers::movies::random_movie)
    })
    .bind(("0.0.0.0", 8080))?;

    let startup_duration = startup_start.elapsed();
    println!("🚀 Server startup completed in {:.2?}", startup_duration);

    server.run().await
}
