CREATE TABLE ASISTEN (
    id_asisten INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    nama_asisten VARCHAR(100)
);

-- Contoh insert
INSERT INTO ASISTEN (id_asisten, username, password, nama_asisten)
VALUES
(1, 'asisten', 'asisten123', 'Asisten Satu');

CREATE TABLE ASISTEN (
    id_asisten INT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    nama_asisten VARCHAR(100)
);

-- Contoh insert
INSERT INTO ASISTEN (id_asisten, username, password, nama_asisten)
VALUES
(1, 'asisten', 'asisten123', 'Asisten Satu');
