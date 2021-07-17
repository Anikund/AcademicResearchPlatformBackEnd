package com.academicresearchplatformbackend.dao;

import lombok.Data;
import org.aspectj.weaver.GeneratedReferenceTypeDelegate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity

public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;//vue path
    private String name;//vue name
    private String displayName;//displayed name

    private String component;//component name
    @OneToOne
    @JoinColumn(name = "p_id")
    private Menu parentMenu;//parent menu id
    @Transient
    List<Menu> children;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        Menu another = (Menu) o;
        return another.getId() == this.id;
    }
}
