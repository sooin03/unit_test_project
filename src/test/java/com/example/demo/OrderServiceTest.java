package com.example.demo;

import com.example.demo.exception.ExceptionCode;
import com.example.demo.exception.ZeroBaseException;
import com.example.demo.infra.entity.Book;
import com.example.demo.infra.entity.BookOrder;
import com.example.demo.infra.entity.BookStock;
import com.example.demo.infra.entity.User;
import com.example.demo.infra.repository.BookOrderRepository;
import com.example.demo.infra.repository.BookRepository;
import com.example.demo.infra.repository.BookStockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    BookStockRepository bookStockRepository;
    @Mock
    BookOrderRepository bookOrderRepository;

    @InjectMocks
    OrderService orderService;

    @BeforeEach
    void setUp() {
        Book book = Book.builder().minAge(0).sale(true).build();
        BookStock bookStock = BookStock.builder().stock(20).build();

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookStockRepository.findByBookId(anyLong())).thenReturn(Optional.of(bookStock));

        doNothing().when(bookStockRepository).save(any(BookStock.class));
        when(bookOrderRepository.save(any(BookOrder.class))).thenReturn(new BookOrder(1L, null, null));
    }

    @Test
    @DisplayName("재고가 정상으로 차감되며 주문이 성공한다.")
    void success__order() {
        BookStock bookStock = BookStock.builder().stock(10).build();
        when(bookStockRepository.findByBookId(anyLong())).thenReturn(Optional.of(bookStock));

        orderService.order(User.asNew(1L), 1L);

        assertThat(bookStock.getStock()).isEqualTo(9);
        verify(bookOrderRepository).save(any(BookOrder.class));
    }

    @Test
    @DisplayName("Book 존재하지 않으면 익셉션 발생(NOT_FOUND_BOOK)")
    void throwException__when__not_found_book() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.order(User.asNew(1L), 1L))
                .isInstanceOf(ZeroBaseException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_FOUND_BOOK);
    }

    @Test
    @DisplayName("BookStock 존재하지 않으면 익셉션 발생(NOT_FOUND_BOOK_STOCK)")
    void throwException__when__not_found_bookstock() {
        when(bookStockRepository.findByBookId(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.order(User.asNew(1L), 1L))
                .isInstanceOf(ZeroBaseException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_FOUND_BOOK_STOCK);
    }

    @Test
    @DisplayName("판매중이지 않은 책을 주문하면 익셉션 발생(BOOK_IS_NOT_SALE)")
    void throwException__when__book_sale_off() {
        Book book = Book.builder().minAge(0).sale(false).build();
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> orderService.order(User.asNew(1L), 1L))
                .isInstanceOf(ZeroBaseException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.BOOK_IS_NOT_SALE);
    }

    @Test
    @DisplayName("재고가 부족할때 익셉션 발생(FAIL_BOOK_ORDER)")
    void throwException__when__not_enough_stock() {
        BookStock bookStock = BookStock.builder().stock(0).build();
        when(bookStockRepository.findByBookId(anyLong())).thenReturn(Optional.of(bookStock));

        assertThatThrownBy(() -> orderService.order(User.asNew(1L), 1L))
                .isInstanceOf(ZeroBaseException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.FAIL_BOOK_ORDER);
    }
}
