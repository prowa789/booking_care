package com.booking_care.repository;

import com.booking_care.model.BenhNhan;
import com.booking_care.model.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BenhNhanRepository extends JpaRepository<BenhNhan,Integer> {
    BenhNhan findByTaiKhoan(TaiKhoan taiKhoan);
    @Modifying
    @Query("update BenhNhan b set b.diaChi = ?1, b.hoTen = ?2,b.ngaySinh=?3,b.sdt=?4,b.email=?5,b.photo=?6 where b.id =?7 ")
    void updateProfileBenhNhan(String diaChi, String hoTen, Date ngaySinh,String sdt,String email,String photo,Integer id);
    @Modifying
    @Query("update BenhNhan b set b.diaChi = ?1, b.hoTen = ?2,b.ngaySinh=?3,b.sdt=?4,b.email=?5 where b.id =?6 ")
    void updateProfileBenhNhanKoUploadFile(String diaChi, String hoTen, Date ngaySinh,String sdt,String email,Integer id);
    @Query("select b from BenhNhan b where b.email = ?1")
    BenhNhan findByEmail(String email);
}
