package com.booking_care.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MomoObject {
    private String partnerCode="MOMOZVB420220719";
    private String accessKey="LwnYZLEefMcDHBvk";
    private Long requestId;
    private Long amount;
    private Long orderId;
    private String orderInfo="";
    private String returnUrl="http://localhost:8085/lich-kham-benh?status=0";
    private String notifyUrl="http://localhost:8085/lich-kham-benh?status=0";
    private String requestType="captureMoMoWallet";
    private String signature;
    private Long errorCode;
    private String message;
    private String localMessage;
    private String payUrl;
    private String username;
    @Override
    public String toString() {
        return
                "{\"partnerCode\":\"" + partnerCode +
                "\", \"accessKey\":\"" + accessKey +
                "\", \"requestId\":\"" + requestId +
                "\", \"amount\":\"" + amount +
                        "\", \"orderInfo\":\"" + orderInfo +
                "\", \"orderId\":\"" + orderId +
                "\", \"returnUrl\":\"" + returnUrl +
                "\", \"notifyUrl\":\"" + notifyUrl +
                "\", \"requestType\":\"" + requestType+
                "\", \"signature\":\"" + signature +
                        "\", \"username\":\"" + username + "\""+

                '}';
    }
}
