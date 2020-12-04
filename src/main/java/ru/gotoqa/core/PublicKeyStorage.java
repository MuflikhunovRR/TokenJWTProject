package ru.gotoqa.core;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.gotoqa.models.PublicKeyResponse;
import ru.gotoqa.models.PublicKeysResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.gotoqa.util.Utils.getStringFromFile;


/**
 * API. Creating response and storing a public key
 */
@Getter
@Setter
@Service
public class PublicKeyStorage {
    private static final Logger LOGGER = LoggerFactory.getLogger(PublicKeyStorage.class);

    private static final String DEFAULT_PUBLIC_KEY_PATH = "src/main/resources/keys/publicOneString.txt";

    /**
     * Public key storage ()
     */
    private List<PublicKeyResponse> publicKeyList = new ArrayList<>();

    /**
     * Public key storage ()
     * k - User email
     * v - PublicKeysResponse
     */
    private Map<String, PublicKeysResponse> publicKeysResponseList = new HashMap<>();

    public PublicKeyResponse applyDefaultKeyFilters() {
        return PublicKeyResponse.builder()
                .publicKey(getStringFromFile(DEFAULT_PUBLIC_KEY_PATH))
                .build();
    }

    public PublicKeysResponse applyKeysFilters(String email) {
        if (publicKeysResponseList.containsKey(email)) {
            return publicKeysResponseList.get(email);
        } else {
           return PublicKeysResponse.builder()
                    .defaultKey(getStringFromFile(DEFAULT_PUBLIC_KEY_PATH))
                    .build();
        }
    }

    public void addKeys(String email, String publicKey){
        PublicKeysResponse keysResponse =
                PublicKeysResponse.builder()
                        .publicKey(publicKey)
                        .defaultKey(getStringFromFile(DEFAULT_PUBLIC_KEY_PATH))
                        .build();
        publicKeysResponseList.put(email, keysResponse);
    }

    public void clearAllKeys() {
        publicKeyList.clear();
        publicKeysResponseList.clear();
        LOGGER.info("Clear all Keys filters");
    }

}