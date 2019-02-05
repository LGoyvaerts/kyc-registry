package ch.ti8m.codecamp.kycoftrust.kycregister.domain;

public class BlockRequest {
    private Long blockId;
    private String uuId;

    public BlockRequest() {
    }

    public BlockRequest(Long blockId, String uuId) {
        this.blockId = blockId;
        this.uuId = uuId;
    }

    public Long getBlockId() {
        return blockId;
    }

    public void setBlockId(Long blockId) {
        this.blockId = blockId;
    }

    public String getUuId() {
        return uuId;
    }

    public void setUuId(String uuId) {
        this.uuId = uuId;
    }
}
