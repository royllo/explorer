package org.royllo.explorer.core.dto.proof;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static lombok.AccessLevel.PRIVATE;

/**
 * Proof DTO.
 */
@Value
@Builder
@AllArgsConstructor(access = PRIVATE)
@SuppressWarnings("checkstyle:VisibilityModifier")
public class ProofDTO {

    /** Unique identifier. */
    Long id;


    /**
     * Proof id (rawProof + ":" + proofIndex).
     *
     * @return calculated proof ID.
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public String getProofId() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest((rawProof + ":" + proofIndex).getBytes(StandardCharsets.UTF_8));
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 is not available:" + e.getMessage());
        }
    }

    /** Raw proof. */
    String rawProof;

    /** Proof index. */
    long proofIndex;


    // TODO Add author!

}
