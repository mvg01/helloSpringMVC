package kr.ac.hansung.cse.dao;

import kr.ac.hansung.cse.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class OfferDao {
    // 서비스와 DB 사이에서 객체지향 API를 제공하는 계층
    // JDBC 활용
    // jdbc class, jdbc driver, datasource 라이브러리 필요
    private JdbcTemplate jdbcTemplate;  //pas(portable service abstraction)

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    public int getRowCount() {
        String sqlStatement= "select count(*) from offers";
        return jdbcTemplate.queryForObject(sqlStatement, Integer.class);

    }

    // cRud operation api
    //query and return a single object
    public Offer getOffer(String name) {

        String sqlStatement = "select * from offers where name=?";
        return jdbcTemplate.queryForObject(sqlStatement, new Object[]{name},
                new RowMapper<Offer>() {

                    @Override
                    public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {

                        Offer offer = new Offer();

                        offer.setId(rs.getInt("id"));
                        offer.setName(rs.getString("name"));
                        offer.setEmail(rs.getString("email"));
                        offer.setText(rs.getString("text"));

                        return offer;
                    }
                });
    }

    public List<Offer> getOffers() {

        String sqlStatement= "select * from offers";
        return jdbcTemplate.query(sqlStatement, new RowMapper<Offer>() {

            @Override
            public Offer mapRow(ResultSet rs, int rowNum) throws SQLException {

                Offer offer= new Offer();

                offer.setId(rs.getInt("id"));
                offer.setName(rs.getString("name"));
                offer.setEmail(rs.getString("email"));
                offer.setText(rs.getString("text"));

                return offer;
            }
        });
    }

    // Crud method
    public boolean insert(Offer offer) {

        String name= offer.getName();
        String email= offer.getEmail();
        String text = offer.getText();

        String sqlStatement= "insert into offers (name, email, text) values (?,?,?)";

        return (jdbcTemplate.update(sqlStatement, new Object[] {name, email, text}) == 1);
    }

    // crUd method
    public boolean update(Offer offer) {

        int id = offer.getId();
        String name= offer.getName();
        String email= offer.getEmail();
        String text = offer.getText();

        String sqlStatement= "update offers set name=?, email=?, text=? where id=?";

        return (jdbcTemplate.update(sqlStatement, new Object[] {name, email, text, id}) == 1);
    }

    //cruD method
    public boolean delete(int id) {
        String sqlStatement= "delete from offers where id=?";
        return (jdbcTemplate.update(sqlStatement, new Object[] {id}) == 1);
    }

}
