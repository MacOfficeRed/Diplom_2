package model.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Order {
    private String[] ingredients;

    public static Order getOrder() {
        return new Order(
                new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f", "61c0c5a71d1f82001bdaaa72"});
    }
}
