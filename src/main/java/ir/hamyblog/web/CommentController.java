package ir.hamyblog.web;

import ir.hamyblog.entities.Comment;
import ir.hamyblog.model.CommentIn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(
        path = "/comments"
)
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(@RequestBody CommentIn commentIn, Authentication authentication) {
        Comment comment = commentService.addComment(commentIn.getUid(), authentication.getName(), commentIn.getContent());
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<List<Comment>> getArticleComments(@PathVariable("articleId") UUID articleUid) {
        List<Comment> comments = commentService.getComments(articleUid);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}
