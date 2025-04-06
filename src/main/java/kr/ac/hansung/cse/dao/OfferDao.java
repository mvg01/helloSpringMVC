package kr.ac.hansung.cse.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.ac.hansung.cse.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository  // 이 클래스가 Spring의 DAO 컴포넌트임을 명시
@Transactional  // DAO 메서드들이 트랜잭션 내에서 실행됨
public class OfferDao {

    @PersistenceContext  // JPA의 EntityManager를 주입받음
    private EntityManager entityManager;  // 엔티티 매니저를 통해 DB 접근

    // 이름으로 Offer 한 건 조회 (JPQL 사용)
    public Offer getOffer(String name) {
        return entityManager.createQuery("SELECT o FROM Offer o WHERE o.name = :name", Offer.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    // ID로 Offer 한 건 조회 (PK 기반 단건 조회)
    public Offer getOffer(int id) {
        return entityManager.find(Offer.class, id);
    }

    // 모든 Offer 조회 (SELECT * FROM offers)
    public List<Offer> getOffers() {
        return entityManager.createQuery("SELECT o FROM Offer o", Offer.class)
                .getResultList();
    }

    // Offer 저장 (INSERT)
    public void insert(Offer offer) {
        entityManager.persist(offer);
    }

    // Offer 수정 (UPDATE)
    public void update(Offer offer) {
        entityManager.merge(offer);
    }

    // ID로 Offer 삭제 (DELETE)
    public void delete(int id) {
        Offer offer = entityManager.find(Offer.class, id);
        if (offer != null) {
            entityManager.remove(offer);
        }
    }
}



// jdbc 활용한 코드
/*@Repository
public class OfferDao {
    // ✅ JdbcTemplate을 활용한 DAO 구현 (SQL 기반 직접 접근)
    // Spring JDBC를 사용하여 SQL을 직접 작성하고 DB에 접근

    private JdbcTemplate jdbcTemplate;  // PAS (Portable Service Abstraction)

    // ✅ DataSource를 주입받아 JdbcTemplate 생성
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // ✅ 전체 행 수 조회 (SELECT COUNT(*))
    public int getRowCount() {
        String sqlStatement= "select count(*) from offers";
        return jdbcTemplate.queryForObject(sqlStatement, Integer.class);
    }

    // ✅ 이름으로 Offer 조회 (단일 객체 반환)
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

    // ✅ 모든 Offer 조회 (리스트 반환)
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

    // ✅ Offer 삽입 (INSERT INTO)
    public boolean insert(Offer offer) {

        String name= offer.getName();
        String email= offer.getEmail();
        String text = offer.getText();

        String sqlStatement= "insert into offers (name, email, text) values (?,?,?)";

        return (jdbcTemplate.update(sqlStatement, new Object[] {name, email, text}) == 1);
    }

    // ✅ Offer 수정 (UPDATE)
    public boolean update(Offer offer) {

        int id = offer.getId();
        String name= offer.getName();
        String email= offer.getEmail();
        String text = offer.getText();

        String sqlStatement= "update offers set name=?, email=?, text=? where id=?";

        return (jdbcTemplate.update(sqlStatement, new Object[] {name, email, text, id}) == 1);
    }

    // ✅ Offer 삭제 (DELETE BY ID)
    public boolean delete(int id) {
        String sqlStatement= "delete from offers where id=?";
        return (jdbcTemplate.update(sqlStatement, new Object[] {id}) == 1);
    }

}*/
