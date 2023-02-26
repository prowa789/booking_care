package com.booking_care.model.request;

import com.booking_care.model.ChuyenKhoa;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChuyenKhoaRequest {
    private String ten;
    private String moTa;

    public ChuyenKhoa toChuyenKhoa() {
        ChuyenKhoa chuyenKhoa = new ChuyenKhoa();
        chuyenKhoa.setTen(this.ten);
        chuyenKhoa.setMoTa(this.moTa);
        return chuyenKhoa;
    }
}
