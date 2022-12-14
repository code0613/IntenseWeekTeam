package com.sparta.hanghaememo.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ResponseMsgDto {


    private String msg ;
    private int status ;

    public ResponseMsgDto(int statusCode, String msg){
        this.status = statusCode;
        this.msg = msg;
    }
}