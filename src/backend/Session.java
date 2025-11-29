/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tubesmibd;

/**
 *
 * @author nicho
 */
public class Session {

    private static Session instance;
    private int idAsisten;

    private Session() {
    } // private constructor agar tidak bisa langsung diinstansiasi

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setIdAsisten(int id) {
        this.idAsisten = id;
    }

    public int getIdAsisten() {
        return idAsisten;
    }

}
