package com.booking_care.repository;

import com.booking_care.model.BaiDang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BaiDangRepository extends JpaRepository<BaiDang,Integer> {
    @Modifying
    @Query("update BaiDang b set b.ten = ?1, b.chuyenMuc.id = ?2,b.tomTat = ?3,b.noiDung=?4,b.photo = ?5 where b.id = ?6")
    void updateBaiDang(String ten, Integer chuyenMucId,String tomTat, String noiDung, String photo, Integer id);
    @Modifying
    @Query("update BaiDang b set b.ten = ?1, b.chuyenMuc.id = ?2,b.tomTat = ?3,b.noiDung=?4 where b.id = ?5")
    void updateBaiDangKoUploadFile(String ten, Integer chuyenMucId,String tomTat, String noiDung, Integer id);
}
