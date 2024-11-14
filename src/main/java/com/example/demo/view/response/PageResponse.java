package com.example.demo.view.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class PageResponse<T>
{
    private int     pageSize;
    private int     pageNumber;
    private int     totalPages;
    public List<T> list;
    private long totalCount;
}

