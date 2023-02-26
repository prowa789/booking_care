package com.booking_care.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class DoiMatKhauRequest {
    @NotBlank(message = "Mật khẩu hiện tại không được để trống")
    @Size(min = 6,message = "Mật khẩu ít nhất 6 số")
    private String matKhauHienTai;
    @NotBlank(message = "Mật khẩu mới không được để trống")
    @Size(min = 6,message = "Mật khẩu ít nhất 6 số")
    private String matKhauMoi;
}
