package ir.hamyblog.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class CommentIn {
    private UUID uid;
    private String content;
}
