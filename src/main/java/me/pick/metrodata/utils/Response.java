package me.pick.metrodata.utils;

import lombok.*;
import org.springframework.http.HttpStatusCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {

    private String message;

    private HttpStatusCode code;

    private String status;

    private Object responseObject;

}
