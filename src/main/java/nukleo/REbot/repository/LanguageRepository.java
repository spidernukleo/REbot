package nukleo.REbot.repository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class LanguageRepository {
    @Autowired
    private JdbcTemplate jdbc;
}
