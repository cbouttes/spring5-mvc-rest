package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.VendorMapper;
import guru.springfamework.api.v1.model.VendorDTO;
import guru.springfamework.api.v1.model.VendorListDTO;
import guru.springfamework.controllers.v1.VendorController;
import guru.springfamework.domain.Vendor;
import guru.springfamework.repositories.VendorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by mance on 09/05/2022.
 */
@Service
public class VendorServiceImpl implements VendorService {

    private VendorRepository vendorRepository;
    private VendorMapper vendorMapper;

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
    }

    @Override
    public VendorListDTO getVendors() {
        //return vendorRepository.findAll().stream().map(vendorMapper::vendorToVendorDTO).collect(Collectors.toList());
        //return vendorRepository.findAll().stream().map(vendor -> vendorMapper.vendorToVendorDTO(vendor)).collect(Collectors.toList());
        List<VendorDTO> vendorDTOS = vendorRepository.findAll().stream().map(vendor -> {
            VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(vendor);
            vendorDTO.setVendorUrl(getVendorUrl(vendor.getId()));
            return vendorDTO;
        }).collect(Collectors.toList());
        VendorListDTO vendorListDTO = new VendorListDTO(vendorDTOS);
        return vendorListDTO;
    }

    @Override
    public VendorDTO getVendorById(Long id) {
        //return vendorRepository.findById(id).map(vendorMapper::vendorToVendorDTO).get();
        //return vendorMapper.vendorToVendorDTO(vendorRepository.findById(id).get());
        Vendor returnVendor = vendorRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        VendorDTO vendorDTO = vendorMapper.vendorToVendorDTO(returnVendor);
        vendorDTO.setVendorUrl(getVendorUrl(id));
        return vendorDTO;
    }

    @Override
    public VendorDTO createNewVendor(VendorDTO vendorDTO) {
        return saveAndReturnDTO(vendorMapper.vendorDTOToVendor(vendorDTO));
    }

    @Override
    public VendorDTO saveVendor(Long id, VendorDTO vendorDTO) {
        if (vendorRepository.findById(id).isPresent()) {
            Vendor vendorToSave = vendorMapper.vendorDTOToVendor(vendorDTO);
            //C'est Hibernate qui fait le boulot
            vendorToSave.setId(id);
            return saveAndReturnDTO(vendorToSave);
        } else {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public VendorDTO patchVendor(Long id, VendorDTO vendorDTO) {
        return vendorRepository.findById(id).map(vendor -> {

            //ici on ne sette que les attributs qui sont différents de null (mis à jour partielle)

            if (vendorDTO.getName() != null) {
                vendor.setName(vendorDTO.getName());
            }
            return saveAndReturnDTO(vendor);
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public void deleteVendorById(Long id) {
        vendorRepository.delete(vendorRepository.findById(id).orElseThrow(ResourceNotFoundException::new));
    }

    private VendorDTO saveAndReturnDTO(Vendor vendor) {
        Vendor savedVendor = vendorRepository.save(vendor);
        VendorDTO returnDTO = vendorMapper.vendorToVendorDTO(savedVendor);
        returnDTO.setVendorUrl(getVendorUrl(savedVendor.getId()));
        return returnDTO;
    }

    private String getVendorUrl(Long id) {
        return VendorController.BASE_URL+"/"+id;
    }

}


