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
                .proof("AABBBBBBBBCC")
                .build();
        assertEquals("AABBBBBBBBCC", proof.getProof());
        assertEquals("AAB...BCC", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofAbstract=AAB...BCC)", proof.toString());
    }

    @Test
    @DisplayName("Null raw proof")
    public void nullRawProof() {
        ProofDTO proof = ProofDTO.builder()
                .build();
        assertNull(proof.getProof());
        assertNull(proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofAbstract=null)", proof.toString());
    }

    @Test
    @DisplayName("Small raw proof")
    public void smallRawProof() {
        // Raw proof size = 0.
        ProofDTO proof = ProofDTO.builder().proof("").build();
        assertEquals("", proof.getProof());
        assertEquals("", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofAbstract=)", proof.toString());

        // Raw proof size = 1.
        proof = ProofDTO.builder().proof("A").build();
        assertEquals("A", proof.getProof());
        assertEquals("A", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofAbstract=A)", proof.toString());

        // Raw proof size = 2.
        proof = ProofDTO.builder().proof("AB").build();
        assertEquals("AB", proof.getProof());
        assertEquals("AB", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofAbstract=AB)", proof.toString());

        // Raw proof size = 3.
        proof = ProofDTO.builder().proof("ABC").build();
        assertEquals("ABC", proof.getProof());
        assertEquals("ABC", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofAbstract=ABC)", proof.toString());

        // Raw proof size = 4.
        proof = ProofDTO.builder().proof("ABCD").build();
        assertEquals("ABCD", proof.getProof());
        assertEquals("ABCD", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofAbstract=ABCD)", proof.toString());

        // Raw proof size = 5.
        proof = ProofDTO.builder().proof("ABCDE").build();
        assertEquals("ABCDE", proof.getProof());
        assertEquals("ABCDE", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofAbstract=ABCDE)", proof.toString());

        // Raw proof size = 6.
        proof = ProofDTO.builder().proof("ABCDEF").build();
        assertEquals("ABCDEF", proof.getProof());
        assertEquals("ABCDEF", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofAbstract=ABCDEF)", proof.toString());

        // Raw proof size = 7.
        proof = ProofDTO.builder().proof("ABCDEFG").build();
        assertEquals("ABCDEFG", proof.getProof());
        assertEquals("ABC...EFG", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofAbstract=ABC...EFG)", proof.toString());
    }

}
