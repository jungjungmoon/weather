package zerobase.weather.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import zerobase.weather.domain.Memo;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

// Jdbc 방식으로 데이터 저장하는 방법
@Repository
public class JdbcMemoRepository {
    // mysql 연동 방법
    private final JdbcTemplate jdbcTemplate;

    // @Autowired -> application.properties 의 데이터 들을 자동으로 가져오게 한다. Mysql 정보 값
    @Autowired
    public JdbcMemoRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // db에 저장
    // sql 쿼리문은 직접 사용자가 입력해야 한다.
    public Memo save(Memo memo){
        String sql = "insert into memo values(?,?)";
        jdbcTemplate.update(sql, memo.getId(), memo.getText());
        return memo;
    }

    public List<Memo> findAll(){
        String sql = "select * from memo";
        return jdbcTemplate.query(sql, memoRowMapper());
    }

    // Optional 객체로 바꿔준다. 그러면 findById 로 찾아서 null값을 처리하기 쉽게 한다.
    public Optional<Memo> findById(int id){
        String sql = "select * from memo where id = ?";
        return jdbcTemplate.query(sql, memoRowMapper(), id).stream().findFirst();
    }

    private RowMapper<Memo> memoRowMapper(){
        //ResultSet
        // {id = 1, text = 'this is memo~!'}
        return (rs, rowNum) -> new Memo(
                rs.getInt("id"),
                rs.getString("text")
        );
    }
}
