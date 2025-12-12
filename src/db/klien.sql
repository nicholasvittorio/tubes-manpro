CREATE TABLE KLIEN (
    id_klien INT PRIMARY KEY,
    nama_klien VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    umur INT,
    alamat VARCHAR(200)
);

-- Contoh insert
INSERT INTO KLIEN (id_klien, nama_klien, email, umur, alamat)
VALUES
(1, 'Nicholas', 'nicholas@email.com', 21, 'Bandung'),
(2, 'Davin', 'davin@email.com', 20, 'Jakarta');
