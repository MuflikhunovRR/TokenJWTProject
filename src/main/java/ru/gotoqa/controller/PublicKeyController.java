package ru.gotoqa.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gotoqa.core.PublicKeyStorage;
import ru.gotoqa.models.PublicKeyResponse;
import ru.gotoqa.models.PublicKeysResponse;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/auth-token")
@Api(tags = "Configuring responses to authentication requests")
public class PublicKeyController {

    private final PublicKeyStorage keyStorage;

    @Autowired
    public PublicKeyController(PublicKeyStorage keyStorage) {
        this.keyStorage = keyStorage;
    }

    @ApiOperation(value = "Add new public key")
    @PostMapping("/addPublicKey")
    public boolean addUcpIdFilter(String email, String publicKey) {
        keyStorage.addKeys(email, publicKey);
        return true;
    }

    /**
     * Get default Public key
     */
    @GetMapping("/default-authenticate")
    @ApiOperation(value = "Returns one public key (PKCS1 RSA)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved key"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    }
    )
    public PublicKeyResponse getPublicKey() {
        return keyStorage.applyDefaultKeyFilters();
    }

    /**
     * Get Public key by email
     */
    @GetMapping("/authenticate/{email}")
    @ApiOperation(value = "Returns default or new public keys (PKCS1 RSA)")
    public PublicKeysResponse getPublicKeys(@PathVariable String email) {
        return keyStorage.applyKeysFilters(email);
    }

    @ApiOperation(value = "Show all generated keys")
    @GetMapping("/showKeys")
    public List<Object> show() {
        return Arrays.asList(
                keyStorage.getPublicKeyList(),
                keyStorage.getPublicKeysResponseList());
    }

    @ApiOperation(value = "Clearing all generated keys")
    @DeleteMapping("/clear")
    public boolean clear() {
        keyStorage.clearAllKeys();
        return true;
    }

}