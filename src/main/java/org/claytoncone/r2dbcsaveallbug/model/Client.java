package org.claytoncone.r2dbcsaveallbug.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    private Long id;

    @Column("given_name")
    private String givenName;

    @Column("middle_initial")
    private String middleInitial;

    @Column("surname")
    private String surname;

    @Column("title")
    private String title;

    @Column("company")
    private String company;

    @Transient
    private Contact contactInfo;

    @Override
    public String toString() {
        return String.format("Client id=%d\nName=%s %s %s\n title=%s, company%s", id, givenName, middleInitial, surname, title, company);
    }
}