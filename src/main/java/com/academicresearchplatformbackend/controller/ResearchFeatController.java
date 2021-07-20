package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.ResearchFeat;
import com.academicresearchplatformbackend.dao.ResearchProject;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.ResearchFeatService;
import com.academicresearchplatformbackend.service.ResearchProjectService;
import com.academicresearchplatformbackend.utils.MyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/feat")
@Api("ResearchFeatController，里面所有api都需要登录")
@Log4j2
public class ResearchFeatController {
    private ResearchFeatService researchFeatService;
    private ResearchProjectService researchProjectService;
    private MyUtils myUtils;

    @Autowired
    public void setResearchFeatService(ResearchFeatService service) {
        this.researchFeatService = service;
    }

    @Autowired
    public void setResearchProjectService(ResearchProjectService researchProjectService) {
        this.researchProjectService = researchProjectService;
    }

    @Autowired
    public void setMyUtils(MyUtils myUtils) {
        this.myUtils = myUtils;
    }

    @GetMapping("/all")
    @ApiOperation("获得所有科研成果的信息")
    public ResponseEntity<Page<ResearchFeat>> getAllPageable(@RequestParam int page,
                                                             @RequestParam int size) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            log.info("用户："+subject.getPrincipal().toString()+"请求所有科研成果信息");
            return new ResponseEntity<>(researchFeatService.findAllPageable(page, size), HttpStatus.OK);
        }
        log.info("未登录用户请求科研成果信息");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/get/by/type/{type}")
    @ApiOperation("根据科研成果的类型获得,url里的type为类型")
    public ResponseEntity<Page<ResearchFeat>> getByTypePageable(@PathVariable String type, @RequestParam int page,
                                                                @RequestParam int size) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            log.info("用户："+subject.getPrincipal().toString()+"请求"+type+"类型的科研成果信息");
            return new ResponseEntity<>(researchFeatService.findByType(type, page, size), HttpStatus.OK);
        }
        log.info("未登录用户请求科研成果信息");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/get/by/patent/{isPatent}")
    @ApiOperation("根据科研成果的是否为专利获得,url里的isPatent指定是否为专利")
    public ResponseEntity<Page<ResearchFeat>> getByPatentPageable(@PathVariable boolean isPatent, @RequestParam int page,
                                                                  @RequestParam int size) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            log.info("用户："+subject.getPrincipal().toString()+"请求专利类型的科研成果信息");
            return new ResponseEntity<>(researchFeatService.findByIsPatent(isPatent, page, size), HttpStatus.OK);
        }
        log.info("未登录用户请求科研成果中的所有专利信息");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/get/by/level/{level}")
    @ApiOperation("根据科研成果的级别获得,url里的level为级别")
    public ResponseEntity<Page<ResearchFeat>> getByLevelPageable(@PathVariable int level, @RequestParam int page,
                                                                 @RequestParam int size) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            log.info("用户："+subject.getPrincipal().toString()+"请求"+level+"级别的科研成果信息");
            return new ResponseEntity<>(researchFeatService.findByLevel(level, page, size), HttpStatus.OK);
        }
        log.info("未登录用户请求科研成果中的所有级别为"+level+"的专利信息");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除id所指定的科研成果信息，需要feat:delete权限")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("feat:delete")) {
            if (researchFeatService.deleteOne(id)) {
                log.info("用户:"+subject.getPrincipal().toString()+"删除id="+id+"的科研成果");
                return new ResponseEntity<>(HttpStatus.OK);
            }
            log.info("用户:"+subject.getPrincipal().toString()+"删除id="+id+"的科研成果失败");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (subject.getPrincipal() == null) {
            log.info("未登录用户");
        }else{

            log.info("用户:"+subject.getPrincipal().toString()+"不具备feat:delete权限");
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update")
    @ApiOperation("更新传过来的科研成果，注意先查再改，更新是直接赋值实现的，需要有feat:update权限")
    public ResponseEntity<String> updateById(@RequestBody ResearchFeat feat) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("feat:update")) {
            if (researchFeatService.update(feat)) {
                log.info("用户:"+subject.getPrincipal().toString()+"更新id="+feat.getId()+"的科研成果");
                return new ResponseEntity<>(HttpStatus.OK);
            }
            log.info("用户:"+subject.getPrincipal().toString()+"更新科研成果失败");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (subject.getPrincipal() == null) {
            log.info("未登录用户");
        }else{

            log.info("用户:"+subject.getPrincipal().toString()+"不具备feat:update");
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


    }

    @PostMapping("/add")
    @ApiOperation("添加一个科研成果，直接传一个对象，需要feat:create权限")
    public ResponseEntity<ResearchFeat> addOne(@RequestBody ResearchFeat feat) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("feat:create")) {
            log.info("用户："+subject.getPrincipal().toString()+"创建新的科研成果");
            return new ResponseEntity<>(researchFeatService.addOne(feat), HttpStatus.OK);
        }
        if (subject.getPrincipal() == null) {
            log.info("未登录用户");
        }else{

            log.info("用户:"+subject.getPrincipal().toString()+"不具备feat:create");
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/add/resource/{id}")
    @ApiOperation("添加一个资源到id所指定的科研成果中，需要feat:update权限")
    @ApiImplicitParam(name = "resource", value = "要添加的资源")
    public ResponseEntity<String> addOneResource(@PathVariable Long id,
                                                 @RequestBody FileResource resource) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("feat:update")) {
            if (researchFeatService.addOneResource(id, resource)) {
                log.info("用户:"+subject.getPrincipal().toString()+"为id="+id+"的科研成果添加一项文件资源");
                return new ResponseEntity<>(HttpStatus.OK);
            }
            log.info("用户:"+subject.getPrincipal().toString()+"为id="+id+"的科研成果添加一项文件资源失败");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (subject.getPrincipal() == null) {
            log.info("未登录用户");
        }else{

            log.info("用户:"+subject.getPrincipal().toString()+"不具备feat:update");
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/add/resource/{fid}/{rid}")
    @ApiOperation("添加一个rid指定的资源到fid所指定的科研成果中，需要feat:update权限")
    @ApiImplicitParam(name = "resource", value = "要添加的资源")
    public ResponseEntity<String> addOneResource(@PathVariable Long fid,
                                                 @PathVariable Long rid) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("feat:update")) {
            if (researchFeatService.addOneResource(fid, rid)) {
                log.info("用户:"+subject.getPrincipal().toString()+"为id="+fid+"的科研成果添加一项已有文件资源(id="+rid+")");
                return new ResponseEntity<>(HttpStatus.OK);
            }
            log.info("用户:"+subject.getPrincipal().toString()+"为id="+fid+"的科研成果添加一项已有文件资源(id="+rid+")失败");
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (subject.getPrincipal() == null) {
            log.info("未登录用户");
        }else{

            log.info("用户:"+subject.getPrincipal().toString()+"不具备feat:update");
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/get/project/{id}")
    @ApiOperation("获得由id指定的科研成果所依托的项目，结果是分页的")
    public ResponseEntity<Page<ResearchProject>> getProjectByFeatId(@PathVariable Long id,
                                                                    @RequestParam int page,
                                                                    @RequestParam int size) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("未登录用户请求id="+id+"的科研成果信息");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        HashSet<ResearchProject> resultSet = new HashSet<>();
        if (subject.isAuthenticated()) {
            List<ResearchProject> allProjects = researchProjectService.findAll();
            allProjects.forEach(i -> i.getFeats().forEach(f -> {
                if (f.getId().equals(id)) {
                    resultSet.add(i);
                }
            }));
        }

        List<ResearchProject> resultList = resultSet.stream().collect(Collectors.toList());

        Page<ResearchProject> resultPage = myUtils.listConvertToPage(resultList, PageRequest.of(page, size));
        if (resultPage == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("用户:"+subject.getPrincipal().toString()+"请求id="+id+"的科研成果信息");
        return new ResponseEntity<>(resultPage, HttpStatus.OK);

    }

    @GetMapping("/get/user/{id}")
    @ApiOperation("获得由id指定的科研成果的相关作者，结果是分页的")
    public ResponseEntity<Page<User>> getUsersFromFeat(@RequestParam int page,
                                                       @RequestParam int size,
                                                       @PathVariable Long id) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("未登录用户请求id="+id+"的科研成果信息");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<ResearchFeat> op = researchFeatService.findById(id);
        if (op.isPresent()) {
            List<User> userList = op.get().getUsers();
            Page<User> userPage = myUtils.listConvertToPage(userList, PageRequest.of(page, size));
            log.info("用户:"+subject.getPrincipal().toString()+" 请求id="+id+"的科研成果信息");
            return new ResponseEntity<>(userPage, HttpStatus.OK);
        }
        log.info("用户:"+subject.getPrincipal().toString()+" 请求id="+id+"的科研成果信息，未找到该信息");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
