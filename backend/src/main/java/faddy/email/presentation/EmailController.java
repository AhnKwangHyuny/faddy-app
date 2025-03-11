package faddy.email.presentation;

import faddy.email.dto.EmailCheckDto;
import faddy.email.service.MailService;
import faddy.global.exception.BadRequestException;
import faddy.global.exception.ExceptionCode;
import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/emails")
public class EmailController {

    private final MailService mailService;


    @Autowired
    public EmailController(MailService mailService) {
        this.mailService = mailService;
    }


    @PostMapping("/mailAuthCheck")
    public String AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto){
        Boolean Checked=mailService.CheckAuthNum(emailCheckDto.getEmail(),emailCheckDto.getAuthNum());
        if(Checked){
            return "ok";
        }
        else{
            throw new BadRequestException(ExceptionCode.INVALID_REQUEST);
        }
    }
 

}
