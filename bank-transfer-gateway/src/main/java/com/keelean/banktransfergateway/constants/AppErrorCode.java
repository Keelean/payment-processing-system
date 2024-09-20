package com.keelean.banktransfergateway.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppErrorCode {
    SUCCESS("200", "Success"),
    ERROR("500", "Failed");

    private final String code;
    private final String message;
}
