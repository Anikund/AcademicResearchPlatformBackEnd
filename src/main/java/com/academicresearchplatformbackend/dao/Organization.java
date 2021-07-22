package com.academicresearchplatformbackend.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Organization {
    @Id
    private Long id;
    @Column(unique = true)
    private String name;//名字
    private String location;//位置
    private String tel;//电话
    private String description;//简介
    private String regulations;//规章制度
    //@OneToOne
    //@JsonIgnore
    //private User principal;//负责人，院系则对应秘书，其他的组织就不知道了
    @Column(name="principal_id")
    private Long principal_id;


//    @OneToMany
//    private List<ResearchProject> projects;//项目，项目里有成果和文件


}
