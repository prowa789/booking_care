package com.booking_care.model.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class BacSyRequest {
    private Integer id;
    private String hoTen;
    private String ngaySinh;
    private String sdt;
    private String email;
    private String gioiThieu;
    private Integer chuyenKhoaId;
    private String chucVu;
}
