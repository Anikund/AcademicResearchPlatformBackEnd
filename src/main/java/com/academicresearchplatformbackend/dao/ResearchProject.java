package com.academicresearchplatformbackend.dao;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class ResearchProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String type;//横向项目还是纵向项目
    private Integer budget;//预算，单位元
    private Timestamp applyTime;//申请时间
    private Boolean isPending;//是否待审核
    private Timestamp terminatingTime;//结算时间
    private Integer fund;//到账经费
    private Integer cost;//支出
    private Integer leftFund;//剩余经费
    private Timestamp midwayTime;//中期检查时间
    private Boolean isTerminated;//
    private boolean waitingFund;

    @ManyToMany
    @JoinTable(name="project_resources",
            joinColumns = @JoinColumn(name="project_id"),
            inverseJoinColumns = @JoinColumn(name="resource_id"))

    private List<FileResource> resources = new ArrayList<>();
    @ManyToMany
    @JoinTable(name="project_feats",
            joinColumns = @JoinColumn(name="project_id"),
            inverseJoinColumns = @JoinColumn(name="feat_id"))

    private List<ResearchFeat> feats = new ArrayList<>();
    @ManyToMany
    @JoinTable(name="project_users",
            joinColumns = @JoinColumn(name="project_id"),
            inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> users= new ArrayList<>();
    @ManyToOne
    private Organization organization;//所属单位
    @PrePersist
    public void setBefore() {
        applyTime = new Timestamp(new Date().getTime());
        isTerminated = false;
        isPending = true;
        cost = 0;
        fund = null;
        leftFund = null;
        waitingFund = true;

    }
}
