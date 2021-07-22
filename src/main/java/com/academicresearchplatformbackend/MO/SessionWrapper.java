package com.academicresearchplatformbackend.MO;

import lombok.Data;
import org.apache.shiro.session.mgt.SessionKey;
@Data
public class SessionWrapper<T> {
    T data;
    SessionKey sessionKey;

}
