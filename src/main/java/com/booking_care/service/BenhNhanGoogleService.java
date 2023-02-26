package com.booking_care.service;

import com.booking_care.model.BenhNhan;
import com.booking_care.model.CustomOAuth2User;
import com.booking_care.model.TaiKhoan;
import com.booking_care.model.VaiTro;
import com.booking_care.repository.BenhNhanRepository;
import com.booking_care.repository.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BenhNhanGoogleService {
    @Autowired
    private BenhNhanRepository benhNhanRepo;
    @Autowired
    private VaiTroRepository vaiTroRepo;

    public void processOAuthPostLogin(CustomOAuth2User customOAuth2User) {
        BenhNhan existBn = benhNhanRepo.findByEmail(customOAuth2User.getEmail());
        VaiTro vaiTro = vaiTroRepo.findById(2).get();
        if (existBn == null) {
            BenhNhan newUser = new BenhNhan();
            newUser.setHoTen(customOAuth2User.getName());
            newUser.setEmail(customOAuth2User.getEmail());
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setUsername(customOAuth2User.getEmail());
            taiKhoan.setProvider(1);
            taiKhoan.setVaiTro(vaiTro);
            newUser.setTaiKhoan(taiKhoan);
            benhNhanRepo.save(newUser);
        }

    }
}
