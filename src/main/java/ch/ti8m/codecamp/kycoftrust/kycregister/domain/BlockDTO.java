package ch.ti8m.codecamp.kycoftrust.kycregister.domain;

import java.util.Date;

public class BlockDTO {
    private String userFirstName;
    private String userLastName;
    private Date userBirthday;
    private String userImagePath;
    private String userPublicKey;
    private Long blockIndex;
    private String blockHash;
    private Long userBlockIndexVerifier1;
    private Long userBlockIndexVerifier2;
    private String previousBlockHash;
    private Integer difficulty;
    private Date timestamp;
    private String signature;
    private String boardActivePlaceHash;
    private String boardOwnerHash;
    private String blockType;
    private String boardUuid;


    public String getUserFirstName() {
        if (userFirstName != null) {
            return userFirstName;
        }
        return "";
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        if (userLastName != null) {
            return userLastName;
        }
        return "";
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public Date getUserBirthday() {
        if (userBirthday != null) {
            return userBirthday;
        }
        return null;
    }

    public void setUserBirthday(Date userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }

    public String getUserPublicKey() {
        if (userPublicKey != null) {
            return userPublicKey;
        }
        return "";
    }

    public void setUserPublicKey(String userPublicKey) {
        this.userPublicKey = userPublicKey;
    }

    public Long getBlockIndex() {
        return blockIndex;
    }

    public void setBlockIndex(Long blockIndex) {
        this.blockIndex = blockIndex;
    }

    public String getBlockHash() {
        if (blockHash != null) {
            return blockHash;
        }
        return null;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public Long getUserBlockIndexVerifier1() {
        if (userBlockIndexVerifier1 != null) {
            return userBlockIndexVerifier1;
        }
        return null;
    }

    public void setUserBlockIndexVerifier1(Long userBlockIndexVerifier1) {
        this.userBlockIndexVerifier1 = userBlockIndexVerifier1;
    }

    public Long getUserBlockIndexVerifier2() {
        if (userBlockIndexVerifier2 != null) {
            return userBlockIndexVerifier2;
        }
        return null;
    }

    public void setUserBlockIndexVerifier2(Long userBlockIndexVerifier2) {
        this.userBlockIndexVerifier2 = userBlockIndexVerifier2;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public void setPreviousBlockHash(String previousBlockHash) {
        this.previousBlockHash = previousBlockHash;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getBoardActivePlaceHash() {
        if (boardActivePlaceHash != null) {
            return boardActivePlaceHash;
        }
        return "";
    }

    public void setBoardActivePlaceHash(String boardActivePlaceHash) {
        this.boardActivePlaceHash = boardActivePlaceHash;
    }

    public String getBoardOwnerHash() {
        if (boardOwnerHash != null) {
            return boardOwnerHash;
        }
        return "";
    }

    public void setBoardOwnerHash(String boardOwnerHash) {
        this.boardOwnerHash = boardOwnerHash;
    }

    public String getBlockType() {
        return blockType;
    }

    public void setBlockType(String blockType) {
        this.blockType = blockType;
    }

    public String getBoardUuid() {
        return boardUuid;
    }

    public void setBoardUuid(String boardUuid) {
        this.boardUuid = boardUuid;
    }
}
