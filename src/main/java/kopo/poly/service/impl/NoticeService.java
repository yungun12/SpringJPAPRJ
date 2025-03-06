package kopo.poly.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.repository.NoticeRepository;
import kopo.poly.repository.entity.NoticeEntity;
import kopo.poly.service.INoticeService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService implements INoticeService {

    // RequiredArgsConstructor 어노테이션으로 생성자를 자동 생성함
    // noticeRepository 변수에 이미 메모리에 올라간 NoticeRepository 객체를 넣어줌
    // 예전에는 autowired 어노테이션을 통해 설정했지만 이제는 생성자를 통해 객체를 주입함

    private final NoticeRepository noticeRepository;

    @Override
    public List<NoticeDTO> getNoticeList() {

        log.info("{}.getNoticeList Start!", this.getClass().getName());

        // 공지사항 전체 리스트 조회하기
        List<NoticeEntity> rList = noticeRepository.findAllByOrderByNoticeSeqDesc();

        // Entity의 값들을 DTO에 맞게 넣어주기
        List<NoticeDTO> nList = new ObjectMapper().convertValue(rList,
                new TypeReference<>() {
                });

        log.info("{}.getNoticeList End!", this.getClass().getName());

        return nList;
    }

    @Transactional
    @Override
    public NoticeDTO getNoticeInfo(NoticeDTO pDTO, boolean type) {

        log.info("{}.getNoticeInfo Start!", this.getClass().getName());

        if (type) {
            // 조회수 증가하기
            int res = noticeRepository.updateReadCnt(pDTO.noticeSeq());

            // 조회수 증가 성공야부 체크
            log.info("res : {}", res);
        }


        // 공지사항 상세내역 가져오기
        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(pDTO.noticeSeq());

        // Entity의 값들을 DTO에 맞게 넣어주기
        NoticeDTO rDTO = new ObjectMapper().convertValue(rEntity, NoticeDTO.class);

        log.info("{}.getNoticeInfo End!", this.getClass().getName());

        return rDTO;
    }

    @Transactional
    @Override
    public void updateNoticeInfo(NoticeDTO pDTO) {

        log.info("{}.updateNoticeInfo Start!", this.getClass().getName());

        Long noticeSeq = pDTO.noticeSeq();

        String title = CmmUtil.nvl(pDTO.title());
        String noticeYn = CmmUtil.nvl(pDTO.noticeYn());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("noticeSeq: {}, title: {}, noticeYn: {}, contents: {}, userId: {}",
                noticeSeq, title, noticeYn, contents, userId);

        // 현재 공지사항 조회수 가져오기
        NoticeEntity rEntity = noticeRepository.findByNoticeSeq(noticeSeq);

        // 수정할 값들을 빌더를 통하여 Entity에 저장하기
        NoticeEntity pEntity = NoticeEntity.builder()
                .noticeSeq(noticeSeq).title(title).noticeYn(noticeYn).contents(contents).userId(userId)
                .readCnt(rEntity.getReadCnt())
                .build();

        // 데이터 수정하기
        noticeRepository.save(pEntity);

        log.info("{}.updateNoticeInfo End!", this.getClass().getName());

    }

    @Override
    public void deleteNoticeInfo(NoticeDTO pDTO) throws Exception {

        log.info("{}.deleteNoticeInfo Start!", this.getClass().getName());

        Long noticeSeq = pDTO.noticeSeq();

        log.info("noticeSeq: {}", noticeSeq);

        // 데이터 삭제하기, JPA 기본 제공하는 함수 사용
        noticeRepository.deleteById(noticeSeq);

        log.info("{}.deleteNoticeInfo End!", this.getClass().getName());
    }

    @Override
    public void insertNoticeInfo(NoticeDTO pDTO) throws Exception {

        log.info("{}.insertNoticeInfo Start!", this.getClass().getName());

        String title = CmmUtil.nvl(pDTO.title());
        String noticeYn = CmmUtil.nvl(pDTO.noticeYn());
        String contents = CmmUtil.nvl(pDTO.contents());
        String userId = CmmUtil.nvl(pDTO.userId());

        log.info("pDTO : {}", pDTO); // Record 타입은 @ToString 기본 제공

        // 공지사항 저장을 위해서는 PK 값은 빌더에 추가하지 않는다.
        // JPA에 자동 증가 설정을 해놓았기 때문에
        NoticeEntity pEntity = NoticeEntity.builder()
                .title(title).noticeYn(noticeYn).contents(contents).userId(userId).readCnt(OL)
                .regId(userId).regDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .chgId(userId).chgDt(DateUtil.getDateTime("yyyy-MM-dd hh:mm:ss"))
                .build();

        // 공지사항 저장하기
        noticeRepository.save(pEntity);

        log.info("{}.insertNoticeInfo End!", this.getClass().getName());

    }
}
