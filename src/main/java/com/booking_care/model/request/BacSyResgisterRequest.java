package com.booking_care.model.request;

import lombok.Data;

@Data
public class BacSyResgisterRequest {
    private String username;
    private String password;
    private String hoTen;
    private String ngaySinh;
    private String sdt;
    private String email;
    private String gioiThieu;
    private Integer chuyenKhoaId;
    private String chucVu;
}
