package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.ResearchProject;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.MessageService;
import com.academicresearchplatformbackend.service.ResearchProjectService;
import com.academicresearchplatformbackend.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/project")
@Api("MessageController")
public class ResearchProjectController {
    private ResearchProjectService researchProjectService;
    private MessageService messageService;
    private UserService userService;

    @Autowired
    public void setResearchProjectService(ResearchProjectService researchProjectService) {
        this.researchProjectService = researchProjectService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/add")
    @ApiOperation("对应创建一个新的项目，自动向负责人发送审核的消息，需要project:create权限")
    @ApiImplicitParam(name = "project", value = "要添加的项目，其中用户必须放入当前用户，剩余的成果与文件可以以后添加")
    public ResponseEntity<ResearchProject> addOne(@RequestBody ResearchProject project) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") ||
                subject.isPermitted("project:create")) {
            ResearchProject p = researchProjectService.addOne(project);
            User user = userService.findByUsername(subject.getPrincipal().toString());
            Long rid = user.getOrganization().getPrincipal().getId();
            Long sid = user.getId();
            messageService.sendMessage(rid, sid, "你有新的项目待审核，请查看");
            return new ResponseEntity<>(p, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update/{id}")
    @ApiOperation("修改id所对应的项目信息，先查再改，需要project:update权限")
    public ResponseEntity<String> updateOne(@RequestBody ResearchProject project) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") ||
                subject.isPermitted("project:create")) {
            if(researchProjectService.update(project))
                return new ResponseEntity<>(HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update/add/user/{pid}/{uid}")
    @ApiOperation("pid对应的项目中加入一个uid对应的用户，需要project:update权限")
    public ResponseEntity<String> addUserToProject(@PathVariable Long pid,
                                                   @PathVariable Long uid) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") ||
                subject.isPermitted("project:create")) {
            if(researchProjectService.addUser(pid, uid))
                return new ResponseEntity<>(HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update/add/feat/{pid}/{fid}")
    @ApiOperation("pid对应的项目中加入一个fid对应的成果，考虑到成果和项目可以单独添加，因此传入二者id，需要project:update权限")
    public ResponseEntity<String> addFeatToProject(@PathVariable Long pid,
                                                       @PathVariable Long fid) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") ||
                subject.isPermitted("project:create")) {
            if(researchProjectService.addFeat(pid, fid))
                return new ResponseEntity<>(HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update/add/resource/{pid}")
    @ApiOperation("向pid对应的项目中添加一项文件（资源），需要project:update权限")
    @ApiImplicitParam(name = "resource", value = "要添加的资源对象")
    public ResponseEntity<String> addResourceToProject(@PathVariable Long pid,
                                                       @RequestBody FileResource fileResource) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") ||
                subject.isPermitted("project:create")) {
            if(researchProjectService.addResource(pid,fileResource))
                return new ResponseEntity<>(HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update/add/resource/{pid}/{rid}")
    @ApiOperation("向pid对应的项目中添加一项rid对应的文件（资源），需要project:update权限")
    public ResponseEntity<String> addResourceToProject(@PathVariable Long pid,
                                                       @PathVariable Long rid) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") ||
                subject.isPermitted("project:create")) {
            if(researchProjectService.addResource(pid,rid))
                return new ResponseEntity<>(HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/censor/{pid}")
    @ApiOperation("审查pid指定的项目，需要，需要project:censor权限")
    public ResponseEntity<String> censorProject(@PathVariable Long pid) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") ||
                subject.isPermitted("project:censor")) {
            if(researchProjectService.censor(pid))
                return new ResponseEntity<>(HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        researchProjectService.censor(pid);
    }




}
