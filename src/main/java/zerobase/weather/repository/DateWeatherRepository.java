package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.DateWeather;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DateWeatherRepository extends JpaRepository<DateWeather, LocalDate> {
    // Date에 따라서 그날에 날씨의 Date값을 가지고 오는 함수
    List<DateWeather> findAllByDate(LocalDate localDate);
}
