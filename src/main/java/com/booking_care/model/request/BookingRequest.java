package com.booking_care.model.request;

import com.booking_care.utils.DateUtils;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import java.util.Date;

@Data
public class BookingRequest {
    private Integer chuyenKhoaId;
    private String ngayKham;
    private Integer khungGioKham;
    private String moTaTrieuChung;
    private Integer bacSyId;
    private String tienKham;

    public boolean isValid() {
        Date ngayKham = DateUtils.stringToDate2(this.ngayKham);
        if(this.chuyenKhoaId == 0 || this.khungGioKham == null
                || this.bacSyId == 0 || Strings.isBlank(this.moTaTrieuChung)
                || Strings.isBlank(this.ngayKham)
                || this.moTaTrieuChung.length() < 10) {
            return false;
        }
        if(ngayKham.compareTo(new Date()) <= 0) {
            return false;
        }
        return true;
    }
}
