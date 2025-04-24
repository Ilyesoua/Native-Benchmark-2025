FROM rust:1.83

WORKDIR /app

RUN apt-get update && \
    apt-get install -y libpq-dev && \
    cargo install diesel_cli --no-default-features --features postgres

COPY . .

EXPOSE 8080

CMD ["bash", "-c", "\
    sleep 3 && \
    diesel setup && \
    diesel migration generate create_movies && \
    MIGRATION_DIR=$(find migrations -type d -name '*create_movies' | head -n 1) && \
    echo 'CREATE TABLE IF NOT EXISTS movies ( \
        id SERIAL PRIMARY KEY, \
        title VARCHAR NOT NULL, \
        plot VARCHAR, \
        release_date VARCHAR, \
        poster VARCHAR \
    );' > $MIGRATION_DIR/up.sql && \
    echo 'DROP TABLE IF EXISTS movies;' > $MIGRATION_DIR/down.sql && \
    echo 'Contenu de up.sql :' && cat $MIGRATION_DIR/up.sql && \
    echo 'Contenu de down.sql :' && cat $MIGRATION_DIR/down.sql && \
    diesel migration run && \
    cargo build --release && \
    ./target/release/restapi"]

