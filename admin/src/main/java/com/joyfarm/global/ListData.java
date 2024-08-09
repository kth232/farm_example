package com.joyfarm.global;

import lombok.Data;

import java.util.List;

@Data
public class ListData<T> { //리스트와 페이지 값을 담는 데이터
    private List<T> items;
    private Pagination pagination;
}
