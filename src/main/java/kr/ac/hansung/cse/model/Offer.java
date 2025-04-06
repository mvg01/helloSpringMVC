package kr.ac.hansung.cse.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// ✅ Lombok 어노테이션: getter/setter, toString, 기본 생성자 자동 생성
@Getter
@Setter
@ToString
@NoArgsConstructor

// ✅ JPA 엔티티임을 선언, DB 테이블과 매핑됨
@Entity
@Table(name="offers")  // 매핑될 테이블 이름 지정
public class Offer {

    @Id  // ✅ 기본 키(primary key)
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ✅ auto_increment 방식
    private int id;

    @Size(min = 2, max = 100, message="Name must be between 2 and 100 chars")  // ✅ 이름 길이 제한 (유효성 검사용)
    private String name;

    @Email(message="please provide a valid email address")  // ✅ 이메일 형식 검사
    @NotEmpty(message="The email address cannot be empty")  // ✅ 이메일 비어있으면 안 됨
    private String email;

    @Size(min = 5, max = 100, message="text must be between 5 and 100 chars")  // ✅ 본문 길이 제한
    private String text;
}
