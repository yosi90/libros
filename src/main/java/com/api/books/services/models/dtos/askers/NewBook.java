package com.api.books.services.models.dtos.askers;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NewBook {

    private String name;
    private Long userId;
    private int orderInSaga;
    private String status;
    private List<Long> authorIds = new ArrayList<>();
    private Long universeId;
    private Long sagaId;

    public NewBook(String name, Long userId, int orderInSaga, String status, String[] authorIds, Long universeId, Long sagaId) {
        this.name = name;
        this.userId = userId;
        this.orderInSaga = orderInSaga;
        this.status = status;
        for (String id : authorIds)
            this.authorIds.add(Long.parseLong(id));
        this.universeId = universeId;
        this.sagaId = sagaId;
    }
}
