package io.inugami.framework.interfaces.models.search;

import io.inugami.framework.interfaces.testing.commons.UnitTestHelper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static io.inugami.framework.interfaces.testing.commons.UnitTestHelper.assertText;

class SearchResponseMapperUtilsTest {
    @Test
    void assertUtilityClass() {
        UnitTestHelper.assertUtilityClassLombok(SearchResponseMapperUtils.class);
    }

    @Test
    void should_return_empty_response_when_null() {
        Function<Integer, String> mapper = (v) -> String.valueOf(v);
        SearchResponse<String>    result = SearchResponseMapperUtils.convert(null, mapper);

        assertText(result, """
                {
                  "data" : [ ],
                  "nbFoundItems" : 0,
                  "next" : false,
                  "page" : 0,
                  "pageSize" : 0,
                  "previous" : false,
                  "totalPages" : 0
                }
                """);
    }

    @Test
    void should_map_data_and_keep_metadata() {
        SearchResponse<Integer> source = SearchResponse.<Integer>builder()
                                                       .data(List.of(1, 2, 3))
                                                       .page(1)
                                                       .pageSize(10)
                                                       .totalPages(5)
                                                       .nbFoundItems(50)
                                                       .previous(true)
                                                       .next(true)
                                                       .sortFields("id")
                                                       .sortOrder(SortOrder.DESC)
                                                       .filters(Map.of("active", "true"))
                                                       .build();

        Function<Integer, String> mapper = i -> "Item-" + i;

                SearchResponse<String> result = SearchResponseMapperUtils.convert(source, mapper);


        assertText(result, """
                {
                   "data" : [ "Item-1", "Item-2", "Item-3" ],
                   "filters" : {
                     "active" : "true"
                   },
                   "nbFoundItems" : 50,
                   "next" : true,
                   "page" : 1,
                   "pageSize" : 10,
                   "previous" : true,
                   "sortFields" : "id",
                   "sortOrder" : "DESC",
                   "totalPages" : 5
                 }
                """);
    }

    @Test
    void should_handle_null_data_list() {

        SearchResponse<Integer> source = SearchResponse.<Integer>builder()
                                                       .data(null)
                                                       .page(0)
                                                       .build();

        SearchResponse<String> result = SearchResponseMapperUtils.convert(source, Object::toString);

        assertText(result.getData(), "[ ]");
        assertText(result.getPage(), "0");
    }
}