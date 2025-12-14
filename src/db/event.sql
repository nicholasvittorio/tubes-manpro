CREATE TABLE EVENT (
    id_event INT PRIMARY KEY,
    jenis_event VARCHAR(100) NOT NULL,
    tanggal_event DATE NOT NULL,
    lokasi VARCHAR(150) NOT NULL,
    jumlah_undangan INT NOT NULL,

    id_klien INT,
    id_asisten INT,

    total_biaya INT,

    FOREIGN KEY (id_klien) REFERENCES KLIEN(id_klien),
    FOREIGN KEY (id_asisten) REFERENCES ASISTEN(id_asisten)
);

-- Contoh insert
INSERT INTO EVENT (id_event, jenis_event, tanggal_event, lokasi, jumlah_undangan, id_klien, id_asisten, total_biaya)
VALUES 
(1, 'Wedding', '2025-01-10', 'Gedung Serbaguna', 300, 1, 1, 25000000),
(2, 'Birthday Party', '2025-02-15', 'Hotel Jakarta', 120, 2, 1, 8000000);
