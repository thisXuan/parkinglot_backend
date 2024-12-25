package com.parkinglot_backend.dto;

/**
 * @Author: HeYuxin
 * @CreateTime: 2024-12-24
 * @Description:
 */


public class QRCodeRequest {
    private String qrCodeContent;

    // Getter and Setter
    public String getQrCodeContent() {
        return qrCodeContent;
    }

    public void setQrCodeContent(String qrCodeContent) {
        this.qrCodeContent = qrCodeContent;
    }
}
