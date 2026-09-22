package com.example.marketplace.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.example.marketplace.entity.Address;
import com.example.marketplace.repository.AddressRepository;
import com.example.marketplace.repository.UserRepository;
import com.example.marketplace.util.ValidationUtil;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ValidationUtil validationUtil;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository, ValidationUtil validationUtil) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.validationUtil = validationUtil;
    }

    private void validateForeignKeys(Address address) {
        if (!userRepository.existsById(address.getUserId())) {
            validationUtil.fail("userId", "references a user that does not exist");
        }
    }

    public Address createAddress(Address address) {
        validationUtil.validate(address);
        validateForeignKeys(address);
        return addressRepository.save(address);
    }

    public List<Address> getAllAddresss() {
        return addressRepository.findAll();
    }

    public Address getAddressById(int id) {
        return addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
    }

    public Address updateAddress(int id, Address addressDetails) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
        address.setUserId(addressDetails.getUserId());
        address.setFullName(addressDetails.getFullName());
        address.setPhone(addressDetails.getPhone());
        address.setAddressLine(addressDetails.getAddressLine());
        address.setCity(addressDetails.getCity());
        address.setState(addressDetails.getState());
        address.setPincode(addressDetails.getPincode());
        address.setCountry(addressDetails.getCountry());
        address.setDefaultAddress(addressDetails.isDefaultAddress());
        validationUtil.validate(address);
        validateForeignKeys(address);
        return addressRepository.save(address);
    }

    public void deleteAddress(int id) {
        Address address = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
        addressRepository.delete(address);
    }
}
