package com.booking_care.model.response;

import com.booking_care.model.TaiKhoan;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class BenhNhanDto {
    private Integer id;

    private String hoTen;

    private String ngaySinh;

    private String sdt;

    private String email;

    private String diaChi;
}