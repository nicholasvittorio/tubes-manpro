/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tubesmibd;

import java.util.Date;

/**
 *
 * @author nicho
 */
public class Event {

    private int idEvent;
    private String jenisEvent;
    private Date tanggalEvent;
    private String lokasi;
    private int jumlahUndangan;
    private int idKlien;
    private int idAsisten;
    private String namaAsisten;
    private String namaKlien;
    private int totalBiaya;

    public Event(int id, String jenis, Date tanggal, String lokasi, int jumlah, String namaKlien, String namaAsisten, int totalBiaya) {
        this.idEvent = id;
        this.jenisEvent = jenis;
        this.tanggalEvent = tanggal;
        this.lokasi = lokasi;
        this.jumlahUndangan = jumlah;
        this.namaKlien = namaKlien;
        this.namaAsisten = namaAsisten;
        this.totalBiaya = totalBiaya;
    }

    public int getTotalBiaya() {
        return totalBiaya;
    }

    public String getNamaKlien() {
        return namaKlien;
    }

    public String getNamaAsisten() {
        return namaAsisten;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public String getJenisEvent() {
        return jenisEvent;
    }

    public Date getTanggalEvent() {
        return tanggalEvent;
    }

    public String getLokasi() {
        return lokasi;
    }

    public int getJumlahUndangan() {
        return jumlahUndangan;
    }

}
