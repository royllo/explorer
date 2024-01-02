package org.royllo.explorer.core.test.core.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.proof.ProofDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.royllo.explorer.core.util.enums.ProofType.PROOF_TYPE_UNSPECIFIED;

@DisplayName("ProofDTO tests")
public class ProofDTOTest {

    @Test
    @DisplayName("Simple proof")
    public void simpleProof() {
        ProofDTO proof = ProofDTO.builder()
                .proof("AABBBBBBBBCC")
                .proofType(PROOF_TYPE_UNSPECIFIED)
                .build();
        assertEquals("AABBBBBBBBCC", proof.getProof());
        assertEquals("AAB...BCC", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofType=PROOF_TYPE_UNSPECIFIED, proofAbstract=AAB...BCC)", proof.toString());
    }

    @Test
    @DisplayName("Null proof")
    public void nullProof() {
        ProofDTO proof = ProofDTO.builder()
                .proofType(PROOF_TYPE_UNSPECIFIED)
                .build();
        assertNull(proof.getProof());
        assertNull(proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofType=PROOF_TYPE_UNSPECIFIED, proofAbstract=null)", proof.toString());
    }

    @Test
    @DisplayName("Small proof")
    public void smallProof() {
        // Proof size = 0.
        ProofDTO proof = ProofDTO.builder().proof("").proofType(PROOF_TYPE_UNSPECIFIED).build();
        assertEquals("", proof.getProof());
        assertEquals("", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofType=PROOF_TYPE_UNSPECIFIED, proofAbstract=)", proof.toString());

        // Proof size = 1.
        proof = ProofDTO.builder().proof("A").proofType(PROOF_TYPE_UNSPECIFIED).build();
        assertEquals("A", proof.getProof());
        assertEquals("A", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofType=PROOF_TYPE_UNSPECIFIED, proofAbstract=A)", proof.toString());

        // Proof size = 2.
        proof = ProofDTO.builder().proof("AB").proofType(PROOF_TYPE_UNSPECIFIED).build();
        assertEquals("AB", proof.getProof());
        assertEquals("AB", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofType=PROOF_TYPE_UNSPECIFIED, proofAbstract=AB)", proof.toString());

        // Proof size = 3.
        proof = ProofDTO.builder().proof("ABC").proofType(PROOF_TYPE_UNSPECIFIED).build();
        assertEquals("ABC", proof.getProof());
        assertEquals("ABC", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofType=PROOF_TYPE_UNSPECIFIED, proofAbstract=ABC)", proof.toString());

        // Proof size = 4.
        proof = ProofDTO.builder().proof("ABCD").proofType(PROOF_TYPE_UNSPECIFIED).build();
        assertEquals("ABCD", proof.getProof());
        assertEquals("ABCD", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofType=PROOF_TYPE_UNSPECIFIED, proofAbstract=ABCD)", proof.toString());

        // Proof size = 5.
        proof = ProofDTO.builder().proof("ABCDE").proofType(PROOF_TYPE_UNSPECIFIED).build();
        assertEquals("ABCDE", proof.getProof());
        assertEquals("ABCDE", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofType=PROOF_TYPE_UNSPECIFIED, proofAbstract=ABCDE)", proof.toString());

        // Proof size = 6.
        proof = ProofDTO.builder().proof("ABCDEF").proofType(PROOF_TYPE_UNSPECIFIED).build();
        assertEquals("ABCDEF", proof.getProof());
        assertEquals("ABCDEF", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofType=PROOF_TYPE_UNSPECIFIED, proofAbstract=ABCDEF)", proof.toString());

        // Proof size = 7.
        proof = ProofDTO.builder().proof("ABCDEFG").proofType(PROOF_TYPE_UNSPECIFIED).build();
        assertEquals("ABCDEFG", proof.getProof());
        assertEquals("ABC...EFG", proof.getProofAbstract());
        assertEquals("ProofDTO(id=null, creator=null, asset=null, proofId=null, proofType=PROOF_TYPE_UNSPECIFIED, proofAbstract=ABC...EFG)", proof.toString());
    }

}
