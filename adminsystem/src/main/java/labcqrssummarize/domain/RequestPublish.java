package labcqrssummarize.domain;

import java.util.*;
import labcqrssummarize.domain.*;
import labcqrssummarize.infra.AbstractEvent;
import lombok.*;

@Data
@ToString
public class RequestPublish extends AbstractEvent {
//이거 코드 Command->Policy 흐름 때문에 잘못 생성된 코드 지우고, RequestContentDenied.java 생성하고 PolicyHandler.java에서 해당 부분 삭제해야함.
    private Long id;
}
