package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import junit.framework.TestCase;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

/**
 * Created by mance on 12/05/2022.
 */
public class VendorServiceImplTest {

    public static final String NAME1 = "Toto";
    public static final String NAME2 = "Titi";
    VendorMapper vendorMapper = VendorMapper.INSTANCE;
    @Mock
    public VendorRepository vendorRepository;
    VendorService vendorService;

    Vendor vendor1;
    Vendor vendor2;
    List<Vendor> vendors;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        vendorService = new VendorServiceImpl(vendorRepository, vendorMapper);
        vendor1 = new Vendor();
        vendor1.setId(1L);
        vendor1.setName(NAME1);
        vendor2 = new Vendor();
        vendor2.setId(2L);
        vendor2.setName(NAME2);
        vendors = new ArrayList<>();
        vendors.add(vendor1);
        vendors.add(vendor2);
        //super.setUp();
    }

    @Test
    public void testGetVendors() throws Exception {
        //Given
        //setup() List<Vendor> vendors
        given(vendorRepository.findAll()).willReturn(vendors);

        //When
        VendorListDTO vendorListDTO = vendorService.getVendors();
        List<VendorDTO> vendorDTOS = vendorListDTO.getVendors();

        //Then
        assertNotNull(vendorDTOS);
        //assertEquals(2, vendorDTOS.size());
        assertThat(vendorDTOS.size(), Matchers.is(equalTo(2)));
        then(vendorRepository).should(times(1)).findAll();

    }

    @Test(expected = ResourceNotFoundException.class)
    public void testGetVendorByIdNotFound() throws Exception {
        //given
        //mockito BBD syntax since mockito 1.10.0
        given(vendorRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        VendorDTO vendorDTO = vendorService.getVendorById(1L);

        //then
        then(vendorRepository).should(times(1)).findById(anyLong());

    }

    @Test
    public void testGetVendorById() throws Exception {
        //Given / Donné (les prérequis)
        //setUp() initialize returnVendor
        when(vendorRepository.findById(anyLong())).thenReturn(Optional.of(vendor1));
        //Equivalent de :
        //mockito BBD syntax since mockito 1.10.0 (plus proche de given when then)
        given(vendorRepository.findById(anyLong())).willReturn(Optional.of(vendor1));

        //When / Quand (l'opération à tester est exécutée)
        VendorDTO vendorDTO = vendorService.getVendorById(1L);

        //Then / Alors (la vérification des résultats)
        assertNotNull(vendorDTO);
        //assertEquals(NAME1, vendorDTO.getName());
        assertThat(vendorDTO.getName(),is(equalTo(NAME1)));
        //Equivalent de :
        //JUnit Assert that with matchers
        assertThat(vendorDTO.getName(), is(equalTo(NAME1)));

        //Vérification du nombre d'invocations de vendorRepository.findById(1L) dans le test
        verify(vendorRepository).findById(any());
        //Equivalent de :
        verify(vendorRepository, times(1)).findById(anyLong());
        //Equivalent de :
        //mockito BBD syntax since mockito 1.10.0 (plus proche de given when then)
        then(vendorRepository).should(times(1)).findById(anyLong());
    }

    public void testCreateNewVendor() throws Exception {
        //Given
        Vendor vendorToCreate = new Vendor();
        vendorToCreate.setName("Tata");
        vendorToCreate.setId(1L); //obligé de setter car comme mocke le save on insère pas en base et donc on génère pas d'id
        given(vendorRepository.save(any(Vendor.class))).willReturn(vendorToCreate);
        VendorDTO vendorToCreateDTO = vendorMapper.vendorToVendorDTO(vendorToCreate);
        //When
        VendorDTO returnVendorDTO = vendorService.createNewVendor(vendorToCreateDTO);
        //Then
        assertNotNull(returnVendorDTO);
        //assertEquals("Tata", returnVendorDTO.getName());
        assertThat(returnVendorDTO.getName(), is(equalTo("Tata")));
        //assertEquals("/api/v1/vendors/".concat(vendorToCreate.getId().toString()), returnVendorDTO.getVendorUrl());
        assertThat(returnVendorDTO.getVendorUrl(), is(equalTo("/api/v1/vendors/".concat(vendorToCreate.getId().toString()))));
        then(vendorRepository).should(times(1)).save(any(Vendor.class));

    }

    public void testSaveVendor() throws Exception {

        //Given
        Vendor vendorToSave = new Vendor();
        vendorToSave.setId(1L);
        vendorToSave.setName("TotoSave");
        given(vendorRepository.findById(anyLong())).willReturn(Optional.of(vendorToSave));
        given(vendorRepository.save(any(Vendor.class))).willReturn(vendorToSave);
        VendorDTO vendorToSaveDTO = vendorMapper.vendorToVendorDTO(vendorToSave);

        //When
        VendorDTO returnVendorDTO = vendorService.saveVendor(1L, vendorToSaveDTO);

        //Then
        assertNotNull(returnVendorDTO);
        //assertEquals(vendorToSave.getName(), returnVendorDTO.getName());
        assertThat(vendorToSave.getName(), is(equalTo(returnVendorDTO.getName())));
        //assertEquals("/api/v1/vendors/".concat(vendorToSave.getId().toString()), returnVendorDTO.getVendorUrl());
        assertThat(returnVendorDTO.getVendorUrl(), is(equalTo("/api/v1/vendors/".concat(vendorToSave.getId().toString()))));
        then(vendorRepository).should(times(1)).findById(anyLong());
        then(vendorRepository).should(times(1)).save(any(Vendor.class));
    }

    public void testPatchVendor() throws Exception {

        //Given
        Vendor vendorToPatch = new Vendor();
        vendorToPatch.setId(1L);
        vendorToPatch.setName("TotoPatch");
        given(vendorRepository.findById(anyLong())).willReturn(Optional.of(vendorToPatch));
        given(vendorRepository.save(any(Vendor.class))).willReturn(vendorToPatch);
        VendorDTO vendorToPatchDTO = vendorMapper.vendorToVendorDTO(vendorToPatch);

        //When
        VendorDTO returnVendorDTO = vendorService.patchVendor(1L, vendorToPatchDTO);

        //Then
        assertNotNull(returnVendorDTO);
        //assertEquals(vendorToPatch.getName(), returnVendorDTO.getName());
        assertThat(vendorToPatch.getName(), is(equalTo(returnVendorDTO.getName())));
        //assertEquals("/api/v1/vendors/".concat(vendorToPatch.getId().toString()), returnVendorDTO.getVendorUrl());
        assertThat(returnVendorDTO.getVendorUrl(), is(equalTo("/api/v1/vendors/".concat(vendorToPatch.getId().toString()))));
        then(vendorRepository).should(times(1)).findById(anyLong());
        then(vendorRepository).should(times(1)).save(any(Vendor.class));

    }

    @Test
    public void testDeleteVendorById() throws Exception {

        //When
        vendorService.deleteVendorById(1L);

        //Then
        then(vendorRepository).should(times(1)).deleteById(anyLong());

    }


}