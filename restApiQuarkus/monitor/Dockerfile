FROM ubuntu:22.04

RUN apt-get update && \
    apt-get install -y procps bc bash

WORKDIR /app

COPY monitor.sh .

RUN chmod +x monitor.sh

ENTRYPOINT ["bash", "-c", "./monitor.sh"]
