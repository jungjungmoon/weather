package zerobase.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 프로젝트 안에서 트랜잭션이 동작하게 된다. 활성화 시킨다.
@EnableScheduling // 매일 자정에 날씨 데이터 불러오기
public class WeatherApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherApplication.class, args);
    }

}
