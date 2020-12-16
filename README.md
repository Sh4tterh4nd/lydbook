# LydBook

Lydbook is a self hosted Audiobook server.

Features:

- Save listening progress
- Share audiobooks with other selected users

**Right now only CBR encoded mp3 files are supported.**

To convert mp3 files with ffmpeg and not loose metadata or cover the following command can be used:

`ffmpeg -i input.mp3 -c:v copy -codec:a libmp3lame -b:a 64k -map_metadata 0 -map_metadata 0:s:0 -id3v2_version 3 output.mp3`

The easiest way to deploy Lydbook is with docker, below is a docker-compose example.

Optional configurations:
`DATABASE_PORT` default `5432`
`DATABASE_NAME` default `audiobook`
`DATABASE_USER` default `book`

```yml
version: '3.3'
services:
  db:
    image: postgres:12
    environment:
      POSTGRES_DB: audiobook
      POSTGRES_PASSWORD: password
      POSTGRES_USER: book
    volumes:
     - postgresData:/var/lib/postgresql/data
    networks:
     - network
  lydbook:
    image: shatterhand/lydbook:latest
    environment:
      DATABASE_PASSWORD: password
      POSTGRES_HOST: db
    ports:
     - 8080:8080
    volumes:
     - bookData:/data
    networks:
     - network
networks:
  network:
volumes:
  postgresData:
    driver: local
  bookData:
    driver: local

```
