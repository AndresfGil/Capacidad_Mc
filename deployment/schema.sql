-- Script SQL para crear la base de datos y tabla de capacidades en MySQL
-- Base de datos: bootcamp-capacidad

CREATE DATABASE IF NOT EXISTS `bootcamp-capacidad` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `bootcamp-capacidad`;

CREATE TABLE IF NOT EXISTS `capacidades` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `nombre` VARCHAR(100) NOT NULL UNIQUE,
    `descripcion` VARCHAR(500) NOT NULL,
    `tecnologias_ids` JSON NOT NULL,
    INDEX `idx_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

