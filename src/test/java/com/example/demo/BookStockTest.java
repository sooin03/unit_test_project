package com.example.demo;

import com.example.demo.exception.ExceptionCode;
import com.example.demo.exception.ZeroBaseException;
import com.example.demo.infra.entity.BookStock;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class BookStockTest {

    @Test
    @DisplayName("BookStock 재고가 정상적으로 차감된다")
    void success__decreaseStock() {

        final int stockCount = 10;

        BookStock bookStock = BookStock
                .builder()
                .stock(stockCount)
                .build();

        assertThat(bookStock.enoughStock()).isTrue(); //필요

        bookStock.decreaseStock();

        assertThat(bookStock.getStock()).isLessThan(stockCount);
    }

    @Test
    @DisplayName("BookStock 재고가 0일때, 차감을 시도하면 익셉션 발생(NOT_ENOUGH_STOCK)")
    void throwException__when__not_enough_stock() {
        BookStock bookStock = BookStock
                .builder()
                .stock(0)
                .build();

        assertThatThrownBy(bookStock::decreaseStock)
                .isInstanceOf(ZeroBaseException.class)
                .hasFieldOrPropertyWithValue("code", ExceptionCode.NOT_ENOUGH_STOCK);
    }
}
