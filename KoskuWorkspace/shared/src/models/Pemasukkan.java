package models;

public class Pemasukkan {
    private int id;
    private String sumber;
    private String jenis;
    private int jumlah;
    private String tanggal;
    private String catatan;

    public Pemasukkan(int id, String sumber, String jenis, int jumlah, String tanggal, String catatan) {
        this.id = id;
        this.sumber = sumber;
        this.jenis = jenis;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.catatan = catatan;
    }

    public Pemasukkan(String sumber, String jenis, int jumlah, String tanggal, String catatan) {
        this(0, sumber, jenis, jumlah, tanggal, catatan);
    }

    // Getter dan Setter
    public int getId() { return id; } 
    public String getSumber() { return sumber; }
    public String getJenis() { return jenis; }
    public int getJumlah() { return jumlah; }
    public String getTanggal() { return tanggal; }
    public String getCatatan() { return catatan; }

    public void setId(int id) { this.id = id; }
    public void setSumber(String sumber) { this.sumber = sumber; }
    public void setJenis(String jenis) { this.jenis = jenis; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
    
    
}
