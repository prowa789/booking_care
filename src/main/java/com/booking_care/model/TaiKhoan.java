package com.booking_care.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.*;


@Data
@Entity
@Table(name="tai_khoan")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    @OneToOne
    @JoinColumn(name = "id_vai_tro")
    private VaiTro vaiTro;

    public boolean hasRole(String tenVaiTro) {
            if (vaiTro.getTen().equals(tenVaiTro)) {
                return true;
            }
        return false;

    }
    private Integer provider;
}
