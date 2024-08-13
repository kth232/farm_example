package com.joyfarm.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListData<T> { //리스트와 페이지 값을 담는 데이터
    private List<T> items; // 목록 데이터
    private Pagination pagination;
}
