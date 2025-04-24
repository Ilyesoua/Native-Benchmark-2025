use diesel::Queryable;
use diesel::Insertable;
use serde::{Deserialize, Serialize};

#[derive(Clone, Debug, Serialize, Deserialize, Queryable, Insertable)]
#[diesel(table_name = crate::schema::movies)]
pub struct Movie {
    pub id: i32,
    pub title: String,
    pub plot: Option<String>,
    pub release_date: Option<String>,
    pub poster: Option<String>,
}
