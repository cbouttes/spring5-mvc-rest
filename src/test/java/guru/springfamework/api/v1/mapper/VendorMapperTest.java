package guru.springfamework.api.v1.mapper;

import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.domain.Vendor;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by mance on 12/05/2022.
 */
public class VendorMapperTest extends TestCase {

    public static final String NAME = "Toto";

    VendorMapper vendorMapper = VendorMapper.INSTANCE;

    @Test
    public void testVendorToVendorDTO() throws Exception {
        //given
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        vendor.setName(NAME);

        //when
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);

        //then
        assertEquals(NAME,vendor.getName());
    }

    public void testVendorDTOToVendor() throws Exception {
        //given
        VendorDTO vendorDTO = new VendorDTO();
        vendorDTO.setName(NAME);

        //when
        Vendor vendor = vendorMapper.vendorDTOToVendor(vendorDTO);

        //then
        assertEquals(NAME,vendor.getName());
    }
}