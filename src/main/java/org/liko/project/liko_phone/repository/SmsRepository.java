package org.liko.project.liko_phone.repository;

import org.liko.project.liko_phone.entity.Sms;
import org.springframework.data.repository.CrudRepository;

/**
 * @Author: Liko
 * @Description:
 * @Date: Created at 19:42 2018/12/15
 */
public interface SmsRepository extends CrudRepository<Sms, Long> {
}
