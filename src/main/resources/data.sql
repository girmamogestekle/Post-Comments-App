-- Sample data for PostEntity entity
INSERT INTO post (title, created_at, updated_at) VALUES
('Introduction to Spring Boot', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Understanding JPA and Hibernate', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RESTFUL API Best Practices', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Database Design Patterns', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Testing in Spring Boot Applications', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Sample data for PostDetailEntity entity
-- Note: PostDetailEntity uses @MapsId, so it shares the same ID as PostEntity
INSERT INTO post_detail (id, description, created_at, updated_at) VALUES
(1, 'Spring Boot is a powerful framework that simplifies the development of Spring applications. It provides auto-configuration, embedded servers, and production-ready features out of the box. This post covers the basics of getting started with Spring Boot, including project setup, configuration, and building your first RESTful API.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'JPA (Java Persistence API) and Hibernate are essential tools for working with databases in Java applications. Learn how to map entities, handle relationships, use repositories, and perform queries. Understanding these concepts is crucial for building robust data persistence layers.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'RESTful APIs are the backbone of modern web applications. This post explores best practices for designing REST APIs, including proper HTTP methods, status codes, resource naming conventions, versioning strategies, and API documentation. Follow these guidelines to create APIs that are intuitive, scalable, and maintainable.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Database design patterns help solve common data modeling challenges. This post covers essential patterns such as normalization, denormalization, single-table inheritance, class-table inheritance, and aggregate patterns. Understanding these patterns will help you design efficient and maintainable database schemas.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Testing is crucial for ensuring the quality and reliability of Spring Boot applications. Learn about different testing strategies including unit tests, integration tests, and end-to-end tests. Explore testing frameworks like JUnit, Mockito, and Spring Boot Test, and discover best practices for writing effective test cases that cover your application thoroughly.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
