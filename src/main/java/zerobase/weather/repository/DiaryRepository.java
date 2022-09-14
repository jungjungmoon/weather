package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;

import java.time.LocalDate;
import java.util.List;

@Repository
// domain 에 있는 Diary 클래스를 가지고 온다. JAP 사용 ~
public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    List<Diary> findAllByDate(LocalDate date); // findAllByDate가 알아서 데이트를 생성해준다. (함수생성) -> 이 데이트를 가지고 그날의 전체 일기를 가지고 오는 함수!
    List<Diary> findAllByDateBetween(LocalDate startDate, LocalDate endDate); // 날씨일기 전체 범위를 찾아주는 함수 2가지 start, end값
    Diary getFirstByDate(LocalDate date); // getFirstByDate -> 다이어리 중에 가장 첫번째 데이터를 가지고와서 일기를 수정하게 한다.

    @Transactional
    void deleteAllByDate(LocalDate date); // 모든 일기 삭제
}
