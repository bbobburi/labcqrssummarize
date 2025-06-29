package labcqrssummarize.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.naming.NameParser;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import labcqrssummarize.config.kafka.KafkaProcessor;
import labcqrssummarize.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor

/**
 * PolicyHandler
 * - Kafka에서 발행되는 Event를 수신하는 역할 (Inbound Adaptor)
 * - 비즈니스 정책에 따라 해당 이벤트에 반응해 Command 또는 상태 변화를 트리거함
 * - 직접 상태를 조작하기보단 Aggregate의 비즈니스 메서드를 호출하는 구조로 유지
 */
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    EBookRepository eBookRepository;

    /**
     * 테스트용 또는 전체 메시지 로깅 용도로 남겨둔 메서드
     * 모든 Kafka 메시지를 문자열로 수신 가능
     */
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

     /**
     * [작가 등록 접수] 이벤트 수신
     * - 등록된 작가 정보를 바탕으로 상태 변화 트리거
     * - 신규 작가라면 생성, 기존 작가라면 상태만 업데이트
     * - 여기서는 예시로 자동 승인 처리
     */
    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RegisteredAuthor'"
    )
    public void wheneverRegisteredAuthor_HandleAuthorRegistrationRequest(
        @Payload RegisteredAuthor registeredAuthor
    ) {
        System.out.println("\n\n##### listener HandleAuthorRegistrationRequest : " + registeredAuthor + "\n\n");

        // 1. 기존 작가 조회 또는 신규 생성
        Author author = authorRepository.findById(registeredAuthor.getAuthorId())
            .orElseGet(() -> {
                Author newAuthor = new Author();
                newAuthor.setAuthorId(registeredAuthor.getAuthorId());
                newAuthor.setName(registeredAuthor.getName());
                newAuthor.setUserId(registeredAuthor.getUserId());
                return newAuthor;
            });

        // 2. 비즈니스 로직 예시: 자동 승인
        author.approve();

        // 3. 상태 저장
        authorRepository.save(author);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='WrittenContent'"
    )
    public void wheneverWrittenContent_HandleContentRegistrationRequest(
        @Payload WrittenContent writtenContent
    ) {
        WrittenContent event = writtenContent;
        System.out.println(
            "\n\n##### listener HandleContentRegistrationRequest : " +
            writtenContent +
            "\n\n"
        );
        // Comments //
        //해당 책은 출판된것은 아니고 본인의 서재에서만 확인할 수 있는 본인의 책, 해당 책은 보안 문제만 검열함.(ex. SQL Injection, 서버에 위협을 줄 수 있는 코드 문장 삽입 여부)
        //
        // 굳이 관리자가 안해도 자동으로 요청 처리하는게 편할거 같음

        // Sample Logic //

    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='RequestPublish'"
    )
    public void wheneverRequestPublish_HandlePublishRequest(
        @Payload RequestPublish requestPublish
    ) {
        RequestPublish event = requestPublish;
        System.out.println(
            "\n\n##### listener HandlePublishRequest : " +
            requestPublish +
            "\n\n"
        );
        // Sample Logic //

    }
}
//>>> Clean Arch / Inbound Adaptor
