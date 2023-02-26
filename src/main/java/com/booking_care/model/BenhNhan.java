package com.booking_care.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Entity
@Data
@Table(name = "benh_nhan")
public class BenhNhan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "ho_ten")
    private String hoTen;

    @DateTimeFormat(pattern="dd/MM/yyyy")
    @Column(name = "ngay_sinh")
    private Date ngaySinh;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "email")
    private String email;

    @Column(name = "dia_chi")
    private String diaChi;

    private String photo;

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || photo.trim().isEmpty() || id == null) return "image/user.png";

        return "/benhnhan-photos/" + id + "/" + photo;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tai_khoan")
    private TaiKhoan taiKhoan;

}
