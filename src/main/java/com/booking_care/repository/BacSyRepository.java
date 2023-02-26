package com.booking_care.repository;

import com.booking_care.model.BacSy;
import com.booking_care.model.TaiKhoan;
import com.booking_care.model.response.BacSyDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BacSyRepository extends JpaRepository<BacSy,Integer> {

//    @Query("select new com.booking_care.model.response.BacSyDto(b.id) from BacSy b")
//    BacSyDto getBacSy();

    @Modifying
    @Query("update BacSy b set b.ngaySinh = ?1,b.hoTen=?2,b.chucVu=?3,b.chuyenKhoa.id=?4,b.gioiThieu=?5,b.sdt=?6,b.email=?7 where b.id = ?8")
    void updateThongTinBacSy(Date ngaySinh, String hoTen, String chucVu, Integer chuyenKhoaId, String gioiThieu, String sdt, String email, Integer id);

    @Modifying
    @Query("update BacSy b set b.ngaySinh = ?1,b.hoTen=?2,b.chucVu=?3,b.chuyenKhoa.id=?4,b.gioiThieu=?5,b.sdt=?6,b.email=?7,b.photo=?8 where b.id = ?9")
    void updateThongTinBacSyAndUploadFile(Date ngaySinh, String hoTen, String chucVu, Integer chuyenKhoaId, String gioiThieu, String sdt, String email, String photo, Integer id);

    @Query("select b from BacSy b where b.chuyenKhoa.id = ?1")
    List<BacSy> getAllByChuyenKhoa(Integer idChuyenKhoa);

    BacSy findByTaiKhoan(TaiKhoan taiKhoan);
}
