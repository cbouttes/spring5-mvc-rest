package guru.springfamework.bootstrap;

import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.domain.Category;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Created by jt on 9/24/17.
 */
@Component
public class Bootstrap implements CommandLineRunner{

    private CategoryRepository categoryRespository;

    private CustomerRepository customerRepository;

    public Bootstrap(CategoryRepository categoryRespository, CustomerRepository customerRepository) {
        this.categoryRespository = categoryRespository;
        this.customerRepository = customerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Category fruits = new Category();
        fruits.setName("Fruits");

        Category dried = new Category();
        dried.setName("Dried");

        Category fresh = new Category();
        fresh.setName("Fresh");

        Category exotic = new Category();
        exotic.setName("Exotic");

        Category nuts = new Category();
        nuts.setName("Nuts");

        categoryRespository.save(fruits);
        categoryRespository.save(dried);
        categoryRespository.save(fresh);
        categoryRespository.save(exotic);
        categoryRespository.save(nuts);

        System.out.println("Data Category Loaded = " + categoryRespository.count() );

        Customer customerBL = new Customer();
        customerBL.setId(Long.valueOf(1));
        customerBL.setFirstname("Bob");
        customerBL.setLastname("Leponge");

        Customer customerCT = new Customer();
        customerCT.setId(Long.valueOf(2));
        customerCT.setFirstname("Carlo");
        customerCT.setLastname("Tentacule");

        customerRepository.save(customerBL);
        customerRepository.save(customerCT);

        System.out.println("Data Customer Loaded = " + customerRepository.count() );

    }
}
