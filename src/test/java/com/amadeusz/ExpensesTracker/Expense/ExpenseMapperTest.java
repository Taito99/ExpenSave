package com.amadeusz.ExpensesTracker.Expense;

import com.amadeusz.ExpensesTracker.category.Category;
import com.amadeusz.ExpensesTracker.exeptions.InvalidExpenseException;
import com.amadeusz.ExpensesTracker.exeptions.MapperNullException;
import com.amadeusz.ExpensesTracker.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class ExpenseMapperTest {

    private ExpenseMapper expenseMapper;
    private Category category;
    private User user;
    UUID ownerId;

    @BeforeEach
    void setUp() {
        expenseMapper = new ExpenseMapper();
        category = Category.builder()
                .name("Test Category")
                .build();

        ownerId = UUID.randomUUID();

        user = User.builder()
                .id(ownerId)
                .build();
    }

    @Test
    public void apply() {
        //given
        Expense expense = Expense.builder()
                .name("Test Expense")
                .category(category)
                .price(BigDecimal.TEN)
                .quantity(1)
                .owner(user)
                .date(LocalDate.of(2024, 12, 22))
                .build();

        ExpenseDto expenseDto = expenseMapper.apply(expense);

        //then
        assertThat(expenseDto).isNotNull();
        assertThat(expenseDto.name()).isEqualTo("Test Expense");
        assertThat(expenseDto.categoryName()).isEqualTo("Test Category");
        assertThat(expenseDto.price()).isEqualTo(BigDecimal.TEN);
        assertThat(expenseDto.quantity()).isEqualTo(1);
        assertThat(expenseDto.ownerId()).isEqualTo(ownerId);
        assertThat(expenseDto.date()).isEqualTo(LocalDate.of(2024, 12, 22));
    }

    @Test
    public void applyIsNull() {
        // given
        Expense expense = null;

        // when
        Throwable thrown = catchThrowable(() -> expenseMapper.apply(expense));

        // then
        assertThat(thrown)
                .isInstanceOf(MapperNullException.class)
                .hasMessage("Expense in ExpenseMapper cannot be null");
    }

    @Test
    public void applyWithBadOwnerId() {
        //given
        Expense expense = Expense.builder()
                .name("Test Expense")
                .category(category)
                .price(BigDecimal.TEN)
                .quantity(1)
                .owner(user)
                .date(LocalDate.of(2024, 12, 22))
                .build();

        ExpenseDto expenseDto = expenseMapper.apply(expense);

        //then
        assertThat(expenseDto.ownerId()).isNotEqualTo(UUID.randomUUID());
    }

    @Test
    public void applyWithNegativePrice() {
            //given
        Expense expense = Expense.builder()
                .name("Test Expense")
                .category(category)
                .price(new BigDecimal("-11"))
                .quantity(1)
                .owner(user)
                .date(LocalDate.of(2024, 12, 22))
                .build();

        Throwable thrown = catchThrowable(() -> expenseMapper.apply(expense));

        //then
        assertThat(thrown)
                .isInstanceOf(InvalidExpenseException.class)
                .hasMessage("Price must be greater than zero");
    }

    @Test
    public void applyWithNegativeQuantity() {
        //given
        Expense expense = Expense.builder()
                .name("Test Expense")
                .category(category)
                .price(BigDecimal.TEN)
                .quantity(-1)
                .owner(user)
                .date(LocalDate.of(2024, 12, 22))
                .build();

        Throwable thrown = catchThrowable(() -> expenseMapper.apply(expense));

        //then
        assertThat(thrown)
                .isInstanceOf(InvalidExpenseException.class)
                .hasMessage("Quantity must be greater than zero");
    }

    @Test
    public void applyWithoutCategory() {
        //given
        Expense expense = Expense.builder()
                .name("Test Expense")
                .category(null)
                .price(BigDecimal.TEN)
                .quantity(2)
                .owner(user)
                .date(LocalDate.of(2024, 12, 22))
                .build();

        Throwable thrown = catchThrowable(() -> expenseMapper.apply(expense));

        //then
        assertThat(thrown)
                .isInstanceOf(InvalidExpenseException.class)
                .hasMessage("Category cannot be null");
     }





}
