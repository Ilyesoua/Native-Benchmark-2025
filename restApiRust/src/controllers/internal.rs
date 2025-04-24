use actix_web::{get, HttpResponse};

#[get("/api/internal/process")]
pub async fn process() -> HttpResponse {
    let mut sum: u64 = 0;
    for i in 0..500_000_000 {
        sum += i;
    }
    HttpResponse::Ok().json(serde_json::json!({ "result": sum }))
}
