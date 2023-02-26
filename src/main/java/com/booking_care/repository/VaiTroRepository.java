package com.booking_care.repository;

import com.booking_care.model.VaiTro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaiTroRepository extends CrudRepository<VaiTro,Integer> {
}
