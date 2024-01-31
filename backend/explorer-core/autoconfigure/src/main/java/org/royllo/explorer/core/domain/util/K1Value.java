package org.royllo.explorer.core.domain.util;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.core.util.base.BaseDomain;

import static lombok.AccessLevel.PACKAGE;

/**
 * K1 Value created by the system.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor(access = PACKAGE)
@Builder
@Entity
@Table(name = "UTIL_K1_CACHE")
public class K1Value extends BaseDomain {

    /** K1 (Unique identifier). */
    @Id
    @Column(name = "K1")
    private String k1;

}
