package com.college.management.service;

import com.college.management.dto.BookIssueDTO;
import com.college.management.dto.LibraryBookDTO;
import com.college.management.entity.*;
import com.college.management.exception.ResourceNotFoundException;
import com.college.management.repository.BookIssueRepository;
import com.college.management.repository.LibraryBookRepository;
import com.college.management.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    private static final Logger log = LoggerFactory.getLogger(LibraryService.class);
    private static final int MAX_BOOKS_PER_STUDENT = 5;
    private static final int LOAN_PERIOD_DAYS = 14;
    private static final BigDecimal FINE_PER_DAY = new BigDecimal("5.00");

    private final LibraryBookRepository bookRepository;
    private final BookIssueRepository issueRepository;
    private final UserRepository userRepository;

    public LibraryService(LibraryBookRepository bookRepository, BookIssueRepository issueRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
    }

    // ---- Book CRUD ----
    @Transactional
    public LibraryBookDTO addBook(LibraryBookDTO dto) {
        LibraryBook book = new LibraryBook();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setIsbn(dto.getIsbn());
        book.setPublisher(dto.getPublisher());
        book.setCategory(dto.getCategory());
        book.setTotalCopies(dto.getTotalCopies());
        book.setAvailableCopies(dto.getTotalCopies());
        book.setShelfLocation(dto.getShelfLocation());
        book.setPublishedYear(dto.getPublishedYear());
        book.setDescription(dto.getDescription());
        return toBookDTO(bookRepository.save(book));
    }

    public List<LibraryBookDTO> getAllBooks() {
        return bookRepository.findAll().stream().map(this::toBookDTO).collect(Collectors.toList());
    }

    public LibraryBookDTO getBookById(Long id) {
        return toBookDTO(bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id)));
    }

    public List<LibraryBookDTO> searchBooks(String query) {
        return bookRepository.findByTitleContainingIgnoreCase(query).stream().map(this::toBookDTO).collect(Collectors.toList());
    }

    @Transactional
    public LibraryBookDTO updateBook(Long id, LibraryBookDTO dto) {
        LibraryBook book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        if (dto.getTitle() != null) book.setTitle(dto.getTitle());
        if (dto.getAuthor() != null) book.setAuthor(dto.getAuthor());
        if (dto.getCategory() != null) book.setCategory(dto.getCategory());
        if (dto.getTotalCopies() != null) {
            int diff = dto.getTotalCopies() - book.getTotalCopies();
            book.setTotalCopies(dto.getTotalCopies());
            book.setAvailableCopies(book.getAvailableCopies() + diff);
        }
        if (dto.getShelfLocation() != null) book.setShelfLocation(dto.getShelfLocation());
        return toBookDTO(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) throw new ResourceNotFoundException("Book not found with id: " + id);
        bookRepository.deleteById(id);
    }

    // ---- Issue / Return ----
    @Transactional
    public BookIssueDTO issueBook(BookIssueDTO dto) {
        LibraryBook book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        User student = userRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies available for this book");
        }
        long activeIssues = issueRepository.countByStudentIdAndStatus(dto.getStudentId(), BookIssueStatus.ISSUED);
        if (activeIssues >= MAX_BOOKS_PER_STUDENT) {
            throw new IllegalStateException("Student has reached maximum book limit (" + MAX_BOOKS_PER_STUDENT + ")");
        }

        BookIssue issue = new BookIssue();
        issue.setBook(book);
        issue.setStudent(student);
        issue.setIssueDate(LocalDate.now());
        issue.setDueDate(LocalDate.now().plusDays(LOAN_PERIOD_DAYS));
        issue.setStatus(BookIssueStatus.ISSUED);
        issue.setFineAmount(BigDecimal.ZERO);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        log.info("Book '{}' issued to student '{}'", book.getTitle(), student.getName());
        return toIssueDTO(issueRepository.save(issue));
    }

    @Transactional
    public BookIssueDTO returnBook(Long issueId) {
        BookIssue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Issue record not found"));

        if (issue.getStatus() == BookIssueStatus.RETURNED) {
            throw new IllegalStateException("Book already returned");
        }

        issue.setReturnDate(LocalDate.now());
        issue.setStatus(BookIssueStatus.RETURNED);

        if (LocalDate.now().isAfter(issue.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(issue.getDueDate(), LocalDate.now());
            issue.setFineAmount(FINE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays)));
        }

        LibraryBook book = issue.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        log.info("Book '{}' returned by student '{}'", book.getTitle(), issue.getStudent().getName());
        return toIssueDTO(issueRepository.save(issue));
    }

    public List<BookIssueDTO> getIssuesByStudent(Long studentId) {
        return issueRepository.findByStudentId(studentId).stream().map(this::toIssueDTO).collect(Collectors.toList());
    }

    public List<BookIssueDTO> getAllIssues() {
        return issueRepository.findAll().stream().map(this::toIssueDTO).collect(Collectors.toList());
    }

    private LibraryBookDTO toBookDTO(LibraryBook b) {
        LibraryBookDTO dto = new LibraryBookDTO();
        dto.setId(b.getId());
        dto.setTitle(b.getTitle());
        dto.setAuthor(b.getAuthor());
        dto.setIsbn(b.getIsbn());
        dto.setPublisher(b.getPublisher());
        dto.setCategory(b.getCategory());
        dto.setTotalCopies(b.getTotalCopies());
        dto.setAvailableCopies(b.getAvailableCopies());
        dto.setShelfLocation(b.getShelfLocation());
        dto.setPublishedYear(b.getPublishedYear());
        dto.setDescription(b.getDescription());
        return dto;
    }

    private BookIssueDTO toIssueDTO(BookIssue i) {
        BookIssueDTO dto = new BookIssueDTO();
        dto.setId(i.getId());
        dto.setBookId(i.getBook().getId());
        dto.setBookTitle(i.getBook().getTitle());
        dto.setStudentId(i.getStudent().getId());
        dto.setStudentName(i.getStudent().getName());
        dto.setIssueDate(i.getIssueDate());
        dto.setDueDate(i.getDueDate());
        dto.setReturnDate(i.getReturnDate());
        dto.setStatus(i.getStatus());
        dto.setFineAmount(i.getFineAmount());
        return dto;
    }
}
