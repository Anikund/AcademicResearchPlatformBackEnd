package com.academicresearchplatformbackend.dao;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class Organization {
    @Id
    private Long id;
    @NotNull
    private String name;//名字
    private String location;//位置
    private String tel;//电话
    private String description;//简介
    private String regulations;//规章制度
    @OneToMany
    private List<ResearchProject> projects;//项目，项目里有成果和文件

}
