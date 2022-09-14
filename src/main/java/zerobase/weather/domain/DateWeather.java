package zerobase.weather.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "date_weather")
@NoArgsConstructor
public class DateWeather {
    // 매일 자정에 날씨 데이터 불러오기
    @Id
    private LocalDate date;
    private String weather;
    private String icon;
    private double temperature;
}
