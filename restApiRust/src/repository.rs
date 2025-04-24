use diesel::pg::PgConnection;
use diesel::r2d2::{self, ConnectionManager};
use diesel::prelude::*;
use crate::model::Movie;
use crate::schema::movies::dsl::*;

pub type DbPool = r2d2::Pool<ConnectionManager<PgConnection>>;

pub struct MovieRepository<'a> {
    pub conn: &'a mut PgConnection,
}

impl<'a> MovieRepository<'a> {
    pub fn save(&mut self, movie: &Movie) -> QueryResult<usize> {
        diesel::insert_into(movies).values(movie).execute(self.conn)
    }

    pub fn find_all(&mut self) -> QueryResult<Vec<Movie>> {
        movies.load::<Movie>(self.conn)
    }

    pub fn find_by_id(&mut self, movie_id: i32) -> QueryResult<Movie> {
        movies.find(movie_id).first::<Movie>(self.conn)
    }
}
