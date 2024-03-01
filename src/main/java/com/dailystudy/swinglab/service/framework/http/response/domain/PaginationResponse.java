package com.dailystudy.swinglab.service.framework.http.response.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

@Data
@NoArgsConstructor
public class PaginationResponse
{
    private Integer page;
    private Integer size;
    private Integer offset;

    private Integer numberOfElements;
    private Integer totalElements;
    private Integer totalPages;
    private boolean first;
    private boolean last;

    public PaginationResponse(Pageable page, Integer rowCount, Integer totalElements)
    {
        this.offset = Long.valueOf(page.getOffset()).intValue();
        this.page = page.getPageNumber() + 1;
        this.size = page.getPageSize();
        this.numberOfElements = rowCount;
        this.totalElements = totalElements;
        this.totalPages = (totalElements % size) == 0 ? totalElements / size : totalElements / size + 1;
        this.first = this.offset == 0 ? true : false;
        this.last = (offset + this.size >= this.totalElements) ? true : false;
    }
    
    public PaginationResponse(Integer page, Integer size, Integer numberOfElements, Integer totalElements)
    {
        this.offset = (page < 1 ? 0 : page - 1) * size;
        this.page = page;
        this.size = size;
        this.numberOfElements = numberOfElements;
        this.totalElements = totalElements;
        this.totalPages = (totalElements % size) == 0 ? totalElements / size : totalElements / size + 1;
        this.first = this.offset == 0 ? true : false;
        this.last = (offset + this.size >= this.totalElements) ? true : false;
    }
}
