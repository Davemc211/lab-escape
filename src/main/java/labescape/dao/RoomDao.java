package labescape.dao;

public interface RoomDao {

    String getSpecificDescription(int roomNumber);

    String getRandomDescription();

    Boolean isValueRep();

    String updateDescription();

}
