package ru.practicum.shareit.request.api;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class ItemRequestResponseDto {

    private Long id;
    private String description;
    private Long requestorId;
    private Long created;

    public static ItemRequestResponseDto from(ItemRequest request) {
        ItemRequestResponseDto dto = new ItemRequestResponseDto();
        dto.setId(request.getId());
        dto.setDescription(request.getDescription());
        dto.setRequestorId(request.getRequestorId());
        if (request.getCreated() != null) dto.setCreated(request.getCreated().toInstant().toEpochMilli());
        return dto;
    }

}
