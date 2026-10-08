CREATE DATABASE IF NOT EXISTS dayneira_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE dayneira_db;

-- usuarios PRIMERO porque libros tiene FK a usuarios
CREATE TABLE IF NOT EXISTS usuarios (
                                        id             INT AUTO_INCREMENT PRIMARY KEY,
                                        username       VARCHAR(50)  NOT NULL UNIQUE,
    email          VARCHAR(100) NOT NULL UNIQUE,
    password_hash  VARCHAR(255) NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS libros (
                                      id             INT AUTO_INCREMENT PRIMARY KEY,
                                      usuario_id     INT NOT NULL,
                                      titulo         VARCHAR(200) NOT NULL,
    genero         VARCHAR(100),
    descripcion    TEXT,
    estado         ENUM('borrador','en_proceso','terminado','pausado') DEFAULT 'borrador',
    color_portada  VARCHAR(7) DEFAULT '#9b6dff',
    emoji_portada  VARCHAR(10) DEFAULT '📖',
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_edicion  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_libro_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
                                                       ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS ideas (
                                     id             INT AUTO_INCREMENT PRIMARY KEY,
                                     usuario_id     INT  NOT NULL,
                                     idea_original  TEXT NOT NULL,
                                     idea_expandida TEXT,
                                     fecha          TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     CONSTRAINT fk_ideas_usuario
                                     FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS piezas_avatar (
                                             id               INT AUTO_INCREMENT PRIMARY KEY,
                                             tipo             VARCHAR(30)  NOT NULL,
    nombre           VARCHAR(100) NOT NULL,
    ruta_imagen      VARCHAR(255) NOT NULL,
    es_personalizada BOOLEAN DEFAULT FALSE
    );

CREATE TABLE IF NOT EXISTS avatares_usuario (
                                                id             INT AUTO_INCREMENT PRIMARY KEY,
                                                usuario_id     INT NOT NULL,
                                                nombre_avatar  VARCHAR(100),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_avatar_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
    ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS selecciones_pieza (
                                                 id        INT AUTO_INCREMENT PRIMARY KEY,
                                                 avatar_id INT NOT NULL,
                                                 pieza_id  INT NOT NULL,
                                                 color_hex VARCHAR(7),
    CONSTRAINT fk_seleccion_avatar
    FOREIGN KEY (avatar_id) REFERENCES avatares_usuario(id)
    ON DELETE CASCADE,
    CONSTRAINT fk_seleccion_pieza
    FOREIGN KEY (pieza_id) REFERENCES piezas_avatar(id)
    ON DELETE CASCADE
    );

INSERT INTO piezas_avatar (tipo, nombre, ruta_imagen) VALUES
                                                          ('ojos',   'Ojos almendra',      'ojos/almendra.png'),
                                                          ('ojos',   'Ojos grandes',       'ojos/grandes.png'),
                                                          ('ojos',   'Ojos rasgados',      'ojos/rasgados.png'),
                                                          ('orejas', 'Orejas redondeadas', 'orejas/redondeadas.png'),
                                                          ('orejas', 'Orejas elficas',     'orejas/elficas.png'),
                                                          ('cara',   'Cara ovalada',       'cara/ovalada.png'),
                                                          ('cara',   'Cara cuadrada',      'cara/cuadrada.png'),
                                                          ('cuerpo', 'Cuerpo estilizado',  'cuerpo/estilizado.png'),
                                                          ('cuerpo', 'Cuerpo robusto',     'cuerpo/robusto.png');

SELECT 'Base de datos Dayneira creada correctamente' AS resultado;
SHOW TABLES;