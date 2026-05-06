-- =====================================================
-- WeatherWise.ai - Database Setup Script
-- Import ke MySQL: mysql -u root -p < weatherwise_db.sql
-- Atau import via phpMyAdmin / MySQL Workbench
-- =====================================================

-- Buat & gunakan database
CREATE DATABASE IF NOT EXISTS weatherwise_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE weatherwise_db;

-- =====================================================
-- Tabel: users
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    username    VARCHAR(50)     NOT NULL UNIQUE,
    email       VARCHAR(100)    NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,           -- BCrypt hashed
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Tabel: saved_locations
-- =====================================================
CREATE TABLE IF NOT EXISTS saved_locations (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    user_id     BIGINT          NOT NULL,
    name        VARCHAR(100)    NOT NULL,
    country     VARCHAR(10)     DEFAULT NULL,
    lat         DOUBLE          NOT NULL,
    lon         DOUBLE          NOT NULL,
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_location_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Tabel: search_history
-- =====================================================
CREATE TABLE IF NOT EXISTS search_history (
    id          BIGINT          NOT NULL AUTO_INCREMENT,
    user_id     BIGINT          NOT NULL,
    city_name   VARCHAR(100)    NOT NULL,
    country     VARCHAR(10)     DEFAULT NULL,
    searched_at DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_history_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Index untuk performa query
-- =====================================================
CREATE INDEX idx_saved_locations_user_id ON saved_locations(user_id);
CREATE INDEX idx_search_history_user_id  ON search_history(user_id);
CREATE INDEX idx_search_history_searched ON search_history(searched_at DESC);

-- =====================================================
-- Data contoh (opsional - bisa dihapus)
-- Password: password123 (BCrypt hash)
-- =====================================================
INSERT INTO users (username, email, password) VALUES
('demo_user', 'demo@weatherwise.ai', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LPVDyptJL76');

INSERT INTO saved_locations (user_id, name, country, lat, lon) VALUES
(1, 'Jakarta', 'ID', -6.2088, 106.8456),
(1, 'Surabaya', 'ID', -7.2575, 112.7521);

INSERT INTO search_history (user_id, city_name, country) VALUES
(1, 'Jakarta', 'ID'),
(1, 'Bali', 'ID'),
(1, 'Surabaya', 'ID');

-- =====================================================
-- Verifikasi hasil import
-- =====================================================
SELECT 'Database weatherwise_db berhasil dibuat!' AS status;
SELECT TABLE_NAME, TABLE_ROWS
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'weatherwise_db';
