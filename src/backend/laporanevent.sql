CREATE TABLE IF NOT EXISTS Event (
    id INT PRIMARY KEY,
    jenis VARCHAR(50),
    tanggal DATE,
    lokasi VARCHAR(100),
    jumlah_undangan INT,
    nama_klien VARCHAR(50),
    nama_asisten VARCHAR(50),
    total_biaya INT
);

SELECT id, jenis, tanggal, lokasi, jumlah_undangan, nama_klien, nama_asisten, total_biaya
FROM Event
ORDER BY tanggal DESC;