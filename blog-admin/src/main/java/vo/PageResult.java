package vo;

import lombok.Data;

import java.util.List;

/**
 * @author sq ♥ovo♥
 * @date 2024/3/5 - 16:11
 */
@Data
public class PageResult<T> {
    private List<T> list;

    private Long total;
}
