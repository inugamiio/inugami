package ${package}.api.domain.user.dto;
import io.inugami.framework.interfaces.models.search.MatchType;
import io.inugami.framework.interfaces.models.search.QueryFilterDTO;
import lombok.experimental.UtilityClass;

import java.util.List;

import static io.inugami.framework.interfaces.models.search.SearchFiltersUtils.buildFilters;

@SuppressWarnings({"java:S2386"})
@UtilityClass
public class UserFilters {

    public static final String UID        = "uid";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME  = "lastName";
    public static final String EMAIL      = "email";


    public static final List<QueryFilterDTO<?>> FILTERS = buildFilters(
            QueryFilterDTO.<String>builder()
                          .field(UID)
                          .matchType(MatchType.IN)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(FIRST_NAME)
                          .matchType(MatchType.CONTAINS)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(LAST_NAME)
                          .matchType(MatchType.CONTAINS)
                          .build(),
            QueryFilterDTO.<String>builder()
                          .field(EMAIL)
                          .matchType(MatchType.IN)
                          .build()
                                                                      );
}
