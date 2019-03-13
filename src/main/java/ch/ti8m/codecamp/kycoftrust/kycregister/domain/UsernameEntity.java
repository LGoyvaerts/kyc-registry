package ch.ti8m.codecamp.kycoftrust.kycregister.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
public class UsernameEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false, name = "username")
    private String username;

    @Column(nullable = false)
    private Date registred;

    @Column
    private String genesisHash;

    @Column
    private String genesisName;

    @Column
    private String genesisImagepath;

    @Column(name = "genesisCreatedOn")
    private Date genesisCreatedOn;

    public UsernameEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getRegistred() {
        return registred;
    }

    public void setRegistred(Date registred) {
        this.registred = registred;
    }

    public String getGenesisHash() {
        return genesisHash;
    }

    public void setGenesisHash(String genesisHash) {
        this.genesisHash = genesisHash;
    }

    public String getGenesisName() {
        return genesisName;
    }

    public void setGenesisName(String genesisName) {
        this.genesisName = genesisName;
    }

    public String getGenesisImagepath() {
        return genesisImagepath;
    }

    public void setGenesisImagepath(String genesisImagepath) {
        this.genesisImagepath = genesisImagepath;
    }

    public Date getGenesisCreatedOn() {
        return genesisCreatedOn;
    }

    public void setGenesisCreatedOn(Date genesisCreatedOn) {
        this.genesisCreatedOn = genesisCreatedOn;
    }

    @Override
    public String toString() {
        return "UsernameEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", registred=" + registred +
                ", genesisHash='" + genesisHash + '\'' +
                ", genesisName='" + genesisName + '\'' +
                ", genesisImagepath='" + genesisImagepath + '\'' +
                ", genesisCreatedOn=" + genesisCreatedOn +
                '}';
    }
}
