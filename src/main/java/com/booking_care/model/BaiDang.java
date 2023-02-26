package com.booking_care.model;

import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.util.Date;

@Table(name = "bai_dang")
@Entity
@Data
public class BaiDang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String ten;
    private String noiDung;
    private String tomTat;
    @JoinColumn(name = "chuyen_muc_id")
    @ManyToOne
    private ChuyenMuc chuyenMuc;

    private Date createAt;
    private String writerBy;
    private String photo;

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || id == null) return null;

        return "/baidang-photos/" + id + "/" + photo;
    }

    @PrePersist
    void createAt() {
        createAt = new Date();
//        writerBy = SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
