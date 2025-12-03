# 📌 Post & Comments System

[![Java](https://img.shields.io/badge/Java-17-blue)]()
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green)]()
[![Docker](https://img.shields.io/badge/Docker-Ready-informational)]()
[![GitHub Actions](https://github.com/girmamogestekle/post-comments-app/actions/workflows/ci.yml/badge.svg)]()
[![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen)]()
[![Coverage](https://img.shields.io/badge/Coverage-80%25-success)]()
[![Code Style](https://img.shields.io/badge/Code%20Style-Google-brightgreen)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)]()

---

## 📝 Overview
A real-world simulation of a social engagement backend system where users share posts and interact through comments. This project is built using enterprise-ready backend architecture, applying cloud-native, scalable, and maintainable development principles.

> Tech Stack: Java 17 ▪ Spring Boot ▪ REST API ▪ MySQL ▪ Docker ▪ Swagger ▪ GitHub Actions ▪ Cursor AI

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

### 🧪 Postman Collection
```
📁 /docs/PostAndComments.postman_collection.json
```
---

### 🚀 Run Instructions
```
mvn spring-boot:run
```
---

### 📘 Swagger UI
```
http://localhost:8080/swagger-ui.html
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


