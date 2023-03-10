package com.booking_care.repository;

import com.booking_care.model.TaiKhoan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;


@Repository
public interface TaiKhoanRepository extends CrudRepository<TaiKhoan,Integer> {
    TaiKhoan findByUsername(String username);
    @Modifying
    @Query("update TaiKhoan t set t.password = ?1 where t.username = ?2")
    void updateMatKhau(String password, String username);
    long countByUsername(String username);
}
