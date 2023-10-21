package org.royllo.explorer.core.domain.asset;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.core.domain.user.User;
import org.royllo.explorer.core.util.base.BaseDomain;

import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Taproot asset group.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "ASSET_GROUP")
public class AssetGroup extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** Asset creator. */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "FK_USER_CREATOR", nullable = false)
    private User creator;

    /** A signature over the genesis point using the above key. */
    // TODO Rename to asset witness or suppress ?
    @Column(name = "ASSET_ID_SIG")
    private String assetIdSig;

    /** The raw group key which is a normal public key. */
    @Column(name = "RAW_GROUP_KEY")
    private String rawGroupKey;

    /** The tweaked group key, which is derived based on the genesis point and also asset type. */
    @Column(name = "TWEAKED_GROUP_KEY")
    private String tweakedGroupKey;

}
