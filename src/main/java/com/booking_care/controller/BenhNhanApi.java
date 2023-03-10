package com.booking_care.controller;

import com.booking_care.constant.ewewwewe.Status;
import com.booking_care.model.BenhNhan;
import com.booking_care.model.LichKham;
import com.booking_care.repository.BenhNhanRepository;
import com.booking_care.repository.LichKhamRepository;
import com.booking_care.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(maxAge = 3600)
public class BenhNhanApi {

    @Autowired
    LichKhamRepository lichKhamRepo;
    @Autowired
    BenhNhanRepository benhNhanRepo;

    @GetMapping("/api/lichKhamBenh")
    @PreAuthorize("hasAuthority('BENH_NHAN')")
    public List<LichKham> getlichKhamBenhApi(@AuthenticationPrincipal CustomUserDetails taiKhoan,
                                             @RequestParam(value = "page", required = true, defaultValue = "1") Integer page,
                                             @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                                             @RequestParam(value = "status", required = false) Integer status) {
        if (taiKhoan == null || !taiKhoan.hasRole("BENH_NHAN")) {
            return null;
        }
        BenhNhan benhNhan = benhNhanRepo.findByTaiKhoan(taiKhoan.getTaiKhoan());
        List<LichKham> lichKhamList = null;
        if (status != null) {
            if (status == 0) {
                lichKhamList = lichKhamRepo.getAllLichKhamOfBenhNhanByStatus(benhNhan.getId(), Status.CHO_XU_LY, PageRequest.of(page - 1, pageSize));
            }
            if (status == 1) {
                lichKhamList = lichKhamRepo.getAllLichKhamOfBenhNhanByStatus(benhNhan.getId(), Status.DA_XAC_NHAN, PageRequest.of(page - 1, pageSize));
            }
            if (status == 2) {
                lichKhamList = lichKhamRepo.getAllLichKhamOfBenhNhanByStatus(benhNhan.getId(), Status.DA_HUY, PageRequest.of(page - 1, pageSize));
            }
            if (status == 3) {
                lichKhamList = lichKhamRepo.getAllLichKhamOfBenhNhanByStatus(benhNhan.getId(), Status.DA_KHAM, PageRequest.of(page - 1, pageSize));
            }
        } else {
            lichKhamList = lichKhamRepo.getAllLichKhamOfBenhNhan(benhNhan.getId(), PageRequest.of(page - 1, pageSize));

        }

        return lichKhamList;
    }
}
