package com.amadeusz.ExpensesTracker.Expense;


import com.amadeusz.ExpensesTracker.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @Column(nullable = false)
    private String name;

    private String description;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Column(nullable = false)
    private LocalDate date = LocalDate.now();

}
