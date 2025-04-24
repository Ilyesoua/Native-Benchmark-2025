use reqwest::Client;
use std::env;
use serde_json::Value;

#[derive(Clone)]
pub struct MovieService {
    pub client: Client,
}

impl MovieService {
    pub fn new() -> Self {
        Self { client: Client::new() }
    }

    pub async fn get_by_title(&self, title: &str) -> Result<Value, Box<dyn std::error::Error>> {
        let api_key = env::var("OMDB_API_KEY").expect("OMDB_API_KEY must be set");
        let url = format!("https://www.omdbapi.com/?apikey={}&t={}", api_key, title);

        let res = self.client.get(&url).send().await?.json::<Value>().await?;

        Ok(res)
    }
}
