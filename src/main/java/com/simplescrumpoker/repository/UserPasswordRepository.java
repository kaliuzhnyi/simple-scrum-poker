package com.simplescrumpoker.repository;

import com.simplescrumpoker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPasswordRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query(nativeQuery = true,
            value = "insert into " +
                    "users_remind_password (user_id, uuid, created_date, status) " +
                    "values (:userId, :uuid, now(), true)")
    void remindPasswordAdd(Long userId, String uuid);

    @Query(nativeQuery = true,
            value = "select " +
                    "case when count(1) > 0 then true else false end " +
                    "from users_remind_password " +
                    "where uuid = :uuid and status = true")
    boolean remindPasswordUuidValid(String uuid);

    @Modifying
    @Query(nativeQuery = true,
            value = "update users_remind_password " +
                    "set status = :status " +
                    "where uuid = :uuid")
    void remindPasswordSetStatus(String uuid, boolean status);

    @Query(nativeQuery = true,
            value = "select urp.user_id " +
                    "from users_remind_password urp " +
                    "where urp.uuid = :uuid and urp.status = true")
    Optional<Long> remindPasswordGetUserId(String uuid);

}
