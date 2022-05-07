package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.api.v1.model.CustomerListDTO;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.services.CustomerService;
import junit.framework.TestCase;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.acl.Owner;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.ResponseEntity.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerControllerTest extends TestCase {

    public static final Long ID = 3L;

    public static final String FIRSTNAME = "Michael";
    public static final String LASTNAME = "Lachappele";

    @Mock
    CustomerService customerService;

    @InjectMocks
    CustomerController customerController;

    MockMvc mockMvc;

    public void setUp() throws Exception {
        //super.setUp();
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    public void testGetAllCustomers() throws Exception {
        //given
        List<CustomerDTO> customerDTOS = Arrays.asList(new CustomerDTO(),new CustomerDTO(), new CustomerDTO());
        when(customerService.getAllCustomers()).thenReturn(customerDTOS);

        //when
        //then
        mockMvc.perform(get("/api/v1/customers/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customers",hasSize(3)));
    }

    public void testGetCustomerById() throws Exception {
        //given
        CustomerDTO returnCustomerDTO = new CustomerDTO();
        returnCustomerDTO.setId(ID);
        returnCustomerDTO.setFirstname(FIRSTNAME);
        returnCustomerDTO.setLastname(LASTNAME);
        when(customerService.getCustomerById(anyLong())).thenReturn(returnCustomerDTO);

        //when
        //then
        mockMvc.perform(get("/api/v1/customers/"+Long.valueOf(ID))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname",equalTo(FIRSTNAME)));
    }
}