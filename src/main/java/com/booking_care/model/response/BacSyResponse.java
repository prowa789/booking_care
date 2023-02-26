package com.booking_care.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BacSyResponse {
    private Integer id;
    private String hoTen;
    private Date ngaySinh;
    private String sdt;
    private String email;
    private String gioiThieu;
    private String chucVu;

    public BacSyResponse(Integer id, String hoTen, Date ngaySinh, String sdt, String email, String gioiThieu, String chucVu) {
        this.id = id;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.email = email;
        this.gioiThieu = gioiThieu;
        this.chucVu = chucVu;
    }
}
