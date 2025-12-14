/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tubesmibd;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author nicho
 */
public class Ui_Pemilik extends javax.swing.JFrame {

    private int currentEventId = -1; // Simpan ID event saat ini
    private int selectedAsistenId;
    private int selectedEventId;

    /**
     * Creates new form Ui_Pemilik
     */
    public Ui_Pemilik() {
        initComponents();
        show_asisten();
        showVendorByEvent();
        show_event();
        showVendorByEvent2();
        loadDashboardData();
    }

    public ArrayList<Klien> listKlien() {
        ArrayList<Klien> listKlien = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Klien";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Klien klien = new Klien(
                        rs.getInt("id_klien"),
                        rs.getString("nama_klien"),
                        rs.getString("email"),
                        rs.getInt("umur"),
                        rs.getString("alamat")
                );
                listKlien.add(klien);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengambil data: " + e.getMessage());
        }

        return listKlien;
    }

    public ArrayList<Asisten> listAsisten() {
        ArrayList<Asisten> listAsisten = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Asisten";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Asisten asisten = new Asisten(
                        rs.getInt("id_asisten"),
                        rs.getString("nama"),
                        rs.getString("alamat"),
                        rs.getString("kontak"),
                        rs.getString("username"),
                        rs.getString("password")
                );
                listAsisten.add(asisten);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengambil data: " + e.getMessage());
        }

        return listAsisten;
    }

    public void show_asisten() {
        ArrayList<Asisten> list = listAsisten();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Hapus data lama dari tabel sebelum menambah baru

        Object[] row = new Object[6];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getIdAsisten();
            row[1] = list.get(i).getNama();
            row[2] = list.get(i).getAlamat();
            row[3] = list.get(i).getKontak();
            row[4] = list.get(i).getUsername();
            row[5] = list.get(i).getPassword();
            model.addRow(row);
        }
    }

    public ArrayList<Vendor> listVendor() {
        ArrayList<Vendor> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Vendor";

            PreparedStatement pst = conn.prepareStatement(sql);
//            pst.setInt(1, selectedEventId); // Set id_event dari parameter

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Vendor v = new Vendor(
                        rs.getInt("id_vendor"),
                        rs.getString("nama_vendor"),
                        rs.getString("jenis_vendor"),
                        rs.getString("kontak"),
                        rs.getInt("harga")
                );
                list.add(v);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal mengambil data vendor untuk event: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public ArrayList<Vendor> listVendorByEvent() {
        ArrayList<Vendor> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT v.id_vendor, v.nama_vendor, v.jenis_vendor, v.kontak, v.harga "
                    + "FROM Vendor v "
                    + "JOIN Event_Vendor ev ON v.id_vendor = ev.id_vendor "
                    + "WHERE ev.id_event = ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, selectedEventId); //  Set parameter id_event

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Vendor v = new Vendor(
                        rs.getInt("id_vendor"),
                        rs.getString("nama_vendor"),
                        rs.getString("jenis_vendor"),
                        rs.getString("kontak"),
                        rs.getInt("harga")
                );
                list.add(v);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error mengambil vendor: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public void showVendorByEvent() {
        ArrayList<Vendor> list = listVendor();
        DefaultTableModel model = (DefaultTableModel) TableVendor.getModel();
        model.setRowCount(0);

        for (Vendor v : list) {
            Object[] row = new Object[5]; // sebelumnya 4
            row[0] = v.getIdVendor();
            row[1] = v.getNamaVendor();
            row[2] = v.getJenisLayanan();
            row[3] = v.getKontak();
            row[4] = v.getHarga();
            model.addRow(row);
        }
    }

    public void showVendorByEvent2() {
        ArrayList<Vendor> list = listVendorByEvent();
        DefaultTableModel model = (DefaultTableModel) jTable6.getModel();
        model.setRowCount(0);

        for (Vendor v : list) {
            Object[] row = new Object[5]; // sebelumnya 4
            row[0] = v.getIdVendor();
            row[1] = v.getNamaVendor();
            row[2] = v.getJenisLayanan();
            row[3] = v.getKontak();
            row[4] = v.getHarga();
            model.addRow(row);
        }
    }

    public ArrayList<Event> listEvent() {
        ArrayList<Event> listEvent = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT e.id_event, e.jenis_event, e.tanggal_event, e.lokasi, e.jumlah_undangan, "
                    + "k.nama_klien, a.nama, e.total_biaya "
                    + "FROM Event e "
                    + "JOIN Klien k ON e.id_klien = k.id_klien "
                    + "JOIN ASISTEN a ON e.id_asisten = a.id_asisten";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id_event"),
                        rs.getString("jenis_event"),
                        rs.getDate("tanggal_event"),
                        rs.getString("lokasi"),
                        rs.getInt("jumlah_undangan"),
                        rs.getString("nama_klien"),
                        rs.getString("nama"),
                        rs.getInt("total_biaya") // tambahan
                );
                listEvent.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengambil data: " + e.getMessage());
        }

        return listEvent;
    }

    public void show_event() {
        ArrayList<Event> list = listEvent(); // Asumsikan kamu sudah punya metode listEvent()
        DefaultTableModel model = (DefaultTableModel) jTable5.getModel(); // Ganti jTable2 sesuai nama JTable kamu
        model.setRowCount(0); // Reset data lama

        Object[] row = new Object[8]; // Tambahkan kolom untuk nama asisten
        for (int i = 0; i < list.size(); i++) {

            row[0] = list.get(i).getIdEvent();
            row[1] = list.get(i).getJenisEvent();
            row[2] = list.get(i).getTanggalEvent();
            row[3] = list.get(i).getLokasi();
            row[4] = list.get(i).getJumlahUndangan();
            row[5] = list.get(i).getNamaKlien();
            row[6] = list.get(i).getNamaAsisten();
            row[7] = list.get(i).getTotalBiaya();
            model.addRow(row);
        }
    }

    public int getTotalEvent() {
        int count = 0;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) AS total FROM Event";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menghitung event: " + e.getMessage());
        }
        return count;
    }

    public int getTotalPendapatanBulanIni() {
        int total = 0;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT SUM(COALESCE(total_biaya, 0)) AS pendapatan FROM Event";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                total = rs.getInt("pendapatan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menghitung pendapatan: " + e.getMessage());
        }
        return total;
    }

    public int getTotalKlien() {
        int count = 0;
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) AS total FROM Klien";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menghitung klien: " + e.getMessage());
        }
        return count;
    }

    private void loadDashboardData() {
        int totalEvent = getTotalEvent();
        int totalPendapatan = getTotalPendapatanBulanIni();
        int totalKlien = getTotalKlien();

        labelEventBerjalan.setText(String.valueOf(totalEvent));
        labelPendapatan.setText("Rp " + totalPendapatan);
        labelJumlahKlien.setText(String.valueOf(totalKlien));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        labelPendapatan = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        labelJumlahKlien = new javax.swing.JLabel();
        labelEventBerjalan = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TableVendor = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        TextFieldNamaVendor = new javax.swing.JTextField();
        TextFieldKontak = new javax.swing.JTextField();
        buttonSaveVendor = new javax.swing.JButton();
        buttonUpdateVendor = new javax.swing.JButton();
        buttonDeleteVendor = new javax.swing.JButton();
        ComboBoxVendor = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldHarga = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();

        jToolBar1.setRollover(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setText("Logout");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jTabbedPane1.setInheritsPopupMenu(true);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Pendapatan Keseluruhan      :");

        labelPendapatan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        labelPendapatan.setText("18.000.000");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setText("Jumlah Klien                         :");

        labelJumlahKlien.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        labelJumlahKlien.setText("5");

        labelEventBerjalan.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        labelEventBerjalan.setText("5");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Event yang Sedang Berjalan : ");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(32, 32, 32)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelJumlahKlien, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(labelEventBerjalan)
                    .addComponent(labelPendapatan, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(2434, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(labelEventBerjalan, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(labelPendapatan))
                .addGap(12, 12, 12)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(labelJumlahKlien))
                .addContainerGap(274, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Dashboard", jPanel6);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID_Asisten", "Nama", "Alamat", "Kontak", "Username", "Password"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel13.setText("Nama");

        jLabel14.setText("Alamat");

        jLabel15.setText("Kontak");

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Delete");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField2))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 648, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1912, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton3))))
                .addContainerGap(180, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Manajemen Asisten", jPanel3);

        TableVendor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID_Vendor", "Nama Vendor", "Jenis Vendor", "Kontak", "Biaya"
            }
        ));
        TableVendor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TableVendorMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(TableVendor);

        jLabel16.setText("Nama Vendor");

        jLabel17.setText("Jenis Vendor");

        jLabel18.setText("Kontak");

        TextFieldKontak.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TextFieldKontakActionPerformed(evt);
            }
        });

        buttonSaveVendor.setText("Save");
        buttonSaveVendor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSaveVendorActionPerformed(evt);
            }
        });

        buttonUpdateVendor.setText("Update");
        buttonUpdateVendor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUpdateVendorActionPerformed(evt);
            }
        });

        buttonDeleteVendor.setText("Delete");
        buttonDeleteVendor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteVendorActionPerformed(evt);
            }
        });

        ComboBoxVendor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dekorasi", "Sound System", "Katering", "Fotografi", "Panggung", "MC", "Hiburan" }));

        jLabel1.setText("Harga");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel16)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel17))
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ComboBoxVendor, 0, 227, Short.MAX_VALUE)
                            .addComponent(TextFieldKontak, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                            .addComponent(TextFieldNamaVendor, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                            .addComponent(jTextFieldHarga))
                        .addGap(42, 42, 42)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 549, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(buttonSaveVendor)
                        .addGap(42, 42, 42)
                        .addComponent(buttonUpdateVendor)
                        .addGap(45, 45, 45)
                        .addComponent(buttonDeleteVendor)))
                .addContainerGap(1919, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(TextFieldNamaVendor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(ComboBoxVendor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel18)
                            .addComponent(TextFieldKontak, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextFieldHarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buttonSaveVendor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonUpdateVendor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(buttonDeleteVendor, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(114, 114, 114))
        );

        jTabbedPane1.addTab("Data Vendor", jPanel4);

        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID_Event", "Jenis Event", "Tanggal Event", "Lokasi", "Undangan", "Klien", "Asisten", "Total Biaya"
            }
        ));
        jTable5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable5MouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(jTable5);

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID_Vendor", "Nama Vendor", "Jenis Vendor", "Kontak", "Biaya"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.setColumnSelectionAllowed(true);
        jScrollPane6.setViewportView(jTable6);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbedPane1.addTab("Laporan Kerja", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 417, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.getAccessibleContext().setAccessibleDescription("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(700, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void Close() {
        WindowEvent closeWindow = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeWindow);
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Close();
        Login windowLogin = new Login();
        windowLogin.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        try {
            String nama = jTextField1.getText();
            String alamat = jTextField2.getText();
            String kontak = jTextField3.getText();
            String username = nama + "_asisten";
            String password = "admin123";

            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Asisten (nama, alamat, kontak, username, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setString(2, alamat);
            pst.setString(3, kontak);
            pst.setString(4, username);
            pst.setString(5, password);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Asisten berhasil disimpan!");
            show_asisten(); // refresh tabel

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saat menyimpan asisten: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked

        int i = jTable1.getSelectedRow();
        TableModel model = jTable1.getModel();

        selectedAsistenId = Integer.parseInt(model.getValueAt(i, 0).toString()); // Ambil ID
        jTextField1.setText(model.getValueAt(i, 1).toString()); // Nama
        jTextField2.setText(model.getValueAt(i, 2).toString()); // Alamat
        jTextField3.setText(model.getValueAt(i, 3).toString()); // Kontak


    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        if (selectedAsistenId == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih asisten yang ingin dihapus.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus asisten ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "DELETE FROM Asisten WHERE id_asisten = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, selectedAsistenId);
            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Asisten berhasil dihapus!");
            selectedAsistenId = -1;
            show_asisten(); // Refresh tabel

            // Kosongkan text field
            TextFieldNamaVendor.setText("");

            TextFieldKontak.setText("");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus asisten: " + e.getMessage());
            e.printStackTrace();
        }


    }//GEN-LAST:event_jButton3ActionPerformed

    private void buttonDeleteVendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteVendorActionPerformed

        try {

            // Cek apakah vendor dipilih di tabel
            int row = TableVendor.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Silakan pilih vendor yang ingin dihapus!");
                return;
            }

            // Ambil id_vendor dari tabel
            String idVendor = TableVendor.getModel().getValueAt(row, 0).toString();

            // Konfirmasi
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus vendor ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Debug log
            System.out.println("DELETE FROM Event_Vendor WHERE id_event = " + selectedEventId + " AND id_vendor = " + idVendor);

            // Hapus relasi vendor dari event
            String query = "DELETE FROM Vendor WHERE id_vendor = ?";
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, idVendor); // Gunakan setString jika id_vendor di DB adalah VARCHAR

            int rowsAffected = pst.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Vendor berhasil dihapus dari tabel.");
            } else {
                JOptionPane.showMessageDialog(this, "Vendor tidak ditemukan dalam tabel ini.");
            }

            // Refresh tampilan tabel
            showVendorByEvent();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus vendor: " + e.getMessage());
            e.printStackTrace();
        }

    }//GEN-LAST:event_buttonDeleteVendorActionPerformed

    private void buttonUpdateVendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUpdateVendorActionPerformed

        try {
            if (selectedEventId == -1) {
                JOptionPane.showMessageDialog(this, "Pilih event terlebih dahulu dari tabel!");
                return;
            }

            int row = TableVendor.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Silakan pilih vendor yang ingin diperbarui!");
                return;
            }

            String namaVendor = TextFieldNamaVendor.getText();
            String jenisVendor = ComboBoxVendor.getSelectedItem().toString();
            String kontak = TextFieldKontak.getText();
            String hargaText = jTextFieldHarga.getText();
            String idVendor = TableVendor.getModel().getValueAt(row, 0).toString();

            if (namaVendor.isEmpty() || jenisVendor.isEmpty() || kontak.isEmpty() || hargaText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
                return;
            }

            int harga = Integer.parseInt(hargaText); // konversi ke int

            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE Vendor SET nama_vendor = ?, jenis_vendor = ?, kontak = ?, harga = ? WHERE id_vendor = ?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, namaVendor);
            pst.setString(2, jenisVendor);
            pst.setString(3, kontak);
            pst.setInt(4, harga); // setInt untuk harga
            pst.setString(5, idVendor);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Vendor berhasil diperbarui!");

            showVendorByEvent();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat update vendor: " + e.getMessage());
            e.printStackTrace();
        }


    }//GEN-LAST:event_buttonUpdateVendorActionPerformed

    private void buttonSaveVendorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSaveVendorActionPerformed
        // TODO add your handling code here:

        try {
            String namaVendor = TextFieldNamaVendor.getText();
            String jenisVendor = ComboBoxVendor.getSelectedItem().toString();
            String kontak = TextFieldKontak.getText();
            String hargaText = jTextFieldHarga.getText();

            if (namaVendor.isEmpty() || jenisVendor.isEmpty() || kontak.isEmpty() || hargaText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
                return;
            }

            int harga = Integer.parseInt(hargaText); // Konversi string ke int

            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Vendor (nama_vendor, jenis_vendor, kontak, harga) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, namaVendor);
            pst.setString(2, jenisVendor);
            pst.setString(3, kontak);
            pst.setInt(4, harga); // pakai setInt karena harga di database INT

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Vendor berhasil disimpan!");
            showVendorByEvent();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga harus berupa angka!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saat menyimpan vendor: " + e.getMessage());
            e.printStackTrace();
        }


    }//GEN-LAST:event_buttonSaveVendorActionPerformed

    private void TextFieldKontakActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TextFieldKontakActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TextFieldKontakActionPerformed

    private void TableVendorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TableVendorMouseClicked

        int i = TableVendor.getSelectedRow();
        if (i == -1) {
            return;
        }
        TableModel model = TableVendor.getModel();
        TextFieldNamaVendor.setText(model.getValueAt(i, 1).toString());
        ComboBoxVendor.setSelectedItem(model.getValueAt(i, 2).toString()); // ganti textfield dengan combobox
        TextFieldKontak.setText(model.getValueAt(i, 3).toString());
        jTextFieldHarga.setText(model.getValueAt(i, 4).toString());


    }//GEN-LAST:event_TableVendorMouseClicked

    private void jTable5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable5MouseClicked
        int row = jTable5.getSelectedRow();
        if (row >= 0) {
            selectedEventId = Integer.parseInt(jTable5.getValueAt(row, 0).toString());
            showVendorByEvent2(); // tampilkan vendor juga
        }

    }//GEN-LAST:event_jTable5MouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Ui_Pemilik.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ui_Pemilik.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ui_Pemilik.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ui_Pemilik.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ui_Pemilik().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> ComboBoxVendor;
    private javax.swing.JTable TableVendor;
    private javax.swing.JTextField TextFieldKontak;
    private javax.swing.JTextField TextFieldNamaVendor;
    private javax.swing.JButton buttonDeleteVendor;
    private javax.swing.JButton buttonSaveVendor;
    private javax.swing.JButton buttonUpdateVendor;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextFieldHarga;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel labelEventBerjalan;
    private javax.swing.JLabel labelJumlahKlien;
    private javax.swing.JLabel labelPendapatan;
    // End of variables declaration//GEN-END:variables
}
//gimikkkkkkkkkkkk
//     private javax.swing.JTextField jTextField1;
//     private javax.swing.JTextField jTextField5;
//     private javax.swing.JTextField jTextField6;
//     private javax.swing.JTextField jTextFieldAlamat;
//     private javax.swing.JTextField jTextFieldEmail;
//     private javax.swing.JTextField jTextFieldNama;
//     private javax.swing.JTextField jTextFieldUmur;
//     private javax.swing.JToolBar jToolBar1;
//     private javax.swing.JButton tombolLogout;
//     // End of variables declaration//GEN-END:variables
// }