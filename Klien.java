package tubesmibd;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author nicho
 */
public class Klien {

    private int id, umur;
    private String nama_klien, email, alamat;

    public Klien(int id, String nama_klien, String email, int umur, String alamat) {
        this.id = id;
        this.nama_klien = nama_klien;
        this.email = email;
        this.umur = umur;
        this.alamat = alamat;
    }

    public String getAlamat() {
        return alamat;
    }

    public int getId() {
        return id;
    }

    public int getUmur() {
        return umur;
    }

    public String getNama_klien() {
        return nama_klien;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return nama_klien;
    }

}
