/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package tubesmibd;

import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.text.NumberFormat;
import java.util.Locale;

/**
 *
 * @author nicho
 */
public class Ui_Asisten1 extends javax.swing.JFrame {

    private int selectedEventId = -1;

    /**
     * Creates new form Ui_Pemilik
     */
    private int id_asisten;

    public Ui_Asisten1() {
        initComponents();
        show_klien();
        show_event();
        loadKlien();
        loadVendor();
        showVendorByEvent();
        showVendorByEvent2();
        updateDashboard();

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

    public ArrayList<Event> listEvent() {
        ArrayList<Event> listEvent = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT e.id_event, e.jenis_event, e.tanggal_event, e.lokasi, e.jumlah_undangan, "
                    + "k.nama_klien, a.nama, "
                    + "(SELECT COALESCE(SUM(v.harga), 0) "
                    + " FROM Event_Vendor ev "
                    + " JOIN Vendor v ON ev.id_vendor = v.id_vendor "
                    + " WHERE ev.id_event = e.id_event) AS total_biaya "
                    + "FROM Event e "
                    + "JOIN Klien k ON e.id_klien = k.id_klien "
                    + "JOIN Asisten a ON e.id_asisten = a.id_asisten";

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

    public void show_klien() {
        ArrayList<Klien> list = listKlien();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Hapus data lama dari tabel sebelum menambah baru

        Object[] row = new Object[5];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getNama_klien();
            row[2] = list.get(i).getEmail();
            row[3] = list.get(i).getUmur();
            row[4] = list.get(i).getAlamat();
            model.addRow(row);
        }
    }

    public void show_event() {
        ArrayList<Event> list = listEvent(); // Asumsikan kamu sudah punya metode listEvent()
        DefaultTableModel model = (DefaultTableModel) TabelEvent.getModel(); // Ganti jTable2 sesuai nama JTable kamu
        model.setRowCount(0); // Reset data lama
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        Object[] row = new Object[8]; // Tambahkan kolom untuk nama asisten
        for (int i = 0; i < list.size(); i++) {

            row[0] = list.get(i).getIdEvent();
            row[1] = list.get(i).getJenisEvent();
            row[2] = list.get(i).getTanggalEvent();
            row[3] = list.get(i).getLokasi();
            row[4] = list.get(i).getJumlahUndangan();
            row[5] = list.get(i).getNamaKlien();
            row[6] = list.get(i).getNamaAsisten();
            row[7] = formatter.format(list.get(i).getTotalBiaya());

            model.addRow(row);
        }
    }

    private void loadVendor() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Vendor";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            jComboBoxVendor.removeAllItems(); // ganti comboBoxKlien dengan jComboBoxVendor

            while (rs.next()) {
                int id = rs.getInt("id_vendor");
                String nama = rs.getString("nama_vendor");
                String jenisLayanan = rs.getString("jenis_vendor");
                String kontak = rs.getString("kontak");
                int harga = rs.getInt("harga"); // tambahkan ini

                Vendor vendor = new Vendor(id, nama, jenisLayanan, kontak, harga); // lengkap
                jComboBoxVendor.addItem(vendor);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load data vendor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadKlien() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM Klien";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            comboBoxKlien.removeAllItems(); // kosongkan dulu

            while (rs.next()) {
                int id = rs.getInt("id_klien");
                String nama = rs.getString("nama_klien");
                String email = rs.getString("email");
                int umur = rs.getInt("umur");
                String alamat = rs.getString("alamat");

                Klien klien = new Klien(id, nama, email, umur, alamat);
                comboBoxKlien.addItem(klien); // tambahkan objek Klien
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal load data klien: " + e.getMessage());
            e.printStackTrace();
        }
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
            pst.setInt(1, selectedEventId); // Gunakan selectedEventId yang sudah di-set saat row diklik

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
        ArrayList<Vendor> list = listVendorByEvent();
        DefaultTableModel model = (DefaultTableModel) TabelVendor.getModel();
        model.setRowCount(0);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        for (Vendor v : list) {
            Object[] row = new Object[5]; // sebelumnya 4
            row[0] = v.getIdVendor();
            row[1] = v.getNamaVendor();
            row[2] = v.getJenisLayanan();
            row[3] = v.getKontak();
            row[4] = formatter.format(v.getHarga());

            model.addRow(row);
        }
    }

    public ArrayList<Vendor> listVendorByEvent2() {
        ArrayList<Vendor> list = new ArrayList<>();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT id_vendor, nama_vendor, jenis_vendor, kontak, harga FROM Vendor";

            PreparedStatement pst = conn.prepareStatement(sql);
//            pst.setInt(1, idEvent); //  Set parameter id_event

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

    public void showVendorByEvent2() {
        ArrayList<Vendor> list = listVendorByEvent2();
        DefaultTableModel model = (DefaultTableModel) jTable7.getModel();
        model.setRowCount(0);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        for (Vendor v : list) {
            Object[] row = new Object[5]; // sebelumnya 4
            row[0] = v.getIdVendor();
            row[1] = v.getNamaVendor();
            row[2] = v.getJenisLayanan();
            row[3] = v.getKontak();
            row[4] = formatter.format(v.getHarga());

            model.addRow(row);
        }
    }

    public int getJumlahEventDitugaskan() {
        int count = 0;
        int idAsisten = Session.getInstance().getIdAsisten();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT COUNT(*) FROM Event WHERE id_asisten = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, idAsisten);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal menghitung jumlah event: " + e.getMessage());
        }
        return count;
    }

    public Event getEventTerdekat() {
        Event event = null;
        int idAsisten = Session.getInstance().getIdAsisten();
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT e.id_event, e.jenis_event, e.tanggal_event, e.lokasi, e.jumlah_undangan, k.nama_klien, a.nama, e.id_asisten "
                    + "FROM Event e "
                    + "JOIN Klien k ON e.id_klien = k.id_klien "
                    + "JOIN Asisten a ON e.id_asisten = a.id_asisten "
                    + "WHERE e.id_asisten = ? AND e.tanggal_event >= CAST(GETDATE() AS DATE) "
                    + "ORDER BY e.tanggal_event ASC "
                    + "OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, idAsisten);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                event = new Event(
                        rs.getInt("id_event"),
                        rs.getString("jenis_event"),
                        new java.util.Date(rs.getDate("tanggal_event").getTime()), // convert ke java.util.Date
                        rs.getString("lokasi"),
                        rs.getInt("jumlah_undangan"),
                        rs.getString("nama_klien"),
                        rs.getString("nama"),
                        rs.getInt("id_asisten")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Gagal mendapatkan event terdekat: " + e.getMessage());
        }
        return event;
    }

    public void updateDashboard() {
        int jumlahEvent = getJumlahEventDitugaskan();
        Event terdekat = getEventTerdekat();

        jLabel8.setText(String.valueOf(jumlahEvent));
        id_asisten = Session.getInstance().getIdAsisten();
        if (terdekat != null) {
            jLabel7.setText(
                    "<html>"
                    + "Jenis: " + terdekat.getJenisEvent() + "<br>"
                    + "Tanggal: " + terdekat.getTanggalEvent().toString() + "<br>"
                    + "Lokasi: " + terdekat.getLokasi()
                    + "</html>"
            );
        } else {
            jLabel7.setText("Tidak ada event mendatang");
        }
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldNama = new javax.swing.JTextField();
        jTextFieldEmail = new javax.swing.JTextField();
        jTextFieldUmur = new javax.swing.JTextField();
        jTextFieldAlamat = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        TabelEvent = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        TabelVendor = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        comboBoxKlien = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jComboBoxVendor = new javax.swing.JComboBox<>();
        tombolLogout = new javax.swing.JButton();

        jToolBar1.setRollover(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTabbedPane1.setForeground(new java.awt.Color(51, 0, 51));
        jTabbedPane1.setToolTipText("");
        jTabbedPane1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Talkshow Start Up, 15 Mei 2025, 19.00 WIB");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("5");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setText("Jumlah Event yang Ditugaskan :");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Event Terdekat                          :");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(1612, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel7))
                .addContainerGap(822, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Dashboard", jPanel6);

        jPanel3.setName("Manajemen Klien"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID_Klien", "Nama Klien", "Email", "Umur", "Alamat"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jLabel15.setText("Nama Klien");

        jLabel16.setText("Email");

        jLabel17.setText("Umur");

        jLabel18.setText("Alamat");

        jTextFieldNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNamaActionPerformed(evt);
            }
        });

        jButton1.setText("Save");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Remove");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Update");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldNama, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                            .addComponent(jTextFieldEmail)
                            .addComponent(jTextFieldUmur)
                            .addComponent(jTextFieldAlamat)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 63, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addGap(84, 84, 84)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 621, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1070, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextFieldNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextFieldUmur, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jTextFieldAlamat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(60, 60, 60)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap(669, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Manajemen Klien", jPanel3);
        jPanel3.getAccessibleContext().setAccessibleName("Manajemen Klien");

        TabelEvent.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID_Event", "Jenis Event", "Tanggal Event", "Lokasi", "Undangan", "Klien", "Asisten", "Total Biaya"
            }
        ));
        TabelEvent.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelEventMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(TabelEvent);

        jLabel5.setText("Jenis Event");

        jLabel6.setText("Tanggal Event");

        jLabel12.setText("Lokasi");

        jLabel13.setText("Jumlah Undangan");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Wedding", "Sweet 17", "Gathering", "Seminar" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        TabelVendor.setModel(new javax.swing.table.DefaultTableModel(
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
        TabelVendor.setColumnSelectionAllowed(true);
        TabelVendor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelVendorMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(TabelVendor);

        jLabel1.setText("Detail Event");

        jButton4.setText("Save");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Update");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Remove");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel2.setText("Nama Klien");

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID_Vendor", "Nama Vendor", "Jenis Vendor", "Kontak", "Biaya"
            }
        ));
        jTable7.setToolTipText("");
        jScrollPane7.setViewportView(jTable7);

        jLabel3.setText("List Vendor");

        jButton7.setText("Save");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton9.setText("Remove");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel4.setText("Nama Vendor");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addGap(20, 20, 20))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField6)
                            .addComponent(comboBoxKlien, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField5)
                            .addComponent(jComboBox1, 0, 232, Short.MAX_VALUE)
                            .addComponent(jTextField1)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton4)
                            .addComponent(jLabel4))
                        .addGap(54, 54, 54)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                                .addComponent(jButton6))
                            .addComponent(jComboBoxVendor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jButton7)
                        .addGap(32, 32, 32)
                        .addComponent(jButton9)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 616, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)))))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 619, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(776, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel5)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6))
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(comboBoxKlien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4)
                            .addComponent(jButton5)
                            .addComponent(jButton6))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jComboBoxVendor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(57, 57, 57)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton7)
                            .addComponent(jButton9)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(439, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Manajemen Event", null, jPanel5, "");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("");

        tombolLogout.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        tombolLogout.setText("Logout");
        tombolLogout.setName(""); // NOI18N
        tombolLogout.setOpaque(true);
        tombolLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tombolLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tombolLogout, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tombolLogout))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    public void Close() {
        WindowEvent closeWindow = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeWindow);
    }
    private void tombolLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tombolLogoutActionPerformed
        // TODO add your handling code here:
        Close();
        Login windowLogin = new Login();
        windowLogin.setVisible(true);
    }//GEN-LAST:event_tombolLogoutActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        try {
            Vendor selectedVendor = (Vendor) jComboBoxVendor.getSelectedItem();
            if (selectedVendor == null) {
                JOptionPane.showMessageDialog(this, "Pilih vendor terlebih dahulu!");
                return;
            }

            if (selectedEventId == -1) {
                JOptionPane.showMessageDialog(this, "Pilih event terlebih dahulu dari tabel!");
                return;
            }

            int idVendor = selectedVendor.getIdVendor();
            int idEvent = selectedEventId;

            Connection conn = DatabaseConnection.getConnection();

            // Cek apakah sudah ada di tabel event_vendor
            String checkSql = "SELECT COUNT(*) FROM event_vendor WHERE id_event = ? AND id_vendor = ?";
            PreparedStatement checkPst = conn.prepareStatement(checkSql);
            checkPst.setInt(1, idEvent);
            checkPst.setInt(2, idVendor);
            ResultSet rs = checkPst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Vendor sudah ditambahkan ke event ini!");
                return;
            }

            // Insert kalau belum ada
            String sql = "INSERT INTO event_vendor (id_event, id_vendor) VALUES (?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, idEvent);
            pst.setInt(2, idVendor);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Vendor berhasil ditambahkan ke event!");
            String updateBiayaSql = "UPDATE Event SET total_biaya = ("
                    + "SELECT SUM(V.harga) FROM Event_Vendor EV "
                    + "JOIN Vendor V ON EV.id_vendor = V.id_vendor "
                    + "WHERE EV.id_event = ?) "
                    + "WHERE id_event = ?";
            PreparedStatement updatePst = conn.prepareStatement(updateBiayaSql);
            updatePst.setInt(1, idEvent);
            updatePst.setInt(2, idEvent);
            updatePst.executeUpdate();
            showVendorByEvent(); // Refresh tabel vendor
            show_event();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error menambahkan vendor ke event: " + e.getMessage());
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        try {
            if (selectedEventId == -1) {
                JOptionPane.showMessageDialog(this, "Pilih event terlebih dahulu dari tabel!");
                return;
            }

            String jenis = jComboBox1.getSelectedItem().toString();
            String tanggalStr = jTextField1.getText();
            String lokasi = jTextField5.getText();
            int jumlahUndangan = Integer.parseInt(jTextField6.getText());
            java.sql.Date tanggalEvent = java.sql.Date.valueOf(tanggalStr);

            Klien selectedKlien = (Klien) comboBoxKlien.getSelectedItem();
            int idKlien = selectedKlien.getId();

            int idAsistenLogin = Session.getInstance().getIdAsisten(); // bisa tetap pakai id login

            Connection conn = DatabaseConnection.getConnection();
            String sql = "UPDATE Event SET jenis_event = ?, tanggal_event = ?, lokasi = ?, jumlah_undangan = ?, id_klien = ?, id_asisten = ? WHERE id_event = ?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, jenis);
            pst.setDate(2, tanggalEvent);
            pst.setString(3, lokasi);
            pst.setInt(4, jumlahUndangan);
            pst.setInt(5, idKlien);
            pst.setInt(6, idAsistenLogin);
            pst.setInt(7, selectedEventId);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Event berhasil diperbarui!");
            show_event(); // refresh tabel
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal update event: " + e.getMessage());
            e.printStackTrace();
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            String jenis = jComboBox1.getSelectedItem().toString();
            String tanggalStr = jTextField1.getText(); // Format: YYYY-MM-DD
            String lokasi = jTextField5.getText();
            int jumlahUndangan = Integer.parseInt(jTextField6.getText());

            java.sql.Date tanggalEvent = java.sql.Date.valueOf(tanggalStr);

            Klien selectedKlien = (Klien) comboBoxKlien.getSelectedItem();
            int idKlien = selectedKlien.getId();

            // Ambil idAsisten dari session
            int idAsistenLogin = Session.getInstance().getIdAsisten();

            Connection conn = DatabaseConnection.getConnection();
            String sql = "INSERT INTO Event (jenis_event, tanggal_event, lokasi, jumlah_undangan, id_klien, id_asisten) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, jenis);
            pst.setDate(2, tanggalEvent);
            pst.setString(3, lokasi);
            pst.setInt(4, jumlahUndangan);
            pst.setInt(5, idKlien);
            pst.setInt(6, idAsistenLogin);

            pst.executeUpdate();

            JOptionPane.showMessageDialog(this, "Event berhasil disimpan!");
            show_event(); // refresh tabel

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saat menyimpan event: " + e.getMessage());
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void TabelEventMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelEventMouseClicked
        int row = TabelEvent.getSelectedRow();
        if (row >= 0) {
            selectedEventId = Integer.parseInt(TabelEvent.getValueAt(row, 0).toString());

            // Ambil nilai dari tabel
            String jenisEvent = TabelEvent.getValueAt(row, 1).toString();
            String tanggalEvent = TabelEvent.getValueAt(row, 2).toString();
            String lokasi = TabelEvent.getValueAt(row, 3).toString();
            String jumlahUndangan = TabelEvent.getValueAt(row, 4).toString();
            String namaKlien = TabelEvent.getValueAt(row, 5).toString();

            // Set ke input form
            jComboBox1.setSelectedItem(jenisEvent); // jenis event
            jTextField1.setText(tanggalEvent);      // tanggal event
            jTextField5.setText(lokasi);            // lokasi
            jTextField6.setText(jumlahUndangan);    // jumlah undangan

            // Cari dan set combobox klien
            for (int i = 0; i < comboBoxKlien.getItemCount(); i++) {
                Klien klien = (Klien) comboBoxKlien.getItemAt(i);
                if (klien.getNama_klien().equals(namaKlien)) {
                    comboBoxKlien.setSelectedIndex(i);
                    break;
                }
            }

            showVendorByEvent(); // tampilkan vendor juga
        }

    }//GEN-LAST:event_TabelEventMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        try {
            int row = jTable1.getSelectedRow();
            String value = jTable1.getModel().getValueAt(row, 0).toString();

            String query = "UPDATE Klien SET nama_klien = ?, email = ?, umur = ?, alamat = ? WHERE id_klien = ?";
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);

            // Ambil data dari inputan form
            String nama = jTextFieldNama.getText();
            String email = jTextFieldEmail.getText();
            String umur = jTextFieldUmur.getText();
            String alamat = jTextFieldAlamat.getText();

            pst.setString(1, nama);
            pst.setString(2, email);
            pst.setInt(3, Integer.parseInt(umur));
            pst.setString(4, alamat);
            pst.setString(5, value); // Asumsi kolom pertama adalah id_klien

            pst.executeUpdate();
            show_klien(); // Refresh tabel setelah update
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat update data: " + e.getMessage());
            e.printStackTrace(); // Bantu log untuk konsol juga
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        try {
            int row = jTable1.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Silakan pilih data yang akan dihapus!");
                return;
            }

            String id_klien = jTable1.getModel().getValueAt(row, 0).toString();

            String query = "DELETE FROM Klien WHERE id_klien = ?";
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, id_klien);

            pst.executeUpdate();
            show_klien(); // Refresh tabel setelah delete
            JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        String nama = jTextFieldNama.getText();
        String email = jTextFieldEmail.getText();
        String umur = jTextFieldUmur.getText();
        String alamat = jTextFieldAlamat.getText();

        try {
            Connection conn = DatabaseConnection.getConnection();
            // Cek di tabel ASISTEN
            String sql = "INSERT INTO Klien (nama_klien, email, umur, alamat) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nama);
            pst.setString(2, email);
            pst.setInt(3, Integer.parseInt(umur));
            pst.setString(4, alamat);

            pst.executeUpdate(); // lakukan eksekusi insert
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan!");

            show_klien(); // Refresh tabel setelah insert
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data: " + e.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextFieldNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldNamaActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        int i = jTable1.getSelectedRow();
        TableModel model = jTable1.getModel();
        jTextFieldNama.setText(model.getValueAt(i, 1).toString());
        jTextFieldEmail.setText(model.getValueAt(i, 2).toString());
        jTextFieldUmur.setText(model.getValueAt(i, 3).toString());
        jTextFieldAlamat.setText(model.getValueAt(i, 4).toString());
    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        try {
            if (selectedEventId == -1) {
                JOptionPane.showMessageDialog(this, "Pilih event terlebih dahulu dari tabel!");
                return;
            }

            int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus event ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                Connection conn = DatabaseConnection.getConnection();
                String sql = "DELETE FROM Event WHERE id_event = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, selectedEventId);
                pst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Event berhasil dihapus.");
                show_event();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal hapus event: " + e.getMessage());
            e.printStackTrace();
        }


    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        try {
            int selectedRow = TabelVendor.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih vendor terlebih dahulu dari tabel!");
                return;
            }

            if (selectedEventId == -1) {
                JOptionPane.showMessageDialog(this, "Pilih event terlebih dahulu dari tabel!");
                return;
            }

            // Ambil ID vendor dari tabel
            int idVendor = (int) TabelVendor.getValueAt(selectedRow, 0); // Asumsikan kolom 0 adalah ID vendor

            int konfirmasi = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus vendor dari event ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                Connection conn = DatabaseConnection.getConnection();

                // Delete dari tabel relasi event_vendor
                String sql = "DELETE FROM event_vendor WHERE id_event = ? AND id_vendor = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, selectedEventId);
                pst.setInt(2, idVendor);
                pst.executeUpdate();

                // Update ulang total_biaya di tabel Event
                String updateBiayaSql = "UPDATE Event SET total_biaya = ("
                        + "SELECT COALESCE(SUM(V.harga), 0) FROM Event_Vendor EV "
                        + "JOIN Vendor V ON EV.id_vendor = V.id_vendor "
                        + "WHERE EV.id_event = ?) "
                        + "WHERE id_event = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateBiayaSql);
                updatePst.setInt(1, selectedEventId);
                updatePst.setInt(2, selectedEventId);
                updatePst.executeUpdate();

                JOptionPane.showMessageDialog(this, "Vendor berhasil dihapus dari event.");
                showVendorByEvent(); // Refresh tabel vendor
                show_event();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus vendor dari event: " + e.getMessage());
            e.printStackTrace();
        }

    }//GEN-LAST:event_jButton9ActionPerformed

    private void TabelVendorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelVendorMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_TabelVendorMouseClicked

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
            java.util.logging.Logger.getLogger(Ui_Asisten1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Ui_Asisten1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Ui_Asisten1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Ui_Asisten1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ui_Asisten1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable TabelEvent;
    private javax.swing.JTable TabelVendor;
    private javax.swing.JComboBox<Klien> comboBoxKlien;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<Vendor> jComboBoxVendor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable7;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextFieldAlamat;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldNama;
    private javax.swing.JTextField jTextFieldUmur;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton tombolLogout;
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