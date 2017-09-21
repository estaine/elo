package com.estaine.elo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@NoArgsConstructor

@Entity
public class Player extends BaseModel {

    @Column(nullable = false, unique = true)
    private String username;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String slackId;

    @Column
    private String imChannel;


    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFormattedSlackId() {
        return "<@" + slackId + ">";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Player player = (Player) o;

        return username.equals(player.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return getFullName() + "[" + username + "]";
    }
}
