package com.booking_care.model.request;

import lombok.Data;

import javax.persistence.Transient;

@Data
public class BacSyResgisterRequest {
    private String hoTen;
    private String ngaySinh;
    private String sdt;
    private String email;
    private String gioiThieu;
    private Integer chuyenKhoaId;
    private String chucVu;
    private Integer tienKham;
    private String chungChi;
    private String kinhNghiem;
    private String linhVucChuyenSau;
    private String photo = "";
    private String noiKham;
}
