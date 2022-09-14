package zerobase.weather.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity // JPA를 사용할 땐 무조건 Entity 쓰기
@Getter
@Setter
@NoArgsConstructor // 생성자 자동 생성 -> service 안에 있는 DiaryService
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // DB에 diary Table 저장한거 도메인 아래 Diary 클래스에 작성 한다.
    private int Id;
    private String weather;
    private String icon;
    private double temperature; // 온도
    private String text;
    private LocalDate date;

    //
    public void setDateWeather(DateWeather dateWeather){
        this.date = dateWeather.getDate();
        this.weather = dateWeather.getWeather();
        this.icon = dateWeather.getIcon();
        this.temperature = dateWeather.getTemperature();
    }

}
