package com.sd.mihnea.proiect_pos.repository;

import com.sd.mihnea.proiect_pos.model.Evenimente;
import com.sd.mihnea.proiect_pos.model.Pachete;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Join_PERepository {


    private final JdbcTemplate jdbcTemplate;


    public Join_PERepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void linkEventToPacket(int eventId, int packetId, int numarLocuri) {
        String sql = "INSERT INTO JOIN_PE (EvenimentID, PachetID, numarLocuri) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, eventId, packetId, numarLocuri);
    }

    public List<Evenimente> findByEventId(int eventId) {
        String sql = """
            SELECT e.* FROM EVENIMENTE e
            JOIN JOIN_PE j ON j.EvenimentID = e.ID
            WHERE j.PachetID = ?
        """;
        RowMapper<Evenimente> mapper = (r, i) -> {
            Evenimente e = new Evenimente();
            e.setID(r.getInt("ID"));
            e.setNume(r.getString("nume"));
            e.setLocatie(r.getString("locatie"));
            e.setDescriere(r.getString("descriere"));
            e.setNumarLocuri(r.getInt("numarLocuri"));
            e.setID_OWNER(r.getInt("ID_OWNER"));
            return e;
        };
        return jdbcTemplate.query(sql, mapper, eventId);
    }


    public List<Pachete> findByPacketId(int packetId) {
        String sql = """
            SELECT p.* FROM PACHETE p
            JOIN JOIN_PE j ON j.PachetID = p.ID
            WHERE j.EvenimentID = ?
        """;
        RowMapper<Pachete> mapper = (r, i) -> {
            Pachete p = new Pachete();
            p.setID(r.getInt("ID"));
            p.setNume(r.getString("nume"));
            p.setDescriere(r.getString("descriere"));
            p.setLocatie(r.getString("locatie"));
            p.setID_OWNER(r.getInt("ID_OWNER"));
            return p;
        };
        return jdbcTemplate.query(sql, mapper, packetId);
    }

    public void deleteRelation(int packetId, int eventId) {
        String sql = "DELETE FROM JOIN_PE WHERE PachetID = ? AND EvenimentID = ?";
        jdbcTemplate.update(sql, packetId, eventId);
    }


}
