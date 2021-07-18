package com.academicresearchplatformbackend.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ResearchFeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;//类型
    private String authors;//作者名字
    private String journalName;//收录期刊/xxx名字
    private Integer citedCount;//被引用数量
    private String description;//简介
    private String level;//级别
//    @OneToMany
//    private List<FileResource> resources;
//
}
