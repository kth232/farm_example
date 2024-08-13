package com.joyfarm.board.services;

import com.joyfarm.board.controllers.RequestBoard;
import com.joyfarm.board.entities.Board;
import com.joyfarm.board.entities.BoardData;
import com.joyfarm.board.exceptions.BoardDataNotFoundException;
import com.joyfarm.board.exceptions.BoardNotFoundException;
import com.joyfarm.board.repositories.BoardDataRepository;
import com.joyfarm.board.repositories.BoardRepository;
import com.joyfarm.file.services.FileUploadDoneService;
import com.joyfarm.member.MemberUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional //게시판 엔티티 설정 유지되도록 하기 위함(지연로딩 시 null 에러 방지)
public class BoardSaveService { //추가, 수정

    private final HttpServletRequest request;
    private final PasswordEncoder encoder;
    private final BoardDataRepository boardDataRepository;
    private final BoardRepository boardRepository; //게시판 아이디 필요
    private final MemberUtil memberUtil;
    private final FileUploadDoneService doneService;

    public BoardData save(RequestBoard form) {

        String mode = form.getMode();
        mode = StringUtils.hasText(mode) ? mode.trim() : "write";

        String gid = form.getGid();

        BoardData data = null;
        Long seq = form.getSeq();
        if (seq != null && mode.equals("update")) { // 글 수정
            data = boardDataRepository.findById(seq).orElseThrow(BoardDataNotFoundException::new);
            //게시글이 없는 경우

        } else { // 글 작성
            String bid = form.getBid();
            Board board = boardRepository.findById(bid).orElseThrow(BoardNotFoundException::new);
            //게시판이 없는 경우

            data = BoardData.builder()
                    .gid(gid)
                    .board(board)
                    .member(memberUtil.getMember())
                    .ip(request.getRemoteAddr())
                    .ua(request.getHeader("User-Agent")) //요청헤더에서 가져옴
                    .build();
        }

        //글 작성, 글 수정 공통 s
        data.setPoster(form.getPoster());
        data.setSubject(form.getSubject());
        data.setContent(form.getContent());
        data.setCategory(form.getCategory());
        data.setEditorView(data.getBoard().isUseEditor());

        data.setNum1(form.getNum1());
        data.setNum2(form.getNum2());
        data.setNum3(form.getNum3());

        data.setText1(form.getText1());
        data.setText2(form.getText2());
        data.setText3(form.getText3());

        data.setLongtext1(form.getLongText1());
        data.setLongtext2(form.getLongText2());

        //비회원 비밀번호 처리
        String guestPw = form.getGuestPw();
        if (StringUtils.hasText(guestPw)) {
            data.setGuestPw(encoder.encode(guestPw));
        }

        if (memberUtil.isAdmin()) {
            data.setNotice(form.isNotice());
        }
        //글 작성, 글 수정 공통 e

        // 파일 업로드 완료 처리
        doneService.process(gid);

        return data;
    }
}
