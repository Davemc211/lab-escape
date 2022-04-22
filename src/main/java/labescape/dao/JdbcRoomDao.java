package labescape.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;

public class JdbcRoomDao implements RoomDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcRoomDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    }

    @Override
    public String getSpecificDescription(int roomNumber) {
        String roomDesc = null;
        String sql = "SELECT room_desc FROM room where room_id = ?";

        SqlRowSet roomInfo = jdbcTemplate.queryForRowSet(sql, roomNumber);

        if(roomInfo.next()){
            roomDesc = roomInfo.getString("room_desc");
        }
        return roomDesc;
    }

    @Override
    public String getRandomDescription() {
        return null;
    }

    @Override
    public Boolean isValueRep() {
        return null;
    }

    @Override
    public String updateDescription() {
        return null;
    }
}
