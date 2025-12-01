/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tubesmibd;

/**
 *
 * @author nicho
 */
public class Vendor {

    private int idVendor;
    private String namaVendor;
    private String jenisLayanan;
    private String kontak;
    private int harga;

    public Vendor(int idVendor, String namaVendor, String jenisLayanan, String kontak, int harga) {
        this.idVendor = idVendor;
        this.namaVendor = namaVendor;
        this.jenisLayanan = jenisLayanan;
        this.kontak = kontak;
        this.harga = harga;
    }

    public int getHarga() {
        return harga;
    }

    // Getter & Setter
    public int getIdVendor() {
        return idVendor;
    }

    public String getNamaVendor() {
        return namaVendor;
    }

    public String getJenisLayanan() {
        return jenisLayanan;
    }

    public String getKontak() {
        return kontak;
    }

    @Override
    public String toString() {
        return namaVendor; // untuk ditampilkan di combobox misalnya
    }
}
