package com.amadeusz.ExpensesTracker.Expense;

import com.amadeusz.ExpensesTracker.category.Category;
import com.amadeusz.ExpensesTracker.category.CategoryRepository;
import com.amadeusz.ExpensesTracker.exeptions.InvalidExpenseException;
import com.amadeusz.ExpensesTracker.user.User;
import com.amadeusz.ExpensesTracker.user.UserContextService;
import com.amadeusz.ExpensesTracker.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.Mockito.*;

class ExpenseServiceTest {

    Category category;

    User user;

    Expense initialExpense;

    @InjectMocks
    private ExpenseService expenseService;


    @Mock
    private  ExpenseDao expenseDao;

    @Mock
    private  CategoryRepository categoryRepository;

    @Mock
    private ExpenseDto expenseDto;


    @Mock
    private  UserRepository userRepository;

    @Mock
    private  UserContextService userContextService;

    @Mock
    private  ExpenseMapper expenseMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = Category
                .builder()
                .id(UUID.randomUUID())
                .name("Test Category")
                .icon("🖊️")
                .build();

        user = User
                .builder()
                .id(UUID.randomUUID())
                .firstName("Test User name")
                .lastName("Test User Last Name")
                .categoryLimits(new HashMap<>(Map.of("Test Category", BigDecimal.valueOf(1000))))
                .expenses(new ArrayList<>())
                .email("test@test.com")
                .username("testUsername")
                .monthlyBudget(new BigDecimal("5000"))
                .availableBudget(new BigDecimal("5000"))
                .build();

        initialExpense = Expense
                .builder()
                .id(UUID.randomUUID())
                .name("Initial Test Expense")
                .category(category)
                .price(BigDecimal.valueOf(100))
                .quantity(1)
                .owner(user)
                .date(LocalDate.now())
                .build();
    }

    @Test
    public void should_successfully_create_expense() {
        // given
        ExpenseDto expenseDto = new ExpenseDto(
                "Test Expense",
                category.getName(),
                BigDecimal.valueOf(50),
                1,
                user.getId(),
                LocalDate.now()
        );

        Expense expense = Expense
                .builder()
                .id(UUID.randomUUID())
                .name(expenseDto.name())
                .category(category)
                .price(expenseDto.price())
                .quantity(expenseDto.quantity())
                .date(expenseDto.date())
                .owner(user)
                .build();

        // when
        when(userContextService.getAuthenticatedUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(categoryRepository.findCategoryByName(category.getName())).thenReturn(Optional.of(category));
        when(expenseMapper.apply(any(Expense.class))).thenReturn(expenseDto);

        expenseService.createExpense(expenseDto);

        // then
        assertThat(user.getExpenses()).hasSize(1);
        assertThat(user.getAvailableBudget()).isEqualTo(BigDecimal.valueOf(4950));
        assertThat(user.getCategoryLimits().get(category.getName())).isEqualTo(BigDecimal.valueOf(950));

        verify(userContextService, times(1)).getAuthenticatedUsername();
        verify(userRepository, times(1)).findUserByUsername(user.getUsername());
        verify(categoryRepository, times(1)).findCategoryByName(category.getName());
        verify(expenseDao, times(1)).insertExpense(any(Expense.class));
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void should_not_create_expense_with_negative_price() {
        // given
        ExpenseDto expenseDto = new ExpenseDto(
                "Test Expense",
                category.getName(),
                BigDecimal.valueOf(-2),
                1,
                user.getId(),
                LocalDate.now()
        );

        Expense expense = Expense.builder()
                .name(expenseDto.name())
                .category(category)
                .price(expenseDto.price())
                .quantity(expenseDto.quantity())
                .owner(user)
                .date(expenseDto.date())
                .build();

        ExpenseMapper expenseMapper = new ExpenseMapper();

        when(userContextService.getAuthenticatedUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(categoryRepository.findCategoryByName(expenseDto.categoryName())).thenReturn(Optional.of(category));

        // when
        Throwable thrown = catchThrowable(() -> {
            ExpenseDto result = expenseMapper.apply(expense);
            expenseService.createExpense(result);
        });

        // then
        assertThat(thrown)
                .isInstanceOf(InvalidExpenseException.class)
                .hasMessage("Price must be greater than zero");

        verify(expenseDao, never()).insertExpense(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void should_not_create_expense_with_empty_name() {
        // given
        ExpenseDto expenseDto = new ExpenseDto(
                "", //empty
                category.getName(),
                BigDecimal.valueOf(10),
                1,
                user.getId(),
                LocalDate.now()
        );

        Expense expense = Expense.builder()
                .name(expenseDto.name())
                .category(category)
                .price(expenseDto.price())
                .quantity(expenseDto.quantity())
                .owner(user)
                .date(expenseDto.date())
                .build();

        ExpenseMapper expenseMapper = new ExpenseMapper();

        when(userContextService.getAuthenticatedUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(categoryRepository.findCategoryByName(expenseDto.categoryName())).thenReturn(Optional.of(category));

        // when
        Throwable thrown = catchThrowable(() -> {
            ExpenseDto result = expenseMapper.apply(expense);
            expenseService.createExpense(result);
        });

        // then
        assertThat(thrown)
                .isInstanceOf(InvalidExpenseException.class)
                .hasMessage("Expense name cannot be blank");

        verify(expenseDao, never()).insertExpense(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void should_not_create_expense_with_blank_name() {
        // given
        ExpenseDto expenseDto = new ExpenseDto(
                " ", //blank
                category.getName(),
                BigDecimal.valueOf(10),
                1,
                user.getId(),
                LocalDate.now()
        );

        Expense expense = Expense.builder()
                .name(expenseDto.name())
                .category(category)
                .price(expenseDto.price())
                .quantity(expenseDto.quantity())
                .owner(user)
                .date(expenseDto.date())
                .build();

        ExpenseMapper expenseMapper = new ExpenseMapper();

        when(userContextService.getAuthenticatedUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(categoryRepository.findCategoryByName(expenseDto.categoryName())).thenReturn(Optional.of(category));

        // when
        Throwable thrown = catchThrowable(() -> {
            ExpenseDto result = expenseMapper.apply(expense);
            expenseService.createExpense(result);
        });

        // then
        assertThat(thrown)
                .isInstanceOf(InvalidExpenseException.class)
                .hasMessage("Expense name cannot be blank");

        verify(expenseDao, never()).insertExpense(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void should_not_create_expense_with_bad_quantity() {
        // given
        ExpenseDto expenseDto = new ExpenseDto(
                "Test Expense",
                category.getName(),
                BigDecimal.valueOf(10),
                -2, //canot create expense for quantity less or equals to 0
                user.getId(),
                LocalDate.now()
        );

        Expense expense = Expense.builder()
                .name(expenseDto.name())
                .category(category)
                .price(expenseDto.price())
                .quantity(expenseDto.quantity())
                .owner(user)
                .date(expenseDto.date())
                .build();

        ExpenseMapper expenseMapper = new ExpenseMapper();

        when(userContextService.getAuthenticatedUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(categoryRepository.findCategoryByName(expenseDto.categoryName())).thenReturn(Optional.of(category));

        // when
        Throwable thrown = catchThrowable(() -> {
            ExpenseDto result = expenseMapper.apply(expense);
            expenseService.createExpense(result);
        });

        // then
        assertThat(thrown)
                .isInstanceOf(InvalidExpenseException.class)
                .hasMessage("Quantity must be greater than zero");

        verify(expenseDao, never()).insertExpense(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void should_not_create_expense_without_user_id() {
        // given
        ExpenseDto expenseDto = new ExpenseDto(
                "Test Expense",
                category.getName(),
                BigDecimal.valueOf(10),
                1,
                user.getId(),
                LocalDate.now()
        );

        Expense expense = Expense.builder()
                .name(expenseDto.name())
                .category(category)
                .price(expenseDto.price())
                .quantity(expenseDto.quantity())
                .owner(null)
                .date(expenseDto.date())
                .build();

        ExpenseMapper expenseMapper = new ExpenseMapper();

        when(userContextService.getAuthenticatedUsername()).thenReturn("testUsername");
        when(userRepository.findUserByUsername("testUsername")).thenReturn(Optional.of(user));
        when(categoryRepository.findCategoryByName(expenseDto.categoryName())).thenReturn(Optional.of(category));

        // when
        Throwable thrown = catchThrowable(() -> {
            ExpenseDto result = expenseMapper.apply(expense);
            expenseService.createExpense(result);
        });

        // then
        assertThat(thrown)
                .isInstanceOf(InvalidExpenseException.class)
                .hasMessage("Owner cannot be null");

        verify(expenseDao, never()).insertExpense(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void should_find_expense_by_id() {
        // given
        UUID expenseId = initialExpense.getId();

        ExpenseDto expectedExpenseDto = new ExpenseDto(
                initialExpense.getName(),
                initialExpense.getCategory().getName(),
                initialExpense.getPrice(),
                initialExpense.getQuantity(),
                initialExpense.getOwner().getId(),
                initialExpense.getDate()
        );

        when(userContextService.getAuthenticatedUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(expenseDao.findExpenseById(expenseId)).thenReturn(Optional.of(initialExpense));
        when(expenseMapper.apply(initialExpense)).thenReturn(expectedExpenseDto);

        // when
        ExpenseDto result = expenseService.getExpenseDetails(expenseId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(expectedExpenseDto.name());
        assertThat(result.price()).isEqualTo(expectedExpenseDto.price());
        assertThat(result.quantity()).isEqualTo(expectedExpenseDto.quantity());
        assertThat(result.ownerId()).isEqualTo(expectedExpenseDto.ownerId());
        assertThat(result.categoryName()).isEqualTo(expectedExpenseDto.categoryName());
        assertThat(result.date()).isEqualTo(expectedExpenseDto.date());

        verify(userContextService, times(1)).getAuthenticatedUsername();
        verify(userRepository, times(1)).findUserByUsername(user.getUsername());
        verify(expenseDao, times(1)).findExpenseById(expenseId);
        verify(expenseMapper, times(1)).apply(initialExpense);
    }

    @Test
    public void should_not_find_expense_by_id() {
        // given
        UUID expenseId = UUID.randomUUID();

        ExpenseDto expectedExpenseDto = new ExpenseDto(
                initialExpense.getName(),
                initialExpense.getCategory().getName(),
                initialExpense.getPrice(),
                initialExpense.getQuantity(),
                initialExpense.getOwner().getId(),
                initialExpense.getDate()
        );

        when(userContextService.getAuthenticatedUsername()).thenReturn(user.getUsername());
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(expenseDao.findExpenseById(expenseId)).thenReturn(Optional.of(initialExpense));
        when(expenseMapper.apply(initialExpense)).thenReturn(expectedExpenseDto);

        // when
        ExpenseDto result = expenseService.getExpenseDetails(expenseId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(expectedExpenseDto.name());
        assertThat(result.price()).isEqualTo(expectedExpenseDto.price());
        assertThat(result.quantity()).isEqualTo(expectedExpenseDto.quantity());
        assertThat(result.ownerId()).isEqualTo(expectedExpenseDto.ownerId());
        assertThat(result.categoryName()).isEqualTo(expectedExpenseDto.categoryName());
        assertThat(result.date()).isEqualTo(expectedExpenseDto.date());

        verify(userContextService, times(1)).getAuthenticatedUsername();
        verify(userRepository, times(1)).findUserByUsername(user.getUsername());
        verify(expenseDao, times(1)).findExpenseById(expenseId);
        verify(expenseMapper, times(1)).apply(initialExpense);
    }






}