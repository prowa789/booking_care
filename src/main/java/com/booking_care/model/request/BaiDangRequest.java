package com.booking_care.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BaiDangRequest {
    private Integer id;
    private String ten;
    private String noiDung;
    private String tomTat;
    private Integer chuyenMucId;
}
