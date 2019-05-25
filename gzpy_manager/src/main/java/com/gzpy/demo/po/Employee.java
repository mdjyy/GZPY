package com.gzpy.demo.po;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Employee  implements Serializable{
    private Integer id;
    private String lastName;
    private Integer gender;
    private String email;
    private Integer did;
}
