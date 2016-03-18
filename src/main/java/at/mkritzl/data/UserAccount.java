package at.mkritzl.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
public class UserAccount implements Serializable {

    @Id
    @Size(max = 100)
    @NotNull
    private String email;

    @NotNull
    @Size(max = 100)
    private String password;

    public UserAccount(@JsonProperty("email") String email, @JsonProperty("password") String password) {
        this.email = email;
        this.password = password;
    }

    protected UserAccount() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}