package project.team.ondo.global.response;

import java.util.List;

public record CursorResponse<T>(
    List<T> items,
    Long nextCursor,
    boolean hasNext
) {
    public static <T> CursorResponse<T> of(List<T> items, Long nextCursor, boolean hasNext) {
        return new CursorResponse<>(items, nextCursor, hasNext);
    }
}
