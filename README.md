# 📌 Post & Comments System

[![Java](https://img.shields.io/badge/Java-17-blue)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-Enabled-brightgreen)](https://spring.io/projects/spring-ai)
[![Swagger UI](https://img.shields.io/badge/Docs-Swagger_UI-blue)](https://girmamogestekle.github.io/Post-Comments-App/)
[![GitHub Action](https://img.shields.io/github/actions/workflow/status/girmamogestekle/Post-Comments-App/workflow.yml?branch=main&label=Build)](https://github.com/girmamogestekle/Post-Comments-App/actions)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=post-comments-app&metric=alert_status)](https://sonarcloud.io/project/overview?id=post-comments-app)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=post-comments-app&metric=coverage)](https://sonarcloud.io/summary/new_code?id=post-comments-app)
[![Docker](https://img.shields.io/badge/Docker-Ready-informational)](https://hub.docker.com/r/gmtekle/post-comments-app)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/girmamogestekle/Post-Comments-App/blob/master/LICENSE)

---

## 📝 Overview
A real-world simulation of a social engagement backend system where users share posts and interact through comments. This project is built using enterprise-ready backend architecture, applying cloud-native, scalable, and maintainable development principles.

> Tech Stack: Java 17 ▪ Spring Boot ▪ REST API ▪ SonarQube ▪ MySQL ▪ Docker ▪ Swagger ▪ GitHub Actions ▪ Cursor AI

---

## ✨ Features

| Category | Capability |
|---------|------------|
| Posts | Create, update, delete, list posts |
| Comments | Full comment lifecycle |
| API Style | Clean, versioned RESTful endpoints |
| Documentation | Live Swagger UI |
| Logging | Structured & centralized logs |
| Error Handling | Global exception patterns with clear responses |
| DB Design | Relational model, Post → Comments (1:N) |

---

## 🏗️ Architecture

> 🔥 This project features **multiple diagram representations** for professional clarity.

---

### 🎯 Primary Architecture Diagram

```
📱 Client
   │
   ▼
🌐 Controllers → ⚙️ Services → 🗂️ Repositories → 🗄️ MySQL
       ↘ 🔍 Logging
       ↘ ✔ Validation
       ↘ 🚨 Exception Handling
       ↘ 📘 API Documentation
```
---

### 🗄️ Database Schema

```
Post: id, title, content, author_id, timestamps
Comment: id, post_id(FK), content, author_id, timestamps
```
---

### 📮 Public Postman Collection  
```
https://run.pstmn.io/button.svg)](https://ethio-equb.postman.co/workspace/My-Workspace~0eb138ab-d434-42af-8f7a-6b51a5bab95e/folder/25853967-939f8ea3-ae8a-4dcb-9111-464e2846b549?action=share&creator=25853967&ctx=documentation&active-environment=25853967-6115b367-1a32-4138-9a65-4c5fed3caa68
```
---

### 🚀 Run Instructions
```
mvn spring-boot:run
```
---

### 📘 Swagger UI
```
https://girmamogestekle.github.io/Post-Comments-App/  
```
---

### 🔮 Future Enhancements
```
| Feature                            | Status  |
| ---------------------------------- | ------- |
| JWT Authentication                 | Soon    |
| Pagination                         | Soon    |
| AWS Cloud Deployment               | Planned |
| Distributed Logging (Grafana/Loki) | Planned |
| TestContainers                     | Planned |
```
---

### 🤖 AI-Powered Enhancements (Spring AI)
```
## 🤖 AI-Powered Enhancements (Spring AI)

This backend integrates Spring AI and OpenAI to provide intelligent features:

| Feature             | Description                          | Endpoint                           |
|---------------------|--------------------------------------|------------------------------------|
| Post AI Explanation | Returns simplified summary using LLM | GET /api/posts/{id}?includeAi=true |

> Powered by Spring AI + OpenAI  
> Easily extendable to enrich Comments, Tags, and Post Details
```
---

### ☁️ Cloud Roadmap Architecture (Future AWS Expansion)
```
flowchart LR
    User --> API[REST API - Spring Boot]
    API --> DB[(MySQL RDS)]
    API --> Logs[CloudWatch / Grafana]
    API --> Storage[(AWS S3) for media]
    API --> Queue[SQS - Async Event Processing]
    API --> Auth[Cognito / JWT Auth Service]
```
---

### 👨‍💻 Author
```
Girma Moges Teklemariam
Software Engineer
🔗 LinkedIn: https://www.linkedin.com/in/girmamogestekle
```


