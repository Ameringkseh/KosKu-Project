package models;

public class Pengeluaran {
    private int id;
    private String kategori;
    private String jenis;
    private int jumlah;
    private String tanggal;
    private String catatan;

    public Pengeluaran(int id, String kategori, String jenis, int jumlah, String tanggal, String catatan) {
        this.id = id;
        this.kategori = kategori;
        this.jenis = jenis;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.catatan = catatan;
    }

    public Pengeluaran(String kategori, String jenis, int jumlah, String tanggal, String catatan) {
        this(0, kategori, jenis, jumlah, tanggal, catatan);
    }

    // Getter dan Setter
    public int getId() { return id; } 
    public String getKategori() { return kategori; }
    public String getJenis() { return jenis; }
    public int getJumlah() { return jumlah; }
    public String getTanggal() { return tanggal; }
    public String getCatatan() { return catatan; }

    public void setId(int id) { this.id = id; }
    public void setKategori(String kategori) { this.kategori = kategori; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
    
    
}
