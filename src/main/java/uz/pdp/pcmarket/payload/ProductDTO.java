package uz.pdp.pcmarket.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ProductDTO {
    private String name;
    private String description;
    private double price;
    private Integer categoryId;
    private Integer attachmentId;
}
