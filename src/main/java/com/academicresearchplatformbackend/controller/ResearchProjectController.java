package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.FileResource;
import com.academicresearchplatformbackend.dao.ResearchProject;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.MessageService;
import com.academicresearchplatformbackend.service.ResearchProjectService;
import com.academicresearchplatformbackend.service.UserService;
import com.academicresearchplatformbackend.utils.MyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/project")
@Api("MessageController，里面所有api都需要登录")
public class ResearchProjectController {
    private ResearchProjectService researchProjectService;
    private MessageService messageService;
    private UserService userService;
    private MyUtils myUtils;

    @Autowired
    public void setMyUtils(MyUtils myUtils) {
        this.myUtils = myUtils;
    }

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

    @GetMapping("/all")
    @ApiOperation("获得所有项目信息，需要project:view权限")
    public ResponseEntity<Page<ResearchProject>> getAllPageable(@RequestParam int page,
                                                                @RequestParam int size) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("project:view")) {
            return new ResponseEntity<>(researchProjectService.findAllPageable(page, size), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
            if (researchProjectService.update(project))
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
            if (researchProjectService.addUser(pid, uid))
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
            if (researchProjectService.addFeat(pid, fid))
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
            if (researchProjectService.addResource(pid, fileResource))
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
            if (researchProjectService.addResource(pid, rid))
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
            if (researchProjectService.censor(pid))
                return new ResponseEntity<>(HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        researchProjectService.censor(pid);
    }

    @GetMapping("/mine")
    @ApiOperation("返回当前用户参加的项目，需要登录")
    public ResponseEntity<Page<ResearchProject>> getProjectsByUser(@RequestParam int page,
                                                                   @RequestParam int size) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User user = userService.findByUsername(subject.getPrincipal().toString());
        Long userId = user.getId();
        List<ResearchProject> allProjects = researchProjectService.findAll();
        HashSet<ResearchProject> result = new HashSet<>();
        allProjects.forEach(i -> {
            i.getUsers().forEach(u -> {
                if (u.getId() == userId) {
                    result.add(i);
                }
            });
        });
        List<ResearchProject> listResult = result.stream().collect(Collectors.toList());
        Page<ResearchProject> pageResult = myUtils.listConvertToPage(listResult, PageRequest.of(page, size));
        return new ResponseEntity<>(pageResult, HttpStatus.OK);
//        Page<ResearchProject> pageResult = new PageImpl<ResearchProject>();
    }

    @GetMapping("/days/toMid/{pid}")
    @ApiOperation("获得当前日期减去此项目中期检查时间的日数，如果返回-999则代表失败，正数代表超期了，负数代表就还没到期，只需要登录即可")
    @ApiImplicitParam(name = "pid", value = "项目的id")
    public ResponseEntity<Integer> getDaysToProjectMid(@PathVariable Long pid) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        int days = researchProjectService.daysToMidwayTime(pid);
        return new ResponseEntity<>(days, HttpStatus.OK);
    }

    @GetMapping("/days/toEnd/{pid}")
    @ApiOperation("获得当前日期减去此项目截止日期的日数，如果返回-999则代表失败，正数代表超期了，负数代表就还没到期，只需要登录即可")
    @ApiImplicitParam(name = "pid", value = "项目的id")
    public ResponseEntity<Integer> getDaysToProjectEnd(@PathVariable Long pid) {
        Subject subject = SecurityUtils.getSubject();
        if (!subject.isAuthenticated()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        int days = researchProjectService.daysToTerminatingTime(pid);
        return new ResponseEntity<>(days, HttpStatus.OK);
    }

    @PutMapping("/terminate/{pid}")
    @ApiOperation("将pid所代表的项目终止，需要project:terminate权限")
    @ApiImplicitParam(name = "pid", value = "需要终止的项目id")
    public ResponseEntity<String> terminateProject(@PathVariable Long pid) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") ||
                subject.isPermitted("project:terminate")) {
            if (researchProjectService.terminate(pid))
                return new ResponseEntity<>(HttpStatus.OK);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }

    @PutMapping("/fund/assign/{pid}/{amount}")
    @ApiOperation("给pid指定的项目赋予amount多的资金，需要project:assign权限")
    public ResponseEntity<String> assignFundToProject(@PathVariable Long pid,
                                                      @PathVariable Integer amount) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("project:assign")) {
            if (researchProjectService.assignFund(pid, amount)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/fund/cosume/{pid}/{amount}")
    @ApiOperation("给pid所指定的项目进行资金消耗，消耗数量为amount，需要project:consumefund权限并且用户参加了该项目")
    public ResponseEntity<String> consumeFundForProject(@PathVariable Long pid,
                                                        @PathVariable Integer amount) {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isPermitted("super") || subject.isPermitted("project:consumefund")) {
            User user = userService.findByUsername(subject.getPrincipal().toString());
            Optional<ResearchProject> project = researchProjectService.findById(pid);
            if (project.isPresent()) {
                if (project.get().getUsers().contains(user)) {
                    if (researchProjectService.consumeFund(pid, amount)) {
                        return new ResponseEntity<>(HttpStatus.OK);
                    }
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
