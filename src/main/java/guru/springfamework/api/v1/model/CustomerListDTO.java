package guru.springfamework.api.v1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * The type Customer list dto.
 */
@Data
@AllArgsConstructor
public class CustomerListDTO {

    private List<CustomerDTO> customers;

}
