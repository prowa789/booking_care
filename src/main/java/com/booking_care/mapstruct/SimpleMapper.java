package com.booking_care.mapstruct;

import com.booking_care.model.BacSy;
import com.booking_care.model.BenhNhan;
import com.booking_care.model.request.BacSyRequest;
import com.booking_care.model.request.BenhNhanRequest;
import com.booking_care.model.response.BacSyDto;
import com.booking_care.model.response.BenhNhanDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SimpleMapper {
    @Mapping(source = "ngaySinh", target = "ngaySinh", dateFormat = "dd/MM/yyyy")
    BenhNhanDto toBenhNhanDto(BenhNhan benhNhan);
    @Mapping(source = "ngaySinh", target = "ngaySinh", dateFormat = "dd/MM/yyyy")
    BenhNhan toBenhNhan(BenhNhanRequest benhNhanRequest);
    @Mapping(source = "ngaySinh", target = "ngaySinh", dateFormat = "dd/MM/yyyy")
    BacSyDto toBacSyDto(BacSy bacSy);
//    BacSy toBacSy(BacSyRequest bacSyRequest);
}
