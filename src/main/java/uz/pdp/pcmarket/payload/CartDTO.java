package uz.pdp.pcmarket.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.pcmarket.entity.Product;

import javax.persistence.ManyToOne;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class CartDTO {

    private Integer productId;

}
