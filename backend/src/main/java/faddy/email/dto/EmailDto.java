package faddy.email.dto;

import lombok.Data;

@Data
public class EmailDto {

    private String email;

    public EmailDto() {
    }

    public EmailDto(String email) {
        this.email = email;
    }
}
