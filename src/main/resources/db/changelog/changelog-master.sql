--liquibase formatted sql

--changeset dyagilev:1
--comment: creating db

CREATE DATABASE IF NOT EXISTS `crudapp`;
USE `crudapp`;
SET foreign_key_checks = 0;
DROP TABLE IF EXISTS `labels`;
CREATE TABLE `labels` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(100) DEFAULT NULL,
                          `status` varchar(30) DEFAULT NULL,
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `writers`;
CREATE TABLE `writers` (
                           `id` int NOT NULL AUTO_INCREMENT,
                           `first_name` varchar(100) DEFAULT NULL,
                           `last_name` varchar(100) DEFAULT NULL,
                           `status` varchar(30) DEFAULT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `posts`;
CREATE TABLE `posts` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `content` varchar(100) DEFAULT NULL,
                         `created` varchar(100) DEFAULT NULL,
                         `updated` varchar(100) DEFAULT NULL,
                         `post_status` varchar(30) DEFAULT NULL,
                         `writer_id` int DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `writer_id` (`writer_id`),
                         CONSTRAINT `posts_ibfk_1` FOREIGN KEY (`writer_id`) REFERENCES `writers` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `post_labels`;
CREATE TABLE `post_labels` (
                               `post_id` int NOT NULL,
                               `label_id` int NOT NULL,
                               UNIQUE KEY `post_id` (`post_id`,`label_id`),
                               KEY `label_id` (`label_id`),
                               CONSTRAINT `post_labels_ibfk_1` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
                               CONSTRAINT `post_labels_ibfk_2` FOREIGN KEY (`label_id`) REFERENCES `labels` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
DROP TABLE IF EXISTS `writer_posts`;
CREATE TABLE `writer_posts` (
                                `writer_id` int NOT NULL,
                                `post_id` int NOT NULL,
                                UNIQUE KEY `writer_id` (`writer_id`,`post_id`),
                                KEY `post_id` (`post_id`),
                                CONSTRAINT `writer_posts_ibfk_1` FOREIGN KEY (`writer_id`) REFERENCES `writers` (`id`),
                                CONSTRAINT `writer_posts_ibfk_2` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
SET foreign_key_checks = 1;