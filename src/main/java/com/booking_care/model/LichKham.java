package com.booking_care.model;

import com.booking_care.constant.ewewwewe.Status;
import lombok.Data;
import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "lich_kham")
public class LichKham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer chuyenKhoaId;

    private String moTaTrieuChung;

    private Date thoiGianDk;

    private Date ngayKham;

    private Status status;

    private Integer khungGioKham;

    private Integer tienKham;

    private boolean isPaid;

    @ManyToOne
    @JoinColumn(name = "id_bac_sy")
    private BacSy bacSy;

    @ManyToOne
    @JoinColumn(name = "id_benh_nhan")
    private BenhNhan benhNhan;

    @PrePersist
    void prePresit(){
        this.thoiGianDk = new Date();
    }

}

