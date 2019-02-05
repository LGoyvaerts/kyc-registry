package ch.ti8m.codecamp.kycoftrust.kycregister.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
public class UsernameEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(unique = true, nullable = false, name = "username")
    private String username;

    @Column(nullable = false)
    private ZonedDateTime registred;

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

    public ZonedDateTime getRegistred() {
        return registred;
    }

    public void setRegistred(ZonedDateTime registred) {
        this.registred = registred;
    }

    @Override
    public String toString() {
        return "Username{" +
                "id = " + id +
                ", username = " + username
                + "}";
    }
}
