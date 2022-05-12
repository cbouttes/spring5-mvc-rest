package guru.springfamework.controllers.v1;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.controllers.RestResponseEntityExceptionHandler;
import guru.springfamework.domain.Vendor;
import guru.springfamework.services.VendorService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mance on 12/05/2022.
 */
public class VendorControllerTest extends AbstractRestControllerTest {

    private static String NAME1 = "Toto";
    private static String NAME2 = "Tata";

    @Mock
    VendorService vendorService;

    @InjectMocks
    VendorController vendorController;

    MockMvc mockMvc;

    VendorDTO vendorDTO1;
    VendorDTO vendorDTO2;

    @Before
    public void setUp() throws Exception {
        vendorDTO1 = new VendorDTO();
        vendorDTO1.setName("Toto");
        vendorDTO1.setVendorUrl(VendorController.BASE_URL.concat("/1"));
        vendorDTO2 = new VendorDTO();
        vendorDTO2.setName("Titi");
        vendorDTO2.setVendorUrl(VendorController.BASE_URL.concat("/2"));

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(vendorController).setControllerAdvice(new RestResponseEntityExceptionHandler()).build();
    }

    @Test
    public void testGetVendors() throws Exception {
        //Given
        //setup() initialize vendorDTO1
        //setup() initialize vendorDTO2
        List<VendorDTO> vendorDTOS = Arrays.asList(vendorDTO1, vendorDTO2);
        VendorListDTO vendorListDTO = new VendorListDTO(vendorDTOS);
        given(vendorService.getVendors()).willReturn(vendorListDTO);

        //When
        //Then
        mockMvc.perform(get(VendorController.BASE_URL).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.vendors", hasSize(2)));
        then(vendorService).should(times(1)).getVendors();
    }

    @Test
    public void testGetVendorById() throws Exception {
        //Given
        //setup() initialize vendorDTO1
        given(vendorService.getVendorById(anyLong())).willReturn(vendorDTO1);

        //When
        //Then
        mockMvc.perform(get(VendorController.BASE_URL + "/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.name", equalTo("Toto")));
        then(vendorService).should(times(1)).getVendorById(anyLong());
    }

    @Test
    public void testCreateVendor() throws Exception {
        //Given
        VendorDTO vendorToCreateDTO = new VendorDTO();
        vendorToCreateDTO.setName("Tutu");
        given(vendorService.createNewVendor(any(VendorDTO.class))).willReturn(vendorToCreateDTO);

        //When
        //Then
        mockMvc.perform(post(VendorController.BASE_URL).contentType(MediaType.APPLICATION_JSON).content(asJsonString(vendorToCreateDTO))).andExpect(status().isCreated()).andExpect(jsonPath("$.name", equalTo(vendorToCreateDTO.getName())));
        then(vendorService).should(times(1)).createNewVendor(any(VendorDTO.class));

    }

    @Test
    public void testUpdateVendor() throws Exception {
        //Given
        VendorDTO vendorToSaveDTO = new VendorDTO();
        vendorToSaveDTO.setName("TutuSave");
        given(vendorService.saveVendor(anyLong(),any(VendorDTO.class))).willReturn(vendorToSaveDTO);

        //When
        //Then
        mockMvc.perform(put(VendorController.BASE_URL+"/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(vendorToSaveDTO))).andExpect(status().isOk()).andExpect(jsonPath("$.name", equalTo(vendorToSaveDTO.getName())));
        then(vendorService).should(times(1)).saveVendor(anyLong(),any(VendorDTO.class));
    }

    @Test
    public void testPatchVendor() throws Exception {
        //Given
        VendorDTO vendorToPatchDTO = new VendorDTO();
        vendorToPatchDTO.setName("TutuPatch");
        given(vendorService.patchVendor(anyLong(),any(VendorDTO.class))).willReturn(vendorToPatchDTO);

        //When
        //Then
        mockMvc.perform(patch(VendorController.BASE_URL+"/1").contentType(MediaType.APPLICATION_JSON).content(asJsonString(vendorToPatchDTO))).andExpect(status().isOk()).andExpect(jsonPath("$.name", equalTo(vendorToPatchDTO.getName())));
        then(vendorService).should(times(1)).patchVendor(anyLong(),any(VendorDTO.class));
    }

    @Test
    public void testDeleteVendor() throws Exception {
        //Given

        //When
        //Then
        mockMvc.perform(delete(VendorController.BASE_URL+"/1")).andExpect(status().isOk());
        then(vendorService).should(times(1)).deleteVendorById(anyLong());
    }


}