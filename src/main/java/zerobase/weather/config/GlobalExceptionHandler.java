package zerobase.weather.config;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 전역의 일 처리
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class) // 컨트롤러 안에 있는 Exception만 한다.
    public Exception handleAllException(){
        System.out.println("error from GlobalExceptionHandler");
        return new Exception();
    }
}
