🎯 TUBES MIBD – Sistem EO

Proyek ini dibuat sebagai simulasi pengembangan Event Organizer System menggunakan Java, Git, dan Scrum.
Tujuan utama repositori ini adalah menunjukkan bahwa tim mampu bekerja kolaboratif menggunakan branch, commit, dan pull request sesuai peran masing-masing.

---

📌 Struktur Sprint (berdasarkan WBS Sistem EO)

Sprint 1

- F2.1 Login User
- F2.2 Basisdata Vendor
- F2.3 Basisdata Asisten

Sprint 2

- F3.1 Events (List)
- F3.2 Pilih Vendor
- F3.3 Harga Deal
- F3.4 Feedback

Sprint 3

- F4.1 Budgeting
- F4.2 Laporan Event
- F4.3 Validasi

Sprint 4

- F5.1 Analisis Kerja
- F5.2 Event Terdekat
- F5.3 Review

Sprint 5

- F6.1 Integrasi
- F6.2 Deployment
- F6.3 Training

---

📁 Pembagian File Java & Peran Git

Frontend Developer

Nama| Role| File
Gib| FD| "Ui_Asisten1.java"
Kris| FD| "Ui_Pemilik.java"

Backend Developer

Nama| Role| File
Davin| PM + BD2| "DatabaseConnection.java", "Login.java", "Manajemen_Asisten.java", "Session.java", "TubesMIBD.java"
Yos| BD1| "Asisten.java", "Event.java", "Klien.java", "Vendor.java"

---

🌿 Nama Branch Git

- BD1 (Yos) → "database"
Berisi class yang berkaitan dengan struktur data & query database.

- BD2 (Davin) → "controller"
Berisi login, session, koneksi database, dan manajemen data.

- FD (Gib & Kris)

   - "fd-ui-asisten"
   - "fd-ui-pemilik"

---

✨ Deskripsi Singkat Tugas Git

Proyek ini menekankan penggunaan Git sebagai workflow kolaboratif:

1. Setiap developer bekerja pada branch masing-masing.
2. Perubahan dilakukan melalui commit kecil dan jelas.
3. Setelah selesai, developer membuat pull request untuk di-review PM.
4. PM (Davin) melakukan review & merge ke branch utama.

Ini menunjukkan bahwa tim memahami konsep:
✔ branching
✔ merging
✔ pull request
✔ kolaborasi multi-developer
✔ pembagian role backend/frontend

---

📷 Referensi WBS

WBS Sprint berdasarkan diagram WBS Sistem EO (terlampir pada dokumen / gambar).

---

📝 Catatan

File Java dipetakan sesuai fitur-fitur dalam sprint. Pembagian ini tidak wajib real tetapi digunakan sebagai simulasi workflow Git dalam satu tim proyek perangkat lunak.

---
