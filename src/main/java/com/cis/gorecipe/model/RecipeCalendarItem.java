package com.cis.gorecipe.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

import static javax.persistence.TemporalType.DATE;

@Entity
@RequiredArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class RecipeCalendarItem {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    @ManyToOne
    private User user;

    @Column(nullable = false)
    @ManyToOne
    private Recipe recipe;


    @Column(nullable = false)
    @Temporal(DATE)
    private java.util.Date birthDate;
}
