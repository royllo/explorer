package org.royllo.explorer.backend.domain.bitcoin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.royllo.explorer.backend.util.base.BaseDomain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Information about a Bitcoin transaction output.
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "BITCOIN_TRANSACTION_OUTPUTS")
public class TransactionOutput extends BaseDomain {

    /** Unique identifier. */
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    /** Block height. */
    @Column(name = "BLOCK_HEIGHT")
    private Integer blockHeight;

    /** Transaction id. */
    @NotBlank(message = "Transaction id is mandatory")
    @Column(name = "TXID", updatable = false)
    private String txId;

    /** Output index (starts at 0). */
    @Column(name = "VOUT", updatable = false)
    private int vout;

    /** ScriptPubKey is the script which controls how bitcoin can be spent. */
    @Column(name = "SCRIPTPUBKEY")
    private String scriptPubKey;

    /** ScriptPubKey asm is the symbolic representation of the Bitcoin's Script language op-codes. */
    @Column(name = "SCRIPTPUBKEY_ASM")
    private String scriptPubKeyAsm;

    /** ScriptPubKey type (p2pkh, p2wsh...). */
    @Column(name = "SCRIPTPUBKEY_TYPE")
    private String scriptPubKeyType;

    /** ScriptPubKey address. */
    @Column(name = "SCRIPTPUBKEY_ADDRESS")
    private String scriptPubKeyAddress;

    /** Value is the number of Satoshi (1 BTC = 100,000,000 Satoshi). */
    @Column(name = "VALUE")
    private BigDecimal value;

}
