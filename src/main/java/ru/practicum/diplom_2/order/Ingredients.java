package ru.practicum.diplom_2.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Ingredients {
    private boolean success;
    private List<Data> data;
}
