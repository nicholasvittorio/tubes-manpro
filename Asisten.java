/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tubesmibd;

/**
 *
 * @author nicho
 */

public class Asisten {
    private int idAsisten;
    private String nama; 
    private String alamat;
    private String kontak; 
    private String username;
    private String password;
    public Asisten(int idAsisten, String nama, String alamat, String kontak, String username, String password) {
        this.idAsisten = idAsisten;
        this.nama = nama;
        this.alamat = alamat;
        this.kontak = kontak;
        this.username = username;
        this.password = password;
                
    }

    public int getIdAsisten() {
        return idAsisten;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getKontak() {
        return kontak;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
}
