package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;

import static org.junit.Assert.assertEquals;

public class CustomerMapperTest {

    public static final String FIRSTNAME = "Michael";
    public static final String LASTNAME = "Lachappele";

    public static final long ID = 1L;

    CustomerMapper customerMapper = CustomerMapper.INSTANCE;

    public void customerToCustomerDTO() throws Exception {

        //given
        Customer customer = new Customer();
        customer.setId(ID);
        customer.setFirstname("Michael");
        customer.setLastname("Lachappele");

        //when
        CustomerDTO customerDTO = customerMapper.customerToCustomerDTO(customer);

        //then
        assertEquals(Long.valueOf(ID),customerDTO.getId());
        assertEquals(FIRSTNAME,customerDTO.getLastname());
        assertEquals(LASTNAME,customerDTO.getFirstname());

    }

}
