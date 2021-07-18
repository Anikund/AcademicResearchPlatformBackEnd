package com.academicresearchplatformbackend.dao;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.sql.Timestamp;
import java.util.List;

@Data
@Entity
public class ResearchProject {
    @Id
    private Long id;
    private String description;
    private Integer budget;//预算，单位元
    private Timestamp applyTime;//申请时间
    private Boolean isPending;//是否待审核
    private Timestamp terminatingTime;//结算时间
    private Integer fund;//到账经费
    private Integer cost;//支出
    private Integer left;//剩余经费
    private Timestamp midwayTime;//中期检查时间
    @OneToMany
    private List<FileResource> resources;
    @OneToMany
    private List<ResearchFeat> feats;
}
