// src/schema.rs
diesel::table! {
    movies (id) {
        id -> Integer,
        title -> Varchar,
        plot -> Nullable<Varchar>,
        release_date -> Nullable<Varchar>,
        poster -> Nullable<Varchar>,
    }
}
