package ru.practicum.explorewithme.main.server.user.repository;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.explorewithme.main.server.user.model.User;

import javax.persistence.*;

@Slf4j
public class AuditUserListener {

    @PrePersist
    @PreUpdate
    @PreRemove
    private void beforeAnyUpdate(User user) {
        if (user.getId() == null) {
            log.info("[USER AUDIT] About to add a user");
        } else {
            log.info("[USER AUDIT] About to update/delete user: " + user.getId());
        }
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    private void afterAnyUpdate(User user) {
        log.info("[USER AUDIT] add/update/delete complete for user: " + user.getId());
    }

    @PostLoad
    private void afterLoad(User user) {
        log.info("[USER AUDIT] user loaded from database: " + user.getId());
    }
}
