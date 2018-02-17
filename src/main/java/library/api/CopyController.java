package library.api;

import library.domain.Copy;
import library.service.CopyService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collection;

import static library.domain.Copy.copyBuilder;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
public class CopyController {
    
    private final CopyService copyService;

    public CopyController(final CopyService copyService) {
        this.copyService = copyService;
    }

    @GetMapping(value = "/book/{bookId}/copy", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Copy>> getAllCopiesOfBook(@PathVariable final String bookId) {
        return new ResponseEntity<>(copyService.getAllCopiesOfBook(bookId), OK);
    }

    @PostMapping(value = "/book/{bookId}/copy", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Copy> createCopy(@PathVariable final String bookId) {
        return copyService
                .createCopy(copyBuilder().withBookId(bookId).build())
                .map(copy -> new ResponseEntity<>(copy, OK))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping(value = "/copy/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Copy> getCopy(@PathVariable final String id) {
        return copyService
                .getCopy(id)
                .map(copy -> new ResponseEntity<>(copy, OK))
                .orElseGet(() -> new ResponseEntity<>(NOT_FOUND));
    }

}
