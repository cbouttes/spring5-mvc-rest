package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Categories;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class CustomerServiceTest extends TestCase {

    public static final Long ID = 2L;

    public static final String FIRSTNAME = "Michael";
    public static final String LASTNAME = "Lachappele";

    CustomerService customerService;
    @Mock
    private CustomerRepository customerRepository;

    @Before
    public void setUp() throws Exception {
        //super.setUp();
        MockitoAnnotations.initMocks(this);
        customerService = new CustomerServiceImpl(CustomerMapper.INSTANCE,customerRepository);
    }

    public void testGetAllCustomers() {

        //given
        List<Customer> customers = Arrays.asList(new Customer(), new Customer(), new Customer());
        when(customerRepository.findAll()).thenReturn(customers);

        //when
        List<CustomerDTO> customerDTOS = customerService.getAllCustomers();

        //then
        assertNotNull(customerDTOS);
        assertEquals(3, customerDTOS.size());

    }

    @Test
    public void testGetCustomerById() {

        //given
        Customer returnCustomer = new Customer();
        returnCustomer.setId(ID);
        returnCustomer.setFirstname(FIRSTNAME);
        returnCustomer.setLastname(LASTNAME);
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(returnCustomer));

        //when
        CustomerDTO customerDTO = customerService.getCustomerById(2L);

        //then
        assertNotNull(customerDTO);
        assertEquals(ID,customerDTO.getId());
        assertEquals(FIRSTNAME,customerDTO.getFirstname());
        assertEquals(LASTNAME,customerDTO.getLastname());

    }
}