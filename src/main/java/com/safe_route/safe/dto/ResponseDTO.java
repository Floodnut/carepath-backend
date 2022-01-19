package com.safe_route.safe.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T>{
    private String error;
    //private String datatype;
    private int total;
    private List<T> data;
}
