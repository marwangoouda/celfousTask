package com.celfocus.hiring.kickstarter.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExceptionErrorDTO {
    private int statusCode;
    private String message;
}
