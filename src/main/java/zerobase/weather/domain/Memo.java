package zerobase.weather.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

// Mysql 메모 테이블 만드는것

// Jpa -> @Entity 추가, name값을 작성한다. 그리고, @Id 추가, @GenerateValue(strategy = GenerationType.IDENTITY)로 지정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "memo")
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String text;

}
