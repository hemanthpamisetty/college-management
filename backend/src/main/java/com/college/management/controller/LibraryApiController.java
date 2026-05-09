package com.college.management.controller;

import com.college.management.dto.ApiResponse;
import com.college.management.dto.BookIssueDTO;
import com.college.management.dto.LibraryBookDTO;
import com.college.management.service.LibraryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/library")
public class LibraryApiController {

    private final LibraryService libraryService;

    public LibraryApiController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    // ---- Books ----
    @PostMapping("/books")
    public ResponseEntity<ApiResponse<LibraryBookDTO>> addBook(@Valid @RequestBody LibraryBookDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Book added", libraryService.addBook(dto)));
    }

    @GetMapping("/books")
    public ResponseEntity<ApiResponse<List<LibraryBookDTO>>> getAllBooks() {
        return ResponseEntity.ok(ApiResponse.success(libraryService.getAllBooks()));
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<ApiResponse<LibraryBookDTO>> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(libraryService.getBookById(id)));
    }

    @GetMapping("/books/search")
    public ResponseEntity<ApiResponse<List<LibraryBookDTO>>> searchBooks(@RequestParam String query) {
        return ResponseEntity.ok(ApiResponse.success(libraryService.searchBooks(query)));
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<ApiResponse<LibraryBookDTO>> updateBook(@PathVariable Long id, @RequestBody LibraryBookDTO dto) {
        return ResponseEntity.ok(ApiResponse.success("Book updated", libraryService.updateBook(id, dto)));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable Long id) {
        libraryService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success("Book deleted", null));
    }

    // ---- Issues ----
    @PostMapping("/issues")
    public ResponseEntity<ApiResponse<BookIssueDTO>> issueBook(@Valid @RequestBody BookIssueDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Book issued", libraryService.issueBook(dto)));
    }

    @PutMapping("/issues/{issueId}/return")
    public ResponseEntity<ApiResponse<BookIssueDTO>> returnBook(@PathVariable Long issueId) {
        return ResponseEntity.ok(ApiResponse.success("Book returned", libraryService.returnBook(issueId)));
    }

    @GetMapping("/issues")
    public ResponseEntity<ApiResponse<List<BookIssueDTO>>> getAllIssues() {
        return ResponseEntity.ok(ApiResponse.success(libraryService.getAllIssues()));
    }

    @GetMapping("/issues/student/{studentId}")
    public ResponseEntity<ApiResponse<List<BookIssueDTO>>> getIssuesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.success(libraryService.getIssuesByStudent(studentId)));
    }
}
