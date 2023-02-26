package com.booking_care.constant.ewewwewe;

public enum Status{
    CHO_XU_LY("Chờ xử lý"), DA_XAC_NHAN("Đã xác nhận"), DA_HUY("Đã hủy"), DA_KHAM("Đã khám");

    private String value;

    private Status(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

    public static void main(String[] args) {
        System.out.println(Status.CHO_XU_LY.getValue());
    }
}
