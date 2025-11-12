-- Migración Flyway V1: Crear tabla de capacidades
-- Base de datos: bootcamp-capacidad

CREATE TABLE IF NOT EXISTS `capacidades` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `nombre` VARCHAR(100) NOT NULL UNIQUE,
    `descripcion` VARCHAR(500) NOT NULL,
    `tecnologias_ids` JSON NOT NULL,
    `activa` BOOLEAN NOT NULL DEFAULT TRUE,
    `fecha_modificacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX `idx_nombre` (`nombre`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


