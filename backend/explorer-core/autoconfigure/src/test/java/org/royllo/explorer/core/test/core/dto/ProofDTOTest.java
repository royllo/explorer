package org.royllo.explorer.core.test.core.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.proof.ProofDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("ProofDTO tests")
public class ProofDTOTest {

    @Test
    @DisplayName("Simple raw proof")
    public void simpleRawProof() {
        ProofDTO proof = ProofDTO.builder()
                .rawProof("AABBBBBBBBCC")
                .build();
        assertEquals("AABBBBBBBBCC", proof.getRawProof());
        assertEquals("AAB...BCC", proof.getRawProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, rawProofAbstract=AAB...BCC)", proof.toString());
    }

    @Test
    @DisplayName("Null raw proof")
    public void nullRawProof() {
        ProofDTO proof = ProofDTO.builder()
                .build();
        assertNull(proof.getRawProof());
        assertNull(proof.getRawProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, rawProofAbstract=null)", proof.toString());
    }

    @Test
    @DisplayName("Small raw proof")
    public void smallRawProof() {
        // Raw proof size = 0.
        ProofDTO proof = ProofDTO.builder().rawProof("").build();
        assertEquals("", proof.getRawProof());
        assertEquals("", proof.getRawProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, rawProofAbstract=)", proof.toString());

        // Raw proof size = 1.
        proof = ProofDTO.builder().rawProof("A").build();
        assertEquals("A", proof.getRawProof());
        assertEquals("A", proof.getRawProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, rawProofAbstract=A)", proof.toString());

        // Raw proof size = 2.
        proof = ProofDTO.builder().rawProof("AB").build();
        assertEquals("AB", proof.getRawProof());
        assertEquals("AB", proof.getRawProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, rawProofAbstract=AB)", proof.toString());

        // Raw proof size = 3.
        proof = ProofDTO.builder().rawProof("ABC").build();
        assertEquals("ABC", proof.getRawProof());
        assertEquals("ABC", proof.getRawProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, rawProofAbstract=ABC)", proof.toString());

        // Raw proof size = 4.
        proof = ProofDTO.builder().rawProof("ABCD").build();
        assertEquals("ABCD", proof.getRawProof());
        assertEquals("ABCD", proof.getRawProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, rawProofAbstract=ABCD)", proof.toString());

        // Raw proof size = 5.
        proof = ProofDTO.builder().rawProof("ABCDE").build();
        assertEquals("ABCDE", proof.getRawProof());
        assertEquals("ABCDE", proof.getRawProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, rawProofAbstract=ABCDE)", proof.toString());

        // Raw proof size = 6.
        proof = ProofDTO.builder().rawProof("ABCDEF").build();
        assertEquals("ABCDEF", proof.getRawProof());
        assertEquals("ABCDEF", proof.getRawProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, rawProofAbstract=ABCDEF)", proof.toString());

        // Raw proof size = 7.
        proof = ProofDTO.builder().rawProof("ABCDEFG").build();
        assertEquals("ABCDEFG", proof.getRawProof());
        assertEquals("ABC...EFG", proof.getRawProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, rawProofAbstract=ABC...EFG)", proof.toString());
    }

}
