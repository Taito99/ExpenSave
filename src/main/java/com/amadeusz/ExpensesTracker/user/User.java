package com.amadeusz.ExpensesTracker.user;

import com.amadeusz.ExpensesTracker.Expense.Expense;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_user")
@ToString
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private BigDecimal monthlyBudget;

    @Column(columnDefinition="Decimal(10,2) default '0.00'")
    private BigDecimal availableBudget;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "owner",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<Expense> expenses = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "user_category_limits", joinColumns = @JoinColumn(name = "user_id"))
    @MapKeyColumn(name = "category_name")
    @Column(name = "limit_amount")
    private Map<String, BigDecimal> categoryLimits = new HashMap<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    private void prePersist() {
    if (monthlyBudget == null) {
        monthlyBudget = BigDecimal.ZERO;
    }
    if (availableBudget == null) {
        availableBudget = BigDecimal.ZERO;
    }
}

}
