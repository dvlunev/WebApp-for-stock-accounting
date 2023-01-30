package me.lunev.coursework3.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SockOperation {

    private String typeOperation;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyy HH:mm:ss")
    private LocalDateTime localDateTime;
    private Sock sock;

    public SockOperation(String typeOperation, Sock sock) {
        this.typeOperation = typeOperation;
        this.sock = sock;
        this.localDateTime = LocalDateTime.now();
    }
}
