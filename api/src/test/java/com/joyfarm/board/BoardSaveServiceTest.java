package com.joyfarm.board;

import com.joyfarm.board.controllers.RequestBoard;
import com.joyfarm.board.entities.Board;
import com.joyfarm.board.entities.BoardData;
import com.joyfarm.board.repositories.BoardRepository;
import com.joyfarm.board.services.BoardSaveService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class BoardSaveServiceTest {

    @Autowired
    private BoardSaveService saveService;

    @Autowired
    private BoardRepository boardRepository;

    private Board board;

    @BeforeEach
    void init() {
        board = new Board();
        board.setBid("freetalk");
        board.setBName("자유게시판");
        /*
         board = Board.builder()
                .bid("freetalk")
                .bName("자유게시판")
                 .gid(UUID.randomUUID().toString())
                 .listAccessType(Authority.ALL)
                 .writeAccessType(Authority.ALL)
                 .commentAccessType(Authority.ALL)
                 .viewAccessType(Authority.ALL)
                 .replyAccessType(Authority.ALL)
                 .locationAfterWriting("list")
                .build();
            */
        boardRepository.saveAndFlush(board);
    }

    @Test
    @DisplayName("게시글 저장 test")
    void saveTest() {
        RequestBoard form = new RequestBoard();
        form.setBid(board.getBid());
        form.setCategory("분류1");
        form.setPoster("작성자");
        form.setSubject("제목");
        form.setContent("내용");
        form.setGuestPw("1234ab");

        BoardData data = saveService.save(form);
        System.out.println(data);

        form.setMode("update");
        form.setSeq(data.getSeq());

        BoardData data2 = saveService.save(form);
        System.out.println(data2);
    }
}