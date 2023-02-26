package com.booking_care.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table
@Data
public class BinhLuan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String noiDung;
    @ManyToOne
    @JoinColumn(name = "id_bac_sy")
    private BacSy bacSy;
    @ManyToOne
    @JoinColumn(name = "id_benh_nhan")
    private BenhNhan benhNhan;
}
