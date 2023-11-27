package fa.hust.utils;

import fa.hust.req.SearchReq;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SearchUtil {

    final private ModelMapper modelMapper;

    private List<Integer> getPageMax(Integer size) {
        List<Integer> pageMax = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            pageMax.add(i);
        }
        return pageMax;
    }

    public SearchReq convertSearchReq(SearchReq req){
        SearchReq searchReq = SearchReq.builder()
                .pageNumber(1)
                .pageSize(10)
                .param("")
                .pageMax(new ArrayList<>())
                .build();
        modelMapper.map(req,searchReq);
        return searchReq;
    }

    public SearchReq setSearch(SearchReq req, int totalPages){
        if (totalPages > 0) {
            req.setPageMax(getPageMax(totalPages));
        } else {
            final String NO_RESULT = "Không có dữ liệu được lưu trữ";
            final String NO_RESULT_FOUND = "Không dữ liều khớp với từ khóa tìm kiếm: " + req.getParam();
            String message = "".equals(req.getParam()) ? NO_RESULT : NO_RESULT_FOUND;
            req.setMassage(message);
        }
        return req;
    }
}
