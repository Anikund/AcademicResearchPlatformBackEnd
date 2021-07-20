package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.Lecture;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.LectureService;
import com.academicresearchplatformbackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/lecture")
@Api("Lecture Controller，全部api都需要登录")
@Log4j2
public class LectureController {
    private LectureService lectureService;
    private UserService userService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setLectureService(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping("/all")
    @ApiOperation("获得所有讲座信息,分页")
    public ResponseEntity<Page<Lecture>> getAllPageable(@RequestParam int page,
                                                        @RequestParam int size) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("未授权用户请求讲座信息");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        log.info("用户请求讲座信息");
        return new ResponseEntity<>(lectureService.findAllPageable(page, size), HttpStatus.OK);
    }

    @GetMapping("/get/by/type/{type}")
    @ApiOperation("根据类型获得讲座信息，分页")
    public ResponseEntity<Page<Lecture>> getByTypePageable(@RequestParam int page,
                                                           @RequestParam int size,
                                                           @PathVariable String type) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("未授权用户请求讲座信息");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        log.info("用户请求讲座信息");
        return new ResponseEntity<>(lectureService.findAllByType(type, page, size), HttpStatus.OK);
    }

    @GetMapping("/get/by/level/{level}")
    @ApiOperation("根据类型获得讲座信息，分页")
    public ResponseEntity<Page<Lecture>> getByLevelPageable(@RequestParam int page,
                                                            @RequestParam int size,
                                                            @PathVariable String level) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("未授权用户请求讲座信息");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        log.info("用户请求讲座信息");
        return new ResponseEntity<>(lectureService.findAllByLevel(level, page, size), HttpStatus.OK);
    }

    @GetMapping("/get/by/id/{id}")
    @ApiOperation("根据id获取讲座信息")
    public ResponseEntity<Lecture> getById(@PathVariable Long id) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            log.info("未授权用户请求讲座信息");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Optional<Lecture> op = lectureService.getById(id);
        if (op.isPresent()) {
            Optional<Lecture> tempOp = lectureService.getById(id);
            if (tempOp.isPresent()) {

                log.info("用户：" + SecurityUtils.getSubject().getPrincipal().toString() + "请求id为" + id + "的讲座信息");
                return new ResponseEntity<>(tempOp.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("用户：" + SecurityUtils.getSubject().getPrincipal().toString() + "请求id为" + id + "的讲座信息失败，无此id的讲座");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除id所指向讲座的信息，需要lecture:delete权限")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {

        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("lecture:delete")) {
            if (lectureService.deleteById(id)) {
                log.info("用户：" + SecurityUtils.getSubject().getPrincipal().toString() + "删除id为" + id + "的讲座信息");
                return new ResponseEntity<>(HttpStatus.OK);
            }
            log.info("用户：" + SecurityUtils.getSubject().getPrincipal().toString() + "请求id为" + id + "的讲座信息失败");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("未授权用户请求删除讲座信息");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/add")
    @ApiOperation("新加一个讲座信息，传一个对象,需要lecture:create权限")
    public ResponseEntity<Lecture> addOne(@RequestBody Lecture lecture) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("lecture:create")) {
            log.info("用户：" + SecurityUtils.getSubject().getPrincipal().toString() + "创建讲座信息，其内容为：\n" + lecture.toString());
            return new ResponseEntity<>(lectureService.addOne(lecture), HttpStatus.OK);
        }
        log.info("未授权用户请求添加讲座信息");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/add/user/{id}")
    @ApiOperation("让当前登录用户参加id所指定的会议，需要有lecture:attend权限")
    public ResponseEntity<String> attendLecture(@PathVariable Long id) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("lecture:attend")) {
            User user = userService.findByUsername(subject.getPrincipal().toString());
            lectureService.addUser(id, user);
            log.info("用户：" + user.getUsername() + "报名参加了讲座(id=" + id+")");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        log.info("未授权用户请求添加讲座参与人员");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/add/resource/{id}")
    @ApiOperation("给id所指定的会议添加新的文件资源(放在requestBody里），需要有lecture:update权限")
    public ResponseEntity<String> addResourceToLecture(@PathVariable Long id,
                                                       @RequestBody FileResource fileResource) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("lecture:attend")) {
            if (lectureService.addResource(id, fileResource)) {
                log.info("用户：" + subject.getPrincipal().toString() + "为讲座(id=" + id + ")添加了一项文件资源，其内容如下：\n" + fileResource.toString());
                return new ResponseEntity<>(HttpStatus.OK);
            }
            log.info("用户：" + subject.getPrincipal().toString() + "为讲座(id=" + id + ")添加一项文件资源，失败，因无此id的会议");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        }
        log.info("未授权用户请求为讲座添加文件资源");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/update/attributes/{id}/{field}/{value}")
    @ApiOperation("更新指定的属性，field指定属性名字（与实体类相应字段相同），value指定，需要lecture:update权限")
    public ResponseEntity<String> updateAttributes(@PathVariable Long id,
                                                   @PathVariable String field,
                                                   @PathVariable String value) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("lecture:update")) {
            if (lectureService.update(id, field, value)) {
                log.info("用户" + subject.getPrincipal().toString() + "为讲座(id=" + id + ")更新了属性，更新字段为\"" + field + "\",更新后属性为:" + value);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            log.info("用户" + subject.getPrincipal().toString() + "为讲座(id=" + id + ")更新属性，失败");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);


        }
        log.info("未授权用户请求为讲座添加文件资源");
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
