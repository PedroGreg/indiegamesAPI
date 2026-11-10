package br.com.indiegamesapi.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Schema(name = "ExceptionResponseModel", example = "{\n" +
        "  \"timestamp\": \"2026-11-06T18:45:00.000+00:00\",\n" +
        "  \"message\": \"name: O name da categoria deve ser preenchido\",\n" +
        "  \"details\": \"uri=/api/category\"\n" +
        "}")
public record ExceptionResponseModel(Date timestamp, String message, String details) {
}
