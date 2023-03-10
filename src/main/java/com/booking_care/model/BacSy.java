package com.booking_care.model;

import com.booking_care.model.request.BacSyResgisterRequest;
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
    private String chucVu;
    private Integer tienKham;
    @ManyToOne
    @JoinColumn(name = "chuyen_khoa_id")
    private ChuyenKhoa chuyenKhoa;
    private String noiKham = "";
    private String photo;
    private String chungChi;
    private String kinhNghiem;
    private String linhVucChuyenSau;

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || photo.trim().isEmpty() || id == null) return "image/user.png";

        return "/bacsy-photos/" + id + "/" + photo;
    }

    public BacSy(String hoTen, Date ngaySinh, String sdt, String email, String chucVu, Integer tienKham, ChuyenKhoa chuyenKhoa, String noiKham, String photo, String chungChi, String kinhNghiem, String linhVucChuyenSau, TaiKhoan taiKhoan) {
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.email = email;
        this.chucVu = chucVu;
        this.tienKham = tienKham;
        this.chuyenKhoa = chuyenKhoa;
        this.noiKham = noiKham;
        this.photo = photo;
        this.chungChi = chungChi;
        this.kinhNghiem = kinhNghiem;
        this.linhVucChuyenSau = linhVucChuyenSau;
        this.taiKhoan = taiKhoan;
    }

    public BacSy() {
    }
    public BacSy(BacSyResgisterRequest request) {
        this.hoTen = request.getHoTen();
        this.sdt = request.getSdt();
        this.email = request.getEmail();
        this.chucVu = request.getChucVu();
        this.tienKham = request.getTienKham();
        this.noiKham = request.getNoiKham();
        this.photo = request.getPhoto();
        this.chungChi = request.getChungChi();
        this.kinhNghiem = request.getKinhNghiem();
        this.linhVucChuyenSau = request.getLinhVucChuyenSau();
    }

    @OneToOne
    @JoinColumn(name = "id_tai_khoan")
    private TaiKhoan taiKhoan;
}
