package pl.herfor.server.data.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScreeningRequest {
    private Long filmId;
    private Long roomId;
    private LocalDateTime startDate;
}
