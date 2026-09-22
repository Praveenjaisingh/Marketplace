package com.example.marketplace.controller;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.example.marketplace.util.ResponseUtil;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;
import com.example.marketplace.entity.Address;
import com.example.marketplace.service.AddressService;

@Component
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    public ServerResponse createAddress(ServerRequest request) throws Exception {
        try {
            Address address = request.body(Address.class);
            Address savedAddress = addressService.createAddress(address);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Address created successfully",
                "data", savedAddress
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAllAddresss(ServerRequest request) throws Exception {
        try {
            List<Address> allAddresss = addressService.getAllAddresss();
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Addresss fetched successfully",
                "data", allAddresss
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse getAddressById(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Address address = addressService.getAddressById(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Address fetched successfully",
                "data", address
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse updateAddress(ServerRequest request) {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            Address address = new Address();
            address.setUserId(((Number) body.get("userId")).intValue());
            address.setFullName((String) body.get("fullName"));
            address.setPhone((String) body.get("phone"));
            address.setAddressLine((String) body.get("addressLine"));
            address.setCity((String) body.get("city"));
            address.setState((String) body.get("state"));
            address.setPincode((String) body.get("pincode"));
            address.setCountry((String) body.get("country"));
            address.setDefaultAddress((Boolean) body.get("defaultAddress"));
            Address updatedAddress = addressService.updateAddress(id, address);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Address updated successfully",
                "data", updatedAddress
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }

    public ServerResponse deleteAddress(ServerRequest request) throws Exception {
        try {
            Map<String, Object> body = request.body(Map.class);
            int id = ((Number) body.get("id")).intValue();
            addressService.deleteAddress(id);
            return ServerResponse.ok().body(Map.of(
                "status", true,
                "message", "Address deleted successfully"
            ));
        } catch (Exception e) {
            return ResponseUtil.error(e);
        }
    }
}
