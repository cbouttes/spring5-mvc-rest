package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.bootstrap.Bootstrap;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * Created by mance on 13/05/2022.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class VendorServiceImplIT {

    @Autowired
    CategoryRepository categoryRespository;

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    VendorRepository vendorRepository;

    VendorService vendorService;

    VendorMapper vendorMapper;

    @Before
    public void setUp() throws Exception {
        System.out.println("Loading Vendor Data");
        System.out.println(vendorRepository.findAll().size());

        //setup data for testing
        Bootstrap bootstrap = new Bootstrap(categoryRespository, customerRepository, vendorRepository);
        bootstrap.run();//load data

        vendorService = new VendorServiceImpl(vendorRepository, VendorMapper.INSTANCE);
        vendorMapper = VendorMapper.INSTANCE;
    }

    @Test
    public void getVendors() {

        //Given
        //Bootstrap load data

        //When
        List<Vendor> vendors = vendorRepository.findAll();

        //Then
        assertEquals(2, vendors.size());

    }

    @Test
    public void getVendorById() {

        //Given
        //Bootstrap load data
        Long firstIdValue = getVendorFirstIdValue();

        //When
        VendorDTO vendorDTO = vendorService.getVendorById(firstIdValue);
        Vendor vendor = vendorRepository.findById(firstIdValue).get();

        //Then
        assertEquals(vendor.getName(), vendorDTO.getName());

    }

    @Test
    public void createNewVendor() {
        //Given
        String createdName = "Tata";
        Vendor vendorToCreate = new Vendor();
        vendorToCreate.setName(createdName);
        VendorDTO vendorToCreateDTO = vendorMapper.vendorToVendorDTO(vendorToCreate);
        //When
        vendorService.createNewVendor(vendorToCreateDTO);
        Long id = getVendorLastIdValue();
        Vendor createdVendor = vendorRepository.findById(id).get();
        //Then
        assertEquals(createdName, createdVendor.getName());
    }

    @Test
    public void saveVendor() {
        //Given
        String updatedName = "UpdatedName";
        //récupérer l'id du premier
        Long id = getVendorFirstIdValue();
        Vendor originalVendor = vendorRepository.findById(id).get();
        VendorDTO vendorToSaveDTO = vendorMapper.vendorToVendorDTO(originalVendor);
        String originalName = vendorToSaveDTO.getName();
        vendorToSaveDTO.setName(updatedName);

        //When
        vendorService.saveVendor(id, vendorToSaveDTO);

        //Then
        Vendor updatedVendor = vendorRepository.findById(id).get();
        assertNotNull(updatedVendor);
        assertNotEquals(originalName,updatedVendor.getName());
        assertEquals(updatedName, updatedVendor.getName());
    }

    @Test
    public void patchVendor() {
        //Given
        String updatedName = "UpdatedName";
        //récupérer l'id du premier
        Long id = getVendorFirstIdValue();
        Vendor originalVendor = vendorRepository.findById(id).get();
        VendorDTO vendorToPatchDTO = vendorMapper.vendorToVendorDTO(originalVendor);
        String originalName = vendorToPatchDTO.getName();
        vendorToPatchDTO.setName(updatedName);

        //When
        vendorService.patchVendor(id, vendorToPatchDTO);

        //Then
        Vendor updatedVendor = vendorRepository.findById(id).get();
        assertNotNull(updatedVendor);
        assertNotEquals(originalName,updatedVendor.getName());
        assertEquals(updatedName, updatedVendor.getName());
    }

    @Test
    public void deleteVendorById() {
        //Given
        List<Vendor> originalVendors = vendorRepository.findAll();
        int originalSize = originalVendors.size();
        //When
        vendorService.deleteVendorById(getVendorFirstIdValue());
        //Then
        List<Vendor> afterDeleteVendors = vendorRepository.findAll();
        int afterDeleteSize = afterDeleteVendors.size();
        assertNotEquals(originalSize, afterDeleteSize);
        assertEquals(originalSize-1,afterDeleteSize);

    }

    private Long getVendorFirstIdValue(){
        List<Vendor> vendors = vendorRepository.findAll();
        System.out.println("Vendors Found: " + vendors.size());
        //return first id
        return vendors.get(0).getId();
    }

    private Long getVendorLastIdValue(){
        List<Vendor> vendors = vendorRepository.findAll();
        System.out.println("Vendors Found: " + vendors.size());
        //return last id
        int size = vendors.size();
        return vendors.get(size-1).getId();
    }

}