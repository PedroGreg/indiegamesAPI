package com.indiegames.exception;

import java.util.Date;

public record ExceptionResponseModel(Date timestamp, String message, String details) {
}
