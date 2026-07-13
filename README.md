# Filmlisten-Manager

A Java/Spring Boot application built as part of a university Software Engineering practical (SEP). It scrapes movie data from IMDb, lets users browse and search it locally with thumbnails and categories, organize titles into personal lists (e.g. "Plan to Watch", "Completed"), rate movies, and chat with other users in real time. The client is a JavaFX desktop application.

Developed by a 5-person team over one semester, iterating across several delivery cycles (specification, prototyping, implementation).

## What it does

- Scrapes and cleans movie data from IMDb and persists it locally
- Account system with personal watchlists and ratings
- Search/filter across the local movie catalog (thumbnails, categories, metadata)
- Real-time chat between users

## My contribution

- IMDb scraping (via HtmlUnit) and data cleaning/persistence into a MySQL database using Spring Data JPA
- Chat functionality (JavaFX client and server-side WebSocket/STOMP configuration) for real-time communication between users
- Code reviews and debugging support for less experienced teammates

## Tech stack

- Java, Spring Boot
- Spring Data JPA + MySQL
- WebSocket / STOMP (Spring Messaging) for real-time chat
- HtmlUnit for scraping
- JavaFX (desktop client) + Jackson (JSON serialization)

## Running it

Three Gradle modules: `server` (Spring Boot backend), `client` (JavaFX desktop app), `ChatClient` (standalone WebSocket test client). The server expects a running MySQL instance configured via `server/src/main/resources/application.properties`.

## Note on commit history

This is a copy of a university group project, originally hosted on our university's internal GitLab. Since the original history included commits from five team members, the history here has been consolidated into a single commit under my account rather than publishing my teammates' names without their input. The code and architecture reflect the full team's work; the "My contribution" section above describes specifically what I worked on.

---
*This README was drafted with the help of Claude (Anthropic), based on a review of the codebase and my own descriptions of the project.*
