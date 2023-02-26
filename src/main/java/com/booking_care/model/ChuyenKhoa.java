package com.booking_care.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "chuyen_khoa")
@Data
public class ChuyenKhoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String ten;
    private String moTa;
}
