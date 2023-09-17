package org.royllo.explorer.core.test.core.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.royllo.explorer.core.dto.proof.ProofFileDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("ProofDTO tests")
public class ProofFileDTOTest {

    @Test
    @DisplayName("Simple raw proof")
    public void simpleRawProof() {
        ProofFileDTO proof = ProofFileDTO.builder()
                .rawProof("AABBBBBBBBCC")
                .build();
        assertEquals("AABBBBBBBBCC", proof.getRawProof());
        assertEquals("AAB...BCC", proof.getRawProofAbstract());
        assertEquals("ProofFileDTO(id=null, creator=null, asset=null, proofFileId=null, rawProofAbstract=AAB...BCC)", proof.toString());
    }

    @Test
    @DisplayName("Null raw proof")
    public void nullRawProof() {
        ProofFileDTO proof = ProofFileDTO.builder()
                .build();
        assertNull(proof.getRawProof());
        assertNull(proof.getRawProofAbstract());
        assertEquals("ProofFileDTO(id=null, creator=null, asset=null, proofFileId=null, rawProofAbstract=null)", proof.toString());
    }

    @Test
    @DisplayName("Small raw proof")
    public void smallRawProof() {
        // Raw proof size = 0.
        ProofFileDTO proofFile = ProofFileDTO.builder().rawProof("").build();
        assertEquals("", proofFile.getRawProof());
        assertEquals("", proofFile.getRawProofAbstract());
        assertEquals("ProofFileDTO(id=null, creator=null, asset=null, proofFileId=null, rawProofAbstract=)", proofFile.toString());

        // Raw proof size = 1.
        proofFile = ProofFileDTO.builder().rawProof("A").build();
        assertEquals("A", proofFile.getRawProof());
        assertEquals("A", proofFile.getRawProofAbstract());
        assertEquals("ProofFileDTO(id=null, creator=null, asset=null, proofFileId=null, rawProofAbstract=A)", proofFile.toString());

        // Raw proof size = 2.
        proofFile = ProofFileDTO.builder().rawProof("AB").build();
        assertEquals("AB", proofFile.getRawProof());
        assertEquals("AB", proofFile.getRawProofAbstract());
        assertEquals("ProofFileDTO(id=null, creator=null, asset=null, proofFileId=null, rawProofAbstract=AB)", proofFile.toString());

        // Raw proof size = 3.
        proofFile = ProofFileDTO.builder().rawProof("ABC").build();
        assertEquals("ABC", proofFile.getRawProof());
        assertEquals("ABC", proofFile.getRawProofAbstract());
        assertEquals("ProofFileDTO(id=null, creator=null, asset=null, proofFileId=null, rawProofAbstract=ABC)", proofFile.toString());

        // Raw proof size = 4.
        proofFile = ProofFileDTO.builder().rawProof("ABCD").build();
        assertEquals("ABCD", proofFile.getRawProof());
        assertEquals("ABCD", proofFile.getRawProofAbstract());
        assertEquals("ProofFileDTO(id=null, creator=null, asset=null, proofFileId=null, rawProofAbstract=ABCD)", proofFile.toString());

        // Raw proof size = 5.
        proofFile = ProofFileDTO.builder().rawProof("ABCDE").build();
        assertEquals("ABCDE", proofFile.getRawProof());
        assertEquals("ABCDE", proofFile.getRawProofAbstract());
        assertEquals("ProofFileDTO(id=null, creator=null, asset=null, proofFileId=null, rawProofAbstract=ABCDE)", proofFile.toString());

        // Raw proof size = 6.
        proofFile = ProofFileDTO.builder().rawProof("ABCDEF").build();
        assertEquals("ABCDEF", proofFile.getRawProof());
        assertEquals("ABCDEF", proofFile.getRawProofAbstract());
        assertEquals("ProofFileDTO(id=null, creator=null, asset=null, proofFileId=null, rawProofAbstract=ABCDEF)", proofFile.toString());

        // Raw proof size = 7.
        proofFile = ProofFileDTO.builder().rawProof("ABCDEFG").build();
        assertEquals("ABCDEFG", proofFile.getRawProof());
        assertEquals("ABC...EFG", proofFile.getRawProofAbstract());
        assertEquals("ProofFileDTO(id=null, creator=null, asset=null, proofFileId=null, rawProofAbstract=ABC...EFG)", proofFile.toString());
    }

}
