package org.liko.project.liko_phone.entity;

import javax.persistence.*;

/**
 * @Author: Liko
 * @Description:
 * @Date: Created at 19:35 2018/12/15
 */
@Entity
public class Sms {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String phoneNumber;

    @Column
    private String msg;

    public Sms() {

    }

    public Sms(String phoneNumber, String msg) {
        this.phoneNumber = phoneNumber;
        this.msg = msg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
