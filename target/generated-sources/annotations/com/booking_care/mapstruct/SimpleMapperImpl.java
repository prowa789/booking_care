package com.booking_care.mapstruct;

import com.booking_care.model.BacSy;
import com.booking_care.model.BenhNhan;
import com.booking_care.model.request.BenhNhanRequest;
import com.booking_care.model.response.BacSyDto;
import com.booking_care.model.response.BenhNhanDto;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-02-20T08:29:25+0700",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 1.8.0_352 (Amazon.com Inc.)"
)
@Component
public class SimpleMapperImpl implements SimpleMapper {

    @Override
    public BenhNhanDto toBenhNhanDto(BenhNhan benhNhan) {
        if ( benhNhan == null ) {
            return null;
        }

        BenhNhanDto benhNhanDto = new BenhNhanDto();

        if ( benhNhan.getNgaySinh() != null ) {
            benhNhanDto.setNgaySinh( new SimpleDateFormat( "dd/MM/yyyy" ).format( benhNhan.getNgaySinh() ) );
        }
        benhNhanDto.setId( benhNhan.getId() );
        benhNhanDto.setHoTen( benhNhan.getHoTen() );
        benhNhanDto.setSdt( benhNhan.getSdt() );
        benhNhanDto.setEmail( benhNhan.getEmail() );
        benhNhanDto.setDiaChi( benhNhan.getDiaChi() );

        return benhNhanDto;
    }

    @Override
    public BenhNhan toBenhNhan(BenhNhanRequest benhNhanRequest) {
        if ( benhNhanRequest == null ) {
            return null;
        }

        BenhNhan benhNhan = new BenhNhan();

        try {
            if ( benhNhanRequest.getNgaySinh() != null ) {
                benhNhan.setNgaySinh( new SimpleDateFormat( "dd/MM/yyyy" ).parse( benhNhanRequest.getNgaySinh() ) );
            }
        }
        catch ( ParseException e ) {
            throw new RuntimeException( e );
        }
        benhNhan.setId( benhNhanRequest.getId() );
        benhNhan.setHoTen( benhNhanRequest.getHoTen() );
        benhNhan.setSdt( benhNhanRequest.getSdt() );
        benhNhan.setEmail( benhNhanRequest.getEmail() );
        benhNhan.setDiaChi( benhNhanRequest.getDiaChi() );

        return benhNhan;
    }

    @Override
    public BacSyDto toBacSyDto(BacSy bacSy) {
        if ( bacSy == null ) {
            return null;
        }

        BacSyDto bacSyDto = new BacSyDto();

        if ( bacSy.getNgaySinh() != null ) {
            bacSyDto.setNgaySinh( new SimpleDateFormat( "dd/MM/yyyy" ).format( bacSy.getNgaySinh() ) );
        }
        bacSyDto.setId( bacSy.getId() );
        bacSyDto.setHoTen( bacSy.getHoTen() );
        bacSyDto.setSdt( bacSy.getSdt() );
        bacSyDto.setEmail( bacSy.getEmail() );
        bacSyDto.setGioiThieu( bacSy.getGioiThieu() );
        bacSyDto.setChucVu( bacSy.getChucVu() );

        return bacSyDto;
    }
}
