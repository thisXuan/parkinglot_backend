package com.parkinglot_backend.service;

import com.parkinglot_backend.dto.QRCodeRequest;
import com.parkinglot_backend.util.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface QRcodeService {
    // 提交二维码方法，接受请求体和Authorization请求头
    Result submitQRCode(QRCodeRequest qrCodeRequest, String token);

    Result getQRCodeContentByUserId(String token);
}
