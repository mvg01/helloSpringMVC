// OfferDao 클래스와 Offer 모델 클래스 import
import kr.ac.hansung.cse.dao.OfferDao;
import kr.ac.hansung.cse.model.Offer;

// JUnit 5 테스트 관련 어노테이션 import
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Spring 테스트 관련 어노테이션 import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional  // ✅ 각 테스트 실행 후 트랜잭션 롤백 (DB 원상복구됨 → 테스트 데이터 오염 방지)
@ExtendWith(SpringExtension.class)  // ✅ JUnit5에서 Spring DI, Context 로딩 가능하게 함
@ContextConfiguration(locations = "/dao-context.xml")  // ✅ 테스트용 Spring 설정 파일 로드

public class OfferDaoTest {

    @Autowired
    private OfferDao offerDao;  // ✅ 테스트할 DAO 클래스 자동 주입

    // ✅ 모든 테스트 메서드 전에 실행되는 준비 메서드 (기본 테스트 데이터 삽입)
    @BeforeEach
    public void setUp() {
        Offer offer = new Offer();  // 테스트용 Offer 생성
        offer.setName("Test Offer");
        offer.setEmail("test@example.com");
        offer.setText("This is a test offer");

        offerDao.insert(offer);  // DB에 삽입
    }

    // ✅ 이름으로 Offer 조회 기능 테스트
    @Test
    @DisplayName("Test1: testGetOfferByName")
    public void testGetOfferByName() {
        Offer offer = offerDao.getOffer("Test Offer");  // 이름으로 조회
        assertNotNull(offer);  // 조회된 객체가 null이 아니어야 함
        assertEquals("Test Offer", offer.getName());  // 이름이 정확히 일치해야 함
    }

    // ✅ ID로 Offer 조회 기능 테스트
    @Test
    @DisplayName("Test2: testGetOfferById")
    public void testGetOfferById() {
        Offer savedOffer = offerDao.getOffer("Test Offer");  // 먼저 이름으로 가져온 후
        Offer offer = offerDao.getOffer(savedOffer.getId());  // ID로 다시 조회
        assertNotNull(offer);  // 조회 성공
        assertEquals(savedOffer.getId(), offer.getId());  // ID 일치 여부 확인
    }

    // ✅ Offer 전체 리스트 조회 테스트
    @Test
    @DisplayName("Test3: testGetOffers")
    public void testGetOffers() {
        List<Offer> offers = offerDao.getOffers();  // 전체 조회
        assertNotNull(offers);  // 리스트 null 여부 확인
        assertFalse(offers.isEmpty());  // 비어있지 않아야 함 (데이터가 있어야 함)
    }

    // ✅ Offer 삽입(insert) 기능 테스트
    @Test
    @DisplayName("Test4: testInsert")
    public void testInsert() {
        Offer newOffer = new Offer();  // 새로운 Offer 생성
        newOffer.setName("New Offer");
        newOffer.setEmail("new@example.com");
        newOffer.setText("This is a new offer");

        offerDao.insert(newOffer);  // 삽입
        assertNotNull(newOffer.getId());  // ID가 생성되었는지 확인

        Offer savedOffer = offerDao.getOffer(newOffer.getId());  // 다시 DB에서 조회
        assertNotNull(savedOffer);  // 정상적으로 저장되었는지 확인
        assertEquals("New Offer", savedOffer.getName());  // 이름 일치 여부 확인
    }

    // ✅ Offer 수정(update) 기능 테스트
    @Test
    @DisplayName("Test5: testUpdate")
    public void testUpdate() {
        Offer offer = offerDao.getOffer("Test Offer");  // 기존 Offer 가져오기
        assertNotNull(offer);
        offer.setText("Updated text");  // 텍스트 수정
        offerDao.update(offer);  // 수정 저장

        Offer updatedOffer = offerDao.getOffer(offer.getId());  // 수정 후 다시 조회
        assertNotNull(updatedOffer);
        assertEquals("Updated text", updatedOffer.getText());  // 텍스트 변경 확인
    }

    // ✅ Offer 삭제(delete) 기능 테스트
    @Test
    @DisplayName("Test6: testDelete")
    public void testDelete() {
        Offer offer = offerDao.getOffer("Test Offer");  // 삭제할 Offer 조회
        assertNotNull(offer);
        offerDao.delete(offer.getId());  // 삭제 실행

        Offer deletedOffer = offerDao.getOffer(offer.getId());  // 다시 조회 → null이어야 함
        assertNull(deletedOffer);  // 삭제되었는지 확인
    }
}
