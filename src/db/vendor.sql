CREATE TABLE VENDOR (
    id_vendor INT PRIMARY KEY,
    nama_vendor VARCHAR(100) NOT NULL,
    jenis_layanan VARCHAR(100) NOT NULL,
    kontak VARCHAR(100),
    harga INT NOT NULL
);

-- Contoh insert
INSERT INTO VENDOR (id_vendor, nama_vendor, jenis_layanan, kontak, harga)
VALUES
(1, 'Vendor Dekorasi A', 'Dekorasi', '081234567890', 5000000),
(2, 'Vendor Catering B', 'Catering', '088888777666', 15000000);
