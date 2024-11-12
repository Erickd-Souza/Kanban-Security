package com.security.kanbam.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;
    private String name;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public enum Values{
        ADMIN(1L),
        BASIC(2L);

        long roleid;

        Values(Long roleid){
            this.roleid = roleid;
        }

        public long getRoleid() {
            return roleid;
        }
    }

}
