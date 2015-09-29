/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.upm.fiware.rss.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author francisco
 */
@Entity
@Table(name = "roles")
public class Role implements Serializable{
    private String id;
    private String name;
    private Set<RSUser> users;

    @Id
    @Column(name = "ROLE_ID")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name = "ROLE_NAME", length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany
    @JoinTable(name = "USER_ROLES",
        joinColumns =
        {
            @JoinColumn(name = "ROLE_ID")
        },
        inverseJoinColumns =
        {
            @JoinColumn(name = "USER_ID")
    })
    public Set<RSUser> getUsers() {
        return users;
    }

    public void setUsers(Set<RSUser> users) {
        this.users = users;
    }

    
}
