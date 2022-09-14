package zerobase.weather.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

@Repository
// 자바 ORM 개념 필수 : 이걸 가져와서 쓰겠다. key : Integer
public interface JpaMemoRepository extends JpaRepository<Memo, Integer> {


}
