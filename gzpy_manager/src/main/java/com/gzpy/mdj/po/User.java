package com.gzpy.mdj.po;

import java.io.Serializable;
import java.sql.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User implements Serializable{
    private Integer userId;
    private String loginName;
    private String userName;
    private String password;
    private String sex;
    private Date employDate;
    private String email;
    private String officePhone;
    private String homePhone;
    private String mobilePhone;
    private Character delStatus;
    
}
