package com.booking_care.repository;

import com.booking_care.model.BacSy;
import com.booking_care.model.TaiKhoan;
import com.booking_care.model.response.BacSyDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("update BacSy b set b.ngaySinh = ?1,b.hoTen=?2,b.chucVu=?3,b.chuyenKhoa.id=?4,b.sdt=?5,b.email=?6,b.chungChi=?7,b.kinhNghiem=?8,b.linhVucChuyenSau=?9,b.tienKham=?10,b.noiKham=?11 where b.id = ?12")
    void updateThongTinBacSy(Date ngaySinh, String hoTen, String chucVu, Integer chuyenKhoaId, String sdt, String email,String chungChi,String kinhNghiem,String linhVucChuyenSau,Integer tienKham ,String noiKham, Integer id);

    @Modifying
    @Query("update BacSy b set b.ngaySinh = ?1,b.hoTen=?2,b.chucVu=?3,b.chuyenKhoa.id=?4,b.sdt=?5,b.email=?6,b.photo=?7,b.chungChi=?8,b.kinhNghiem=?9,b.linhVucChuyenSau=?10,b.tienKham=?11,b.noiKham=?12 where b.id = ?13")
    void updateThongTinBacSyAndUploadFile(Date ngaySinh, String hoTen, String chucVu, Integer chuyenKhoaId, String sdt, String email, String photo,String chungChi,String kinhNghiem,String linhVucChuyenSau,Integer tienKham ,String noiKham, Integer id);

    @Query("select b from BacSy b where b.chuyenKhoa.id = ?1")
    List<BacSy> getAllByChuyenKhoa(Integer idChuyenKhoa);

    BacSy findByTaiKhoan(TaiKhoan taiKhoan);

    @Query("select b from BacSy b where b.chuyenKhoa.id = ?1 and b.hoTen like %?2%")
    Page<BacSy> getAllByChuyenKhoaAndName(Integer idCk, String bsName, Pageable pageable);

    @Query("select b from BacSy b where b.hoTen like %?1%")
    Page<BacSy> getAllByName(String bsName, Pageable pageable);
}
