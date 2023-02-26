package com.booking_care.repository;

import com.booking_care.model.ChuyenKhoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChuyenKhoaRepository extends JpaRepository<ChuyenKhoa,Integer> {
    @Modifying
    @Query("update ChuyenKhoa c set c.ten = ?1, c.moTa = ?2 where c.id = ?3")
    void updateChuyenKhoa(String ten,String moTa,Integer id);
}
