package com.booking_care.model.request;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

@Data
public class AddBaiDangRequest {
    private String ten;
    private String noiDung;
    private String tomTat;
    private Integer chuyenMucId;
    private MultipartFile image;
}
