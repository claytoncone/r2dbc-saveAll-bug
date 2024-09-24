package org.claytoncone.r2dbcsaveallbug.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
    @Id
    @Column("client_id")
    private Long clientId;
    private String email;
    private String phone;
    private String fax;
    private String mobile;

    @Override
    public String toString() {
        return String.format("email %s\nphone: %s     mobile: %s\nFax: %s", email, phone, mobile, fax);
    }
}
