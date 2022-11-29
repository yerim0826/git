package com.shopping.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
/*@GeneratedValue(strategy = GenerationType.AUTO, generator = "system-uuid") // 문자는 AUTO
    @GenericGenerator(name="system-uuid", strategy = "uuid")*/
@Entity
@Table(name = "sarams")
@Getter @Setter @ToString
public class Saram {
    @Id
    @Column(name = "saram_id")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name="saram_id", strategy = "auto")
    private String id ;

    @Column(nullable = false, length = 50)
    private String name ;

    @Column(nullable = true, length = 200)
    private String address ;

    private Integer salary ;
}
