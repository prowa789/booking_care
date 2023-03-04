package com.booking_care.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bac_sy")
@Data
public class BacSy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String hoTen;
    private Date ngaySinh;
    private String sdt;
    private String email;
    private String gioiThieu;
    private String chucVu;
    private Integer tienKham;
    @ManyToOne
    @JoinColumn(name = "chuyen_khoa_id")
    private ChuyenKhoa chuyenKhoa;
    private String noiKham;
    private String photo;
    private String chungChi;
    private String kinhNghiem;
    private String linhVucChuyenSau;

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || photo.trim().isEmpty() || id == null) return "image/user.png";

        return "/bacsy-photos/" + id + "/" + photo;
    }

    public BacSy(String hoTen, Date ngaySinh, String sdt, String email, String gioiThieu, String chucVu, ChuyenKhoa chuyenKhoa, String photo, TaiKhoan taiKhoan) {
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.email = email;
        this.gioiThieu = gioiThieu;
        this.chucVu = chucVu;
        this.chuyenKhoa = chuyenKhoa;
        this.photo = photo;
        this.taiKhoan = taiKhoan;
    }

    public BacSy() {
    }

    @OneToOne
    @JoinColumn(name = "id_tai_khoan")
    private TaiKhoan taiKhoan;
}
