/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tubesmibd;

/**
 *
 * @author nicho
 */
public class TubesMIBD {

    public static void main(String[] args) {
        // Menjalankan JFrame utama (login)
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
}
    

