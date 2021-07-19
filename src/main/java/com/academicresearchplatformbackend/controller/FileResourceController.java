package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.service.FileResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/resource")
@Api("FileResource Controller，里面的所有api都需要登录")
public class FileResourceController {
    private FileResourceService fileResourceService;

    @Autowired
    public void setFileResourceService(FileResourceService fileResourceService) {
        this.fileResourceService = fileResourceService;
    }

    @GetMapping("/all")
    @ApiOperation("获得所有资源（文件）")
    public ResponseEntity<Page<FileResource>> getAllPageable(@PathVariable int page,
                                                             @PathVariable int size) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return new ResponseEntity<>(fileResourceService.findAllPageable(page, size), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update")
    @ApiOperation("更新一个资源，需要传入一整个对象，需要resource:update权限")
    public ResponseEntity<String> update(@RequestBody FileResource fileResource) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("resource:update")) {
            fileResourceService.update(fileResource);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/add")
    @ApiOperation("添加一个新资源,需要有resource:create权限")
    public ResponseEntity<FileResource> addOne(FileResource fileResource) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("resource:create")) {
            return new ResponseEntity<>(fileResourceService.add(fileResource), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除一个资源，由其id指定，需要resource:delete权限")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("resource:delete")) {
            fileResourceService.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/update/name/{id}/{name}")
    @ApiOperation("更新一个资源的名字，资源由id指定，name指定要更新成的名字，需要resource:update权限")
    public ResponseEntity<String> updateName(@PathVariable Long id, @PathVariable String name) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("resource:update")) {
            fileResourceService.updateName(id,name);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
