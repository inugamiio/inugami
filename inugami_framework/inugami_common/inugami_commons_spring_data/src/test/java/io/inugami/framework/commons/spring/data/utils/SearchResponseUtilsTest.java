package io.inugami.framework.commons.spring.data.utils;


import io.inugami.framework.interfaces.models.search.SearchRequest;
import io.inugami.framework.interfaces.models.search.SearchRequestDefaultDTO;
import io.inugami.framework.interfaces.models.search.SearchResponse;
import io.inugami.framework.interfaces.models.search.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.function.Function;

import static io.inugami.commons.test.UnitTestHelper.assertText;

class SearchResponseUtilsTest {

    @Test
    void should_build_default_page_request_when_search_request_is_null() {
        assertText(SearchResponseUtils.buildPageRequest(null, "id"),
                   """
                           {
                             "pageNumber" : 0,
                             "pageSize" : 20,
                             "sort" : {
                               "empty" : false,
                               "sorted" : true,
                               "unsorted" : false
                             },
                             "offset" : 0,
                             "paged" : true,
                             "unpaged" : false
                           }
                           """);
    }

    @Test
    void should_build_custom_page_request_with_desc_order() {
        SearchRequest request = SearchRequestDefaultDTO.builder()
                                                       .page(2)
                                                       .pageSize(50)
                                                       .sortOrder(SortOrder.DESC)
                                                       .sortFields("name")
                                                       .build();
        assertText(SearchResponseUtils.buildPageRequest(request, "id"),
                   """
                           {
                             "pageNumber" : 2,
                             "pageSize" : 50,
                             "sort" : {
                               "empty" : false,
                               "sorted" : true,
                               "unsorted" : false
                             },
                             "offset" : 100,
                             "paged" : true,
                             "unpaged" : false
                           }
                           """);

    }

    @Test
    void should_return_valid_empty_page() {
        PageRequest  pr     = PageRequest.of(0, 10);
        assertText(SearchResponseUtils.buildEmptyPage(pr),
                   """
                           {
                              "content" : [ ],
                              "empty" : true,
                              "first" : true,
                              "last" : true,
                              "number" : 0,
                              "numberOfElements" : 0,
                              "pageable" : {
                                "pageNumber" : 0,
                                "pageSize" : 10,
                                "sort" : {
                                  "empty" : true,
                                  "sorted" : false,
                                  "unsorted" : true
                                },
                                "offset" : 0,
                                "paged" : true,
                                "unpaged" : false
                              },
                              "size" : 10,
                              "sort" : {
                                "empty" : true,
                                "sorted" : false,
                                "unsorted" : true
                              },
                              "totalElements" : 0,
                              "totalPages" : 0
                            }
                           """);
    }

    @Test
    void should_map_data_and_metadata_correctly() {
        List<String> entities = List.of("10", "20");
        PageRequest  pr       = PageRequest.of(0, 2);
        Page<String> page     = new PageImpl<>(entities, pr, 10); // 10 au total donc a une page suivante

        Function<String, Integer> mapper = Integer::valueOf;

        SearchResponse<Integer> response = SearchResponseUtils.buildSearchResponse(page, mapper);

        assertText(response,
                   """
                           {
                             "data" : [ 10, 20 ],
                             "nbFoundItems" : 10,
                             "next" : true,
                             "page" : 0,
                             "pageSize" : 2,
                             "previous" : false,
                             "totalPages" : 5
                           }
                           """);

    }

    @Test
    void should_ignore_null_mapped_items() {
        Page<String>             page   = new PageImpl<>(List.of("valid", "skip"), PageRequest.of(0, 10), 2);
        Function<String, String> mapper = s -> "skip".equals(s) ? null : s;

        SearchResponse<String> response = SearchResponseUtils.buildSearchResponse(page, mapper);

        assertText(response,
                   """
                           {
                             "data" : [ "valid" ],
                             "nbFoundItems" : 2,
                             "next" : false,
                             "page" : 0,
                             "pageSize" : 10,
                             "previous" : false,
                             "totalPages" : 1
                           }
                           """);
    }
}