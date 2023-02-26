package com.booking_care.model.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class BenhNhanRequest {
    private Integer id;
    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;
    @NotBlank(message = "ngay sinh không được để trống")
    private String ngaySinh;
    @NotBlank(message = "sdt không được để trống")
    @Size(min = 10,message = "sdt ít nhất 10 số")
    private String sdt;
    @NotBlank(message = "email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;
    @NotBlank(message = "Địa chỉ không được để trống")
    private String diaChi;
    private String photos;
}
