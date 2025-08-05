package com.celfocus.hiring.kickstarter.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionErrorDTO {
    private int statusCode;
    private String message;

    public ExceptionErrorDTO(String message){
        super();
        this.message = message;
    }

}
