package org.royllo.explorer.core.util.base;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.ZonedDateTime;

/**
 * Base domain.
 */
@Getter
@Setter
@ToString
@SuperBuilder
@RequiredArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseDomain {

    /** Data created on. */
    @CreatedDate
    @Column(name = "CREATED_ON", nullable = false, updatable = false)
    private ZonedDateTime createdOn;

    /** Data updated on. */
    @LastModifiedDate
    @Column(name = "UPDATED_ON", insertable = false)
    private ZonedDateTime updatedOn;

}
