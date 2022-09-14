package zerobase.weather;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Memo;
import zerobase.weather.repository.JdbcMemoRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional //-> 테스트코드를 다 하고난뒤, 데이터값을 보고싶으면 주석처리해서 데이터 상에서 다시 보여준다.
public class JdbcMemoRepositoryTest {

    //// Jdbc 방식으로 이용한 테스트
    @Autowired
    JdbcMemoRepository jdbcMemoRepository;

    @Test
    void insertMemoTest() {

        //given 주어졌고
        Memo newMemo = new Memo(2, "insertMemoTest");

        //when 이걸 했을때
        jdbcMemoRepository.save(newMemo);

        //then 이런게 나온다
        Optional<Memo> result = jdbcMemoRepository.findById(2);
        assertEquals(result.get().getText(),"insertMemoTest");
    }

    @Test
    void findAllMemoTest() {
        List<Memo> memoList = jdbcMemoRepository.findAll();
        System.out.println(memoList);
        assertNotNull(memoList);
    }


}
