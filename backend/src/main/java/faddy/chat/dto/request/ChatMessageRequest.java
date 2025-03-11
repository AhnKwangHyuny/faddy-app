package faddy.chat.dto.request;

import faddy.chat.type.ContentType;
import lombok.Builder;
import lombok.Getter;

@Builder
public record ChatMessageRequest( String content , ContentType contentType , String token){

}