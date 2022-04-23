package labescape.dao;

import labescape.model.Container;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Random;


@Component
public class JdbcContainerDao implements ContainerDao{
    Container box;
    private final JdbcTemplate jdbcTemplate;

    public JdbcContainerDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Container getRandomBox() {
        String sql = "SELECT * FROM container ORDER BY RANDOM() LIMIT 1";
        SqlRowSet containerInfo = jdbcTemplate.queryForRowSet(sql);

        if(containerInfo.next()){
            box = createContainerFromRow(containerInfo);
        }

        return box;
    }

    private Container createContainerFromRow(SqlRowSet containerInfo) {
        Container box = new Container();
        box.setName(containerInfo.getString("container_name"));
        box.setType(containerInfo.getString("container_type"));
        if (box.getType().equals("junk")) {
            box.setLocked(false);
        }
        else box.setLocked(new Random().nextInt(10) > 7 && !box.getType().equals("special"));
        return box;
    }

    @Override
    public Container getSpecialBox() {
        return null;
    }

    @Override
    public Container getUnlockedContainer() {
        return null;
    }

    @Override
    public void unlockContainer(Container box) {

    }
}
