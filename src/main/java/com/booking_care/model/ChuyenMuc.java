package com.booking_care.model;

import lombok.Data;

import javax.persistence.*;

@Table(name = "chuyen_muc")
@Entity
@Data
public class ChuyenMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String ten;
    private String moTa;
}
