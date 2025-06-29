package labcqrssummarize.domain;

import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class RequestPublish extends AbstractEvent {
//이거 코드 이전 도메인 Pub/Sub 구조때문에 생긴것.  RequestContentDenied.java 
    private Long id;
}
