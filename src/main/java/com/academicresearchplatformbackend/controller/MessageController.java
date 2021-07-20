package com.academicresearchplatformbackend.controller;

import com.academicresearchplatformbackend.dao.Message;
import com.academicresearchplatformbackend.dao.User;
import com.academicresearchplatformbackend.service.Impl.MessageServiceImpl;
import com.academicresearchplatformbackend.service.MessageService;
import com.academicresearchplatformbackend.service.UserService;
import com.academicresearchplatformbackend.utils.MyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/message/user")
@Api("MessageController，里面所有api都需要登录")
@Log4j2
public class MessageController {
    private MessageService messageService;
    private UserService userService;
    private MyUtils myUtils;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setMyUtils(MyUtils myUtils) {
        this.myUtils = myUtils;
    }

    @ApiOperation(value = "根据当前用户返回其收到的、所有已读和未读消息列表，若为admin则返回所有消息，需要message:read权限",
            notes = "若无权限则返回状态UNAUTHORIZED，否则返回Page（可为null）")
    @GetMapping("/received")
    //@RequiresAuthentication
    public ResponseEntity<Page<Message>> getReceivedMessages(@RequestParam int page,
                                                             @RequestParam int size) {
        //super admin
        if (SecurityUtils.getSubject().isPermitted("super")) {
            log.info("管理员权限获得所有消息");
            return new ResponseEntity<>(messageService.getAllMessages(page, size), HttpStatus.OK);
        }
        //no permission to read messages
        if (!SecurityUtils.getSubject().isPermitted("message:read")) {
            log.info("未获得message:read权限，无法获得信息");
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        //can read, return
        User user = myUtils.getCurrentAuthenticatedUser().getData();
        if (user == null) {
            log.info("请求用户无法找到");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("用户"+user.getUsername()+"获得其接受的所有信息");
        return new ResponseEntity<>(messageService.getByReceiver(user, page, size), HttpStatus.OK);
    }

    @GetMapping("/sent")
    @ApiOperation(value = "根据当前用户返回其所有已发送的列表，若为admin则返回所有消息，需要message:read权限",
            notes = "若无权限则返回状态UNAUTHORIZED，否则返回Page（可为null）")
    //@RequiresAuthentication
    public ResponseEntity<Page<Message>> getSentMessages(@RequestParam int page, @RequestParam int size) {
        //super admin
        if (SecurityUtils.getSubject().isPermitted("super")) {
            log.info("super权限获得所有信息");
            return new ResponseEntity<>(messageService.getAllMessages(page, size), HttpStatus.OK);
        }
        //no permission to read messages
        if (!SecurityUtils.getSubject().isPermitted("message:read")) {
            log.info("未获得message:read权限，无法获得已发送信息");
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        //can read, return
        User user = myUtils.getCurrentAuthenticatedUser().getData();
        if (user == null) {
            log.info("当前用户未找到");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info("用户"+user.getUsername()+"获得其已发送的信息");
        return new ResponseEntity<>(messageService.getByTransmitter(user, page, size), HttpStatus.OK);

    }

    @PostMapping("/sendMessage")
    @ApiOperation("发送消息给一个目标用户，需要message:send权限")
    @ApiImplicitParams({@ApiImplicitParam(name = "rUsername", value = "接受者用户名"),
            @ApiImplicitParam(name = "content", value = "内容")})
    //@RequiresAuthentication
    public ResponseEntity<String> sendMessageToOne(@RequestParam String rUsername,
                                                   @RequestBody String content) {
        if (!SecurityUtils.getSubject().isPermitted("message:send")
        && !SecurityUtils.getSubject().isPermitted("super")) {
            log.info("未获得权限发送信息");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        User receiver = userService.findByUsername(rUsername);
        if (receiver == null) {
            log.info("接受者用户名:"+rUsername+",未找到");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {

            System.out.println(content);
            log.info("用户" + myUtils.getCurrentAuthenticatedUser().getData().getUsername() + "发送信息:\n" + content
                    + "\n给用户：" + rUsername);
            messageService.sendMessage(receiver.getId(),
                    myUtils.getCurrentAuthenticatedUser().getData().getId(),
                    content);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PostMapping("/sendMessageToAll")
    @ApiOperation("发送消息给所有用户（包括他自己），需要message:send权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "内容")})
    //@RequiresAuthentication
    public ResponseEntity<String> sendMessageToAll(@RequestBody String content) {
//        if(SecurityUtils.getSubject().isPermitted(""))
        if (!SecurityUtils.getSubject().isPermitted("message:send")
        && !SecurityUtils.getSubject().isPermitted("super")) {
            log.info("当前用户无权限发送信息");
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        {
            System.out.println(content);
            List<User> allUsers = userService.findAll();
            if (allUsers == null) {//no users
                log.info("当前系统内无可用用户");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Long tId = myUtils.getCurrentAuthenticatedUser().getData().getId();
            allUsers.forEach(i -> messageService.sendMessage(i.getId(),
                    tId,
                    content));
            log.info("用户："+myUtils.getCurrentAuthenticatedUser().getData().getUsername()+"发送消息:\n"+content
            +"\n给所有用户");
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @PutMapping("/readMessage")
    @ApiOperation("将id的消息标记为已读")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "消息id")})
    //@RequiresAuthentication
    public ResponseEntity<String> readMessage(Long id) {
        if (SecurityUtils.getSubject().isPermitted("message:read") || SecurityUtils.getSubject().isPermitted("super")) {
            if (messageService.readMessage(id, myUtils.getCurrentAuthenticatedUser().getData().getId())) {
                log.info("用户" + SecurityUtils.getSubject().getPrincipal().toString()
                        + "阅读消息（id=" + id + ")成功，该消息被标记未已读");
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                log.info("当前用户无权限标记此信息未已读");
            }


        }
        log.info("当前用户无权限message:read");

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
